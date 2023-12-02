package com.berasil.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berasil.databinding.FragmentAccountBinding
import com.berasil.ui.welcome.WelcomeActivity

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToWelcomePage.setOnClickListener {
            val toWelcomePage = Intent(activity, WelcomeActivity::class.java)
            startActivity(toWelcomePage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}