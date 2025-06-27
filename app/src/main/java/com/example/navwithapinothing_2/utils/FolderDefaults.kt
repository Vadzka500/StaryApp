package com.example.navwithapinothing_2.utils

import androidx.compose.ui.graphics.toArgb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.features.theme.Blue30
import com.example.navwithapinothing_2.features.theme.Gray30
import com.example.navwithapinothing_2.features.theme.Purple20
import com.example.navwithapinothing_2.features.theme.Purple30
import com.example.navwithapinothing_2.features.theme.Red30
import com.example.navwithapinothing_2.features.theme.Yellow20

/**
 * @Author: Vadim
 * @Date: 23.06.2025
 */
object FolderDefaults {
    val defaultFolders = listOf(
        Folder(folderName = "Любимые", color = Purple20.toArgb(), imageResName = "hearts"),
        Folder(folderName = "Избранные", color = Yellow20.toArgb(), imageResName = "stars"),
        Folder(
            folderName = "Смотреть снова",
            color = Purple30.toArgb(),
            imageResName = "publish"
        ),
        Folder(folderName = "Летние", color = Red30.toArgb(), imageResName = "sun_3"),
        Folder(folderName = "Зимние", color = Blue30.toArgb(), imageResName = "snowflake"),
        Folder(folderName = "Не понравились", color = Gray30.toArgb(), imageResName = "add"),
    )
}