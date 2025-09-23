package com.sidspace.stary.home.data.utils

import androidx.compose.ui.graphics.toArgb
import com.sidspace.stary.data.model.database.FolderDBO
import com.sidspace.stary.home.data.uikit.Blue30
import com.sidspace.stary.home.data.uikit.Gray30
import com.sidspace.stary.home.data.uikit.Purple20
import com.sidspace.stary.home.data.uikit.Purple30
import com.sidspace.stary.home.data.uikit.Red30
import com.sidspace.stary.home.data.uikit.Yellow20

object FolderDefaults {
    val defaultFolders = listOf(
        FolderDBO(folderName = "Любимые", color = Purple20.toArgb(), imageResName = "img_hearts"),
        FolderDBO(folderName = "Избранные", color = Yellow20.toArgb(), imageResName = "img_stars"),
        FolderDBO(
            folderName = "Смотреть снова",
            color = Purple30.toArgb(),
            imageResName = "img_publish"
        ),
        FolderDBO(folderName = "Летние", color = Red30.toArgb(), imageResName = "img_sun"),
        FolderDBO(folderName = "Зимние", color = Blue30.toArgb(), imageResName = "img_snowflake"),
        FolderDBO(folderName = "Не понравились", color = Gray30.toArgb(), imageResName = "img_add"),
    )
}
