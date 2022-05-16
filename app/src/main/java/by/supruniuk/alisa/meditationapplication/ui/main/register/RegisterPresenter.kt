package by.supruniuk.alisa.meditationapplication.ui.main.register

import android.net.Uri
import android.util.Log
import by.supruniuk.alisa.meditationapplication.ui.main.login.LoginActivity
import by.supruniuk.alisa.meditationapplication.repositories.FirebaseRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class RegisterPresenter {
    private lateinit var view: RegisterView
    private val firebaseRepository = FirebaseRepository()
    private val compositeDisposable = CompositeDisposable()

    fun attachView(view: RegisterView) {
        this.view = view
    }

    private fun updateProfileImage(image: Uri?) {
        image?.let { it ->
            firebaseRepository.uploadImageToFirebase(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { },
                    onError = { Log.d("sendImageToFirebase", it.message.orEmpty()) })

        }
    }

    fun createUser(email: String, password: String, name: String, image: Uri?) {
        compositeDisposable.add(
            firebaseRepository.signUp(email, password, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        if (image != null) updateProfileImage(image)
                        view.successRegisterToast()
                    },
                    onError = { error ->
                        view.errorToast()
                        Log.d(LoginActivity.TAG, "Sign out is not success")

                    })
        )
    }

    fun destroy() {
        compositeDisposable.dispose()
    }

}
