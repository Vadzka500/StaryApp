package com.sidspace.stary.random.presentation.mapper

import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.random.presentation.model.CollectionRandomUi




fun Collection.toCollectionRandomUi(): CollectionRandomUi {
    return CollectionRandomUi(
        name = name,
        slug = slug
    )
}