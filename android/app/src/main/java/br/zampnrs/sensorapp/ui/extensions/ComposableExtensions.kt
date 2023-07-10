package br.zampnrs.sensorapp.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun fontSizeResource(id: Int): TextUnit {
    return dimensionResource(id = id).value.sp
}
