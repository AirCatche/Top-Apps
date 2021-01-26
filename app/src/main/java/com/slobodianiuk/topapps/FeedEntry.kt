package com.slobodianiuk.topapps

class FeedEntry {
    var name = ""
    var artist = ""
    var releaseData = ""
    var summary = ""
    var imageURL = ""
    var price = ""
    var entryType = 0

    override fun toString(): String {
        return "Application = $name \nartist = $artist \nreleaseData = $releaseData \nsummary = $summary \nimageURL = $imageURL \nprice = $price"
    }


}