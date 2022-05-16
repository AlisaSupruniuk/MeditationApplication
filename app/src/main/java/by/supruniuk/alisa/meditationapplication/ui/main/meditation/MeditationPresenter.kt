package by.supruniuk.alisa.meditationapplication.ui.main.meditation

import by.supruniuk.alisa.meditationapplication.models.MeditationModel
import by.supruniuk.alisa.meditationapplication.repositories.FirebaseRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MeditationPresenter {
    private lateinit var view: MeditationView
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    private val child = databaseReference.child("songs")
    var audio = ""

    fun attachView(view: MeditationView) {
        this.view = view
    }

    fun getMeditationList() {
        child.get().addOnSuccessListener {
            var list = mutableListOf<MeditationModel>()
            for (model in it.children){
                list.add(convertToModel(model.child("name").value.toString() ?: "empty",
                    model.child("last_name").value.toString() ?: "empty",
                    model.child("url").value.toString() ?: "empty"
                ))
            }
            view.showMeditationList(list)
        }.addOnFailureListener {
            view.showErrorToast()
        }
//        child.addValueEventListener( object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                var list = mutableListOf<MeditationModel>()
//                for (meditationSnapshot in snapshot.children){
//                    list.add(meditationSnapshot as MeditationModel)
//                }
//                view.showMeditationList(list)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }

    private fun convertToModel(name: String, last_name: String, url: String): MeditationModel =
        MeditationModel(name, last_name, url)
}