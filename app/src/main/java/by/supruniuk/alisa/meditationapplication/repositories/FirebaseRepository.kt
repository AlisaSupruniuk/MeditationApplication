package by.supruniuk.alisa.meditationapplication.repositories

import android.net.Uri
import by.supruniuk.alisa.meditationapplication.models.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun signIn(email: String, password: String): Single<ProfileModel> =
        Single.create {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        val profileModel = ProfileModel(
                            name = user.displayName ?: "",
                            email = user.email ?: "",
                            image = user.photoUrl ?: Uri.EMPTY,
                        )
                        it.onSuccess(profileModel)
                    } else {
                        it.onError(task.exception)
                    }
                }
        }

    fun signUp(email: String, password: String, name: String): Single<ProfileModel> =
        Single.create<ProfileModel> {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        val profileModel = ProfileModel(
                            name = user.displayName ?: "",
                            email = user.email ?: "",
                            image = user.photoUrl ?: Uri.EMPTY
                        )
                        it.onSuccess(profileModel)
                    } else {
                        it.onError(task.exception)
                    }
                }
        }
            .flatMap { updateDisplayName(name) }

    fun getUser(): Single<ProfileModel> = Single.create {
        val user = auth.currentUser
        if (user != null) {
            val profileModel = ProfileModel(
                name = user.displayName ?: "",
                email = user.email ?: "",
                image = user.photoUrl ?: Uri.EMPTY
            )
            it.onSuccess(profileModel)
        } else {
            it.onError(throw Throwable(message = "User not found..."))
        }
    }

    fun uploadImageToFirebase(imageUri: Uri): Single<ProfileModel> =
        Single.create<String> {
            val user: FirebaseUser = auth.currentUser!!
            val storageRef = storage.reference.child("users/${user.uid}/profile.jpg")
            storageRef.putFile(imageUri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    it.onError(task.exception)
                }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    it.onSuccess(task.result.toString())
                } else {
                    it.onError(task.exception)
                }
            }
        }.flatMap {
            updateImage(it)
        }

    fun signOut(): Completable = Completable.create {
        auth.signOut()
        it.onComplete()
    }

    fun sendEmail(email: String): Completable =
        Completable.create {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.onComplete()
                    } else {
                        it.onError(task.exception)
                    }
                }
        }

    private fun updateDisplayName(name: String): Single<ProfileModel> =
        Single.create {
            val user = auth.currentUser
            val profileUpdates = userProfileChangeRequest { displayName = name }
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profileModel = ProfileModel(
                            name = user.displayName ?: "",
                            email = user.email ?: "",
                            image = user.photoUrl ?: Uri.EMPTY
                        )
                        it.onSuccess(profileModel)
                    } else {
                        it.onError(task.exception)
                    }
                }
        }

    private fun updateImage(image: String): Single<ProfileModel> =
        Single.create {
            val user: FirebaseUser = auth.currentUser!!
            val profileUpdates = userProfileChangeRequest { photoUri = Uri.parse(image) }
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profileModel = ProfileModel(
                            name = user.displayName ?: "",
                            email = user.email ?: "",
                            image = user.photoUrl ?: Uri.EMPTY,
                        )
                        it.onSuccess(profileModel)
                    } else {
                        it.onError(task.exception)
                    }
                }
        }
}