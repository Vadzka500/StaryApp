package com.sidspace.stary.ui.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
fun getSystemBarHeight(): Dp {

    val density = LocalDensity.current
    val inset = WindowInsets.statusBars.getTop(density)

    return with(density) {
        inset.toDp()
    }

}
