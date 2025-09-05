package com.sidspace.stary.ui.sort

import com.sidspace.stary.ui.enums.SortDirection
import com.sidspace.stary.ui.enums.SortType
import com.sidspace.stary.ui.model.MovieUi


fun sortListMovies(
    list: List<MovieUi>,
    sortType: SortType,
    sortDirection: SortDirection
): List<MovieUi> {
    return when (sortType) {
        SortType.DATE -> {
            if (sortDirection == SortDirection.DESCENDING) {

                list.sortedBy { if (!it.isSeries) it.year else it.releaseStart }
                    .reversed()

            } else {

                list.sortedBy { if (!it.isSeries) it.year else it.releaseStart }

            }
        }

        SortType.NAME -> {

            if (sortDirection == SortDirection.DESCENDING)
                list.sortedBy { it.name }
                    .reversed()
            else {

                list.sortedBy { it.name }

            }

        }

        SortType.RATING -> {
            if (sortDirection == com.sidspace.stary.ui.enums.SortDirection.DESCENDING) {

                list.sortedBy { it.scoreKp }
                    .reversed()
            } else {

                list.sortedBy { it.scoreKp }


            }
        }

        SortType.NONE -> {
            list
        }
    }
}
