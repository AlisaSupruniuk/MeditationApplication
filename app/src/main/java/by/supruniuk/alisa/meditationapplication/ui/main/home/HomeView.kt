package by.supruniuk.alisa.meditationapplication.ui.main.home

import by.supruniuk.alisa.meditationapplication.models.ProfileModel

interface HomeView {
    fun defaultImage()
    fun showUserData(profileModel: ProfileModel)
    fun showProgressBar()
    fun hideProgressBar()
}