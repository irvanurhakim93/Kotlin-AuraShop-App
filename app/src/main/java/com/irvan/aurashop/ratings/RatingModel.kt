package com.github.dimsmith.auraparfum.ratings

data class RatingModel (
        val productId: String,
        val oneStar: Int,
        val twoStar: Int,
        val threeStar: Int,
        val fourStar: Int,
        val fiveStar: Int
)