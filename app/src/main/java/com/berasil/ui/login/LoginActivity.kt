package com.berasil.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.berasil.databinding.ActivityLoginBinding
import com.berasil.ui.CcViewModelFactory
import com.berasil.ui.main.MainActivity
import com.berasil.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        CcViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edLoginEmail.addTextChangedListener(loginTextWatcher)
        binding.edLoginPassword.addTextChangedListener(loginTextWatcher)

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.loginResponse.observe(this) { loginResponse ->
            if (loginResponse.success) {
                val toMainActivity = Intent(this, MainActivity::class.java)
                toMainActivity.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(toMainActivity)
                showToast(loginResponse.message)
                finish()
            } else {
                showToast(loginResponse.message)
            }
        }

        binding.loginButton.setOnClickListener {
            loginViewModel.login(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString()
            )
        }

        binding.toRegisterPage.setOnClickListener {
            val toRegisterPage = Intent(this, RegisterActivity::class.java)
            startActivity(toRegisterPage)
            finish()
        }
    }

    private val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val edLoginEmail = binding.edLoginEmail.text.toString().trim()
            val edLoginPassword = binding.edLoginPassword.text.toString().trim()

            binding.loginButton.isEnabled =
                Patterns.EMAIL_ADDRESS.matcher(edLoginEmail)
                    .matches() && edLoginPassword.length >= 8
        }

        override fun afterTextChanged(s: Editable) {
            // Do nothing.
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}