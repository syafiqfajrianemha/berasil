package com.berasil.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.berasil.databinding.ActivityRegisterBinding
import com.berasil.ui.CcViewModelFactory
import com.berasil.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        CcViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterName.addTextChangedListener(registerTextWatcher)
        binding.edRegisterEmail.addTextChangedListener(registerTextWatcher)
        binding.edRegisterPassword.addTextChangedListener(registerTextWatcher)

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            if (registerResponse.success) {
                showToast(registerResponse.message)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                showToast(registerResponse.message)
            }
        }

        binding.registerButton.setOnClickListener {
            registerViewModel.register(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        }

        binding.toLoginPage.setOnClickListener {
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
            val edRegisterName = binding.edRegisterName.text.toString().trim()
            val edRegisterEmail = binding.edRegisterEmail.text.toString().trim()
            val edRegisterPassword = binding.edRegisterPassword.text.toString().trim()

            binding.registerButton.isEnabled =
                edRegisterName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(edRegisterEmail)
                    .matches() && edRegisterPassword.length >= 8
        }

        override fun afterTextChanged(s: Editable) {
            // Do nothing.
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.registerButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}