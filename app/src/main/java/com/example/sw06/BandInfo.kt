package com.example.sw06

data class BandInfo(
    val name: String,
    val members: List<String>,
    val foundingYear: Int,
    val homeCountry: String,
    val bestOfCdCoverImageUrl: String?
)