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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val state by homeViewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        homeViewModel.intent(HomeContract.Intent.StartConnection)
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
                    text = stringResource(R.string.temperature_label, state.dht11.temperature),
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
                    text = stringResource(R.string.humidity_label, state.dht11.humidity),
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
                    text = stringResource(R.string.heat_index_label, state.dht11.heatIndex),
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
                text = stringResource(R.string.angle_label, state.servoMotor),
                fontSize = fontSizeResource(id = R.dimen.default_text_size)
            )

            CircularSlider(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.5f)
            ) { angle ->
                homeViewModel.intent(HomeContract.Intent.RotateServo(angle.toDegrees()))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier.fillMaxSize())
}
