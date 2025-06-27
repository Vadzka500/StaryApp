package com.example.navwithapinothing_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.navwithapinothing_2.features.screen.MainScreen
import com.example.navwithapinothing_2.features.theme.NavWithApiNothing_2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()



        setContent {
            NavWithApiNothing_2Theme {
                MainScreen()
            }
        }
    }
}