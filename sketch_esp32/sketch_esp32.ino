#include "DHT.h"
#include <ESP32Servo.h>
#include <WiFi.h>
#include <PubSubClient.h>

#define DHTPIN 26
#define DHTTYPE DHT11
#define SERVOPIN 27

DHT dht(DHTPIN, DHTTYPE);
Servo servo;

const char* ssid = "REPLACE_WITH_YOUR_SSID";
const char* password = "REPLACE_WITH_YOUR_PASSWORD";
const char* mqtt_server = "broker.hivemq.com";
int mqtt_port = 1883;

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  Serial.begin(9600);

  dht.begin();
  servo.attach(SERVOPIN);

  setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();
  float heatIndex = dht.computeHeatIndex(temperature, humidity, false);

  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("Failed to get DHT11 data :(");
  } else {
    char humidityString[8];
    dtostrf(humidity, 2, 2, humidityString);
    client.publish("/SensorApp/humidity", humidityString);

    char temperatureString[8];
    dtostrf(temperature, 2, 2, temperatureString);
    client.publish("/SensorApp/temperature", temperatureString);

    char heatIndexString[8];
    dtostrf(heatIndex, 2, 2, heatIndexString);
    client.publish("/SensorApp/heatIndex", heatIndexString);
  }
}

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();

  if (String(topic) == "/SensorApp/servo") {
    servo.write(messageTemp.toInt());
    delay(15);
  }
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    
    if (client.connect("ESP32Client")) {
      Serial.println("connected");
      client.subscribe("/SensorApp/servo");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      
      delay(3000);
    }
  }
}
