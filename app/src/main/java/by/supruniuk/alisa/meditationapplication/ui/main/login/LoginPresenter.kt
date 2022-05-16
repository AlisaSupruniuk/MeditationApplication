package by.supruniuk.alisa.meditationapplication.ui.main.login

import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import by.supruniuk.alisa.meditationapplication.repositories.FirebaseRepository
import by.supruniuk.alisa.meditationapplication.ui.main.home.HomePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginPresenter {
    private lateinit var view: LoginView
    private val firebaseRepository = FirebaseRepository()
    private val compositeDisposable = CompositeDisposable()

    fun attachView(view: LoginView) {
        this.view = view
    }

    fun login(email: String, password: String) {
        compositeDisposable.add(firebaseRepository.signIn(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    view.showSuccessToast()
                },
                onError = {
                    view.errorToast()
                    Log.d(HomePresenter.TAG, it.message.toString())
                }))
    }

    fun destroy(){
        compositeDisposable.dispose()
    }
}