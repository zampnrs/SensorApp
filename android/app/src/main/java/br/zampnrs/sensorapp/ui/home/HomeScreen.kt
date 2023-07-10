package br.zampnrs.sensorapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import br.zampnrs.sensorapp.R
import br.zampnrs.sensorapp.ui.extensions.fontSizeResource
import br.zampnrs.sensorapp.ui.extensions.toDegrees

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    var angleValue by remember {
        mutableStateOf(0)
    }

    var temperatureValue by remember {
        mutableStateOf(0f)
    }
    
    var humidityValue by remember {
        mutableStateOf(0f)
    }

    var heatIndexValue by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.connectToServer()
    }

    homeViewModel.apply {
        temperature.value.let {
            temperatureValue = it
        }
        humidity.value.let {
            humidityValue = it
        }
        heatIndex.value.let {
            heatIndexValue = it
        }
    }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.default_space)
            )
        ) {
            Row {
                Text(
                    text = stringResource(R.string.temperature_label, temperatureValue),
                    fontSize = fontSizeResource(id = R.dimen.default_text_size)
                )
                Image(
                    painter = painterResource(id = R.drawable.thermometer),
                    contentDescription = ""
                )
            }
            
            Text(
                text = stringResource(id = R.string.label_plus),
                fontSize = fontSizeResource(id = R.dimen.default_text_size)
            )

            Row {
                Text(
                    text = stringResource(R.string.humidity_label, humidityValue),
                    fontSize = fontSizeResource(id = R.dimen.default_text_size)
                )
                Image(
                    painter = painterResource(id = R.drawable.humidity),
                    contentDescription = ""
                )
            }

            Text(
                text = stringResource(id = R.string.label_equal),
                fontSize = fontSizeResource(id = R.dimen.default_text_size)
            )
            
            Row {
                Text(
                    text = stringResource(R.string.heat_index_label, heatIndexValue),
                    fontSize = fontSizeResource(id = R.dimen.default_text_size)
                )
                Image(
                    painter = painterResource(id = R.drawable.thermometer),
                    contentDescription = ""
                )
            }
        }

        Box {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.angle_label, angleValue),
                fontSize = fontSizeResource(id = R.dimen.default_text_size)
            )

            CircularSlider(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.5f)
            ) { angle ->
                angle.toDegrees().let {
                    if (it != angleValue) {
                        angleValue = it
                        homeViewModel.publishServoAngle(it)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier.fillMaxSize())
}
