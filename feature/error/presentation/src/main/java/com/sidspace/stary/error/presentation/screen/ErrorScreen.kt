package com.sidspace.stary.error.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidspace.stary.error.presentation.R
import com.sidspace.stary.ui.uikit.poppinsFort

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.error_ic),
                contentDescription = "Error",
                modifier = Modifier.padding(start = 15.dp)
            )
            Text(
                text = context.getString(R.string.error_str),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}