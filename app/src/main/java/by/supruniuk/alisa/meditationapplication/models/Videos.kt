package by.supruniuk.alisa.meditationapplication.models

import java.io.Serializable

class Videos(
    var url: String,
    var title: String,
    var description: String,
    var duration: String,
    var image: Int
) : Serializable