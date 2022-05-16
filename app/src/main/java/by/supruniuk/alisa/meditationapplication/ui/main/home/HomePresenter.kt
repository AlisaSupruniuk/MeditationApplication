package by.supruniuk.alisa.meditationapplication.ui.main.home

import android.net.Uri
import android.util.Log
import by.supruniuk.alisa.meditationapplication.repositories.FirebaseRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class HomePresenter {
    private lateinit var view: HomeView
    private val firebaseRepository = FirebaseRepository()

    fun attachView(view: HomeView){
        this.view = view
    }

    fun showUserContent() {
        firebaseRepository.getUser().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { profileModel ->
                    if (profileModel.image == Uri.EMPTY) {
                        view.defaultImage()
                    }
                    view.showUserData(profileModel)
                },
                onError = { Log.d(TAG, it.message.toString()) })
    }

    fun updateProfileImage(image: Uri?) {
        image?.let { it ->
            firebaseRepository.uploadImageToFirebase(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showProgressBar() }
                .doFinally { view.hideProgressBar() }
                .subscribeBy(
                    onSuccess = { view.showUserData(it) },
                    onError = { Log.d("sendImageToFirebase", it.message.orEmpty()) })

        }
    }

    fun logout() {
        TODO("Not yet implemented")
    }

    companion object {
        const val TAG = "HomePresenter"
    }
}