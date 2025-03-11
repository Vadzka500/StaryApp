package com.example.navwithapinothing_2.navigation

/**
 * @Author: Vadim
 * @Date: 18.12.2024
 */

sealed interface Screen {

    data object Screen1: Screen

    data object Screen2: Screen

    data object Screen3: Screen
}

