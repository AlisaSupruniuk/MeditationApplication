package by.supruniuk.alisa.meditationapplication.ui.main.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.supruniuk.alisa.meditationapplication.MainActivity
import by.supruniuk.alisa.meditationapplication.ui.main.onboarding.OnboardingActivity
import by.supruniuk.alisa.meditationapplication.databinding.ActivityLoginBinding
import by.supruniuk.alisa.meditationapplication.ui.main.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var binding: ActivityLoginBinding
    private val presenter = LoginPresenter()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter.attachView(this)
        preferences = getSharedPreferences("ONBOARD", Context.MODE_PRIVATE)

        binding.registerBtn.setOnClickListener() {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.loginBtn.setOnClickListener() {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show()
            } else {
                presenter.login(email, password)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            startMainActivity()
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    private fun startMainActivity() {
        if (isSeenOnboarding()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
    }

    private fun isSeenOnboarding(): Boolean {
        return preferences.getBoolean("SHOWED", false)
    }

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun showSuccessToast() {
        Toast.makeText(this, "Login Successful..", Toast.LENGTH_SHORT).show()
        startMainActivity()
    }

    override fun errorToast() {
        Toast.makeText(
            this,
            "Email or Password is wrong. Please try again.",
            Toast.LENGTH_SHORT
        ).show()
    }
}