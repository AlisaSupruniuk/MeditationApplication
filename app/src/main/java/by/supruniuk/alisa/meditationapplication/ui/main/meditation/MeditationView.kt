package by.supruniuk.alisa.meditationapplication.ui.main.meditation

import by.supruniuk.alisa.meditationapplication.models.MeditationModel

interface MeditationView {
    fun showMeditationList(list: List<MeditationModel>)
    fun showErrorToast()
}