package com.berasil.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.berasil.databinding.ActivityLoginBinding
import com.berasil.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDontHaveAccount.setOnClickListener {
            val toRegisterPage = Intent(this, RegisterActivity::class.java)
            startActivity(toRegisterPage)
        }

        binding.buttonRegister.setOnClickListener {
            val toRegisterPage = Intent(this, RegisterActivity::class.java)
            startActivity(toRegisterPage)
        }
    }
}