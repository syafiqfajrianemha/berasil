package com.berasil.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.berasil.databinding.ActivityRegisterBinding
import com.berasil.helper.LoadingDialog
import com.berasil.ui.CcViewModelFactory
import com.berasil.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val loadingDialog by lazy { LoadingDialog(this) }

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
            if (it) loadingDialog.showLoading() else loadingDialog.hideLoading()
        }

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            if (registerResponse.success) {
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, registerResponse.message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                showSnackBarMessage(registerResponse.message)
            }
        }

        binding.registerButton.setOnClickListener {
            it.hideKeyboard()
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

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(this, binding.tvCreateAccount, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}