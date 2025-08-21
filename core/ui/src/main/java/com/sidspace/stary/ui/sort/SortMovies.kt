package com.sidspace.stary.ui.sort


fun sortListMovies(
    list: List<com.sidspace.stary.ui.model.MovieData>,
    sortType: com.sidspace.stary.ui.enum.SortType,
    sortDirection: com.sidspace.stary.ui.enum.SortDirection
): List<com.sidspace.stary.ui.model.MovieData> {
    return when (sortType) {
        _root_ide_package_.com.sidspace.stary.ui.enum.SortType.DATE -> {
            if (sortDirection == _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.DESCENDING) {

                list.sortedBy { if (!it.isSeries) it.year else it.releaseStart }
                    .reversed()

            } else {

                list.sortedBy { if (!it.isSeries) it.year else it.releaseStart }

            }
        }

        _root_ide_package_.com.sidspace.stary.ui.enum.SortType.NAME -> {

            if (sortDirection == _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.DESCENDING)
                list.sortedBy { it.name }
                    .reversed()
            else {

                list.sortedBy { it.name }

            }

        }

        _root_ide_package_.com.sidspace.stary.ui.enum.SortType.RATING -> {
            if (sortDirection == _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.DESCENDING) {

                list.sortedBy { it.scoreKp }
                    .reversed()
            } else {

                list.sortedBy { it.scoreKp }


            }
        }

        _root_ide_package_.com.sidspace.stary.ui.enum.SortType.NONE -> {
            list
        }
    }
}