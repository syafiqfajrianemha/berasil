package com.berasil.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.berasil.databinding.ActivityRegisterBinding
import com.berasil.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterUsername.addTextChangedListener(registerTextWatcher)
        binding.edRegisterEmail.addTextChangedListener(registerTextWatcher)
        binding.edRegisterPassword.addTextChangedListener(registerTextWatcher)

        binding.buttonLogin.setOnClickListener {
            val toLoginPage = Intent(this, LoginActivity::class.java)
            startActivity(toLoginPage)
            finish()
        }
    }

    private val registerTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val edRegisterUsername = binding.edRegisterUsername.text.toString().trim()
            val edRegisterEmail = binding.edRegisterEmail.text.toString().trim()
            val edRegisterPassword = binding.edRegisterPassword.text.toString().trim()

            binding.registerButton.isEnabled =
                edRegisterUsername.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(edRegisterEmail)
                    .matches() && edRegisterPassword.length >= 8
        }

        override fun afterTextChanged(s: Editable) {
            // Do nothing.
        }
    }
}