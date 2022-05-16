package by.supruniuk.alisa.meditationapplication.ui.main.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import by.supruniuk.alisa.meditationapplication.ui.main.login.LoginActivity
import by.supruniuk.alisa.meditationapplication.databinding.RegisterAtivityBinding
import by.supruniuk.alisa.meditationapplication.ui.main.home.HomeFragment

class RegisterActivity : AppCompatActivity(), RegisterView {

    private lateinit var binding: RegisterAtivityBinding
    private val presenter = RegisterPresenter()


    var selectedPhotoUri: Uri? = null

    private val contentResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedPhotoUri = uri
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterAtivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter.attachView(this)
        binding.loginBtn.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
        binding.createUserBtn.setOnClickListener {
            val firstname: String = binding.firstname.text.toString()
            val lastname: String = binding.lastname.text.toString()
            val email: String = binding.email.text.toString()
            val password: String = binding.password.text.toString()
            val passwordRepeat: String = binding.passwordRepeat.text.toString()

            if (password != passwordRepeat) {
                Toast.makeText(this, "Please check both password", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(firstname) &&
                TextUtils.isEmpty(lastname) &&
                TextUtils.isEmpty(email) &&
                TextUtils.isEmpty(password) &&
                TextUtils.isEmpty(passwordRepeat)
            ) {
                Toast.makeText(this, "Please add fully information..", Toast.LENGTH_SHORT).show()
            } else {
                presenter.createUser(email, password, "$firstname $lastname", selectedPhotoUri)
            }
        }
        binding.photoBtn.setOnClickListener() {
            contentResult.launch(HomeFragment.INPUT_TYPE)
        }
    }

    override fun successRegisterToast() {
        Toast.makeText(this, "User Created...", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun errorToast() {
        Toast.makeText(
            this,
            "Your internet connection is not stable. Try again...",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}