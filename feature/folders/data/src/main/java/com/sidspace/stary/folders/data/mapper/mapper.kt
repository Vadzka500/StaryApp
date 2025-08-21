package com.sidspace.stary.folders.data.mapper

import com.sidspace.stary.data.model.database.FolderDBO
import com.sidspace.stary.domain.model.Folder

fun Folder.toFolderDBO(): FolderDBO{
    return FolderDBO(
        folderId = id,
        folderName = name,
        color = color,
        imageResName = imageResName
    )
}