package com.example.navwithapinothing_2.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.ui.components.AuthOption
import com.example.navwithapinothing_2.ui.components.MyTextField

@Preview
@Composable
fun LoginScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.login),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Login", fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
        }


        MyTextField(
            textFieldState = TextFieldState(),
            hint = "Email", leadingIcon = Icons.Outlined.Email,
            trailingIcon = Icons.Outlined.Check, modifier = Modifier.fillMaxWidth()
        )

        MyTextField(
            textFieldState = TextFieldState(),
            hint = "Password", leadingIcon = Icons.Outlined.Lock,
            trailingText = "Forgot?", modifier = Modifier.fillMaxWidth(), isPassword = true
        )

        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login", fontSize = 17.sp, modifier = Modifier.padding(vertical = 8.dp))
        }

        Text(text = "Or, login with...", fontSize = 14.sp, modifier = Modifier.align(Alignment.CenterHorizontally).alpha(0.5f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            AuthOption(image = R.drawable.google)
            AuthOption(image = R.drawable.facebook)
            AuthOption(image = R.drawable.apple, tint = MaterialTheme.colorScheme.onBackground)
        }

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Don't have an account?")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Register", modifier = Modifier.clickable {  })
        }
        Spacer(modifier = Modifier.height(1.dp))
    }
}