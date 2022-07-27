package com.premelc.shows_dominik_premelc

import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show

object ShowsObject {

    private val reviews: List<Review> = listOf(
        Review(
            "petra_benjak",
            "Petra Benjak",
            5F,
            "Najbolja stvar koju sam ikad gledala",
            R.mipmap.pfp
        ),
        Review(
            "premo",
            "Premo",
            4F,
            "Najbolja stvar koju sam ikad gledao",
            R.mipmap.pfp
        ),
        Review(
            "zigmund123",
            "zigmund123",
            2F,
            "ne kuzim",
            R.mipmap.pfp
        )
    )

    val showsList: List<Show> = listOf(
        Show(
            "the_office",
            "The Office",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            reviews,
            "https://roost.nbcuni.com/bin/viewasset.html/content/dam/Peacock/Campaign/landingpages/library/theoffice/mainpage/office-social-min.png/_jcr_content/renditions/original"
        ),
        Show(
            "stranger_things",
            "Stranger Things",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            reviews,
            "https://roost.nbcuni.com/bin/viewasset.html/content/dam/Peacock/Campaign/landingpages/library/theoffice/mainpage/office-social-min.png/_jcr_content/renditions/original"

        ),
        Show(
            "krv_nije_voda",
            "Krv nije voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            reviews,
            "https://roost.nbcuni.com/bin/viewasset.html/content/dam/Peacock/Campaign/landingpages/library/theoffice/mainpage/office-social-min.png/_jcr_content/renditions/original"
        )
    )

    fun findShowById(id: String): Show? {
        for (item in showsList) {
            if (item.id == id) {
                return item
            }
        }
        return null
    }

}