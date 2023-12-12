package com.berasil.ui.history.detail

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import com.berasil.R
import com.berasil.data.local.entity.ResultDetection
import com.berasil.databinding.ActivityDetailHistoryBinding
import com.berasil.ui.BerasilViewModelFactory
import com.berasil.ui.history.HistoryViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding

    private val historyViewModel by viewModels<HistoryViewModel> {
        BerasilViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val results = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(
                EXTRA_HISTORY,
                ResultDetection::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_HISTORY)
        }

        if (results != null) {
            val quality = results.quality
            val createdAt = results.createdAt
            val butirKepala = results.butirKepala
            val butirPatah = results.butirPatah
            val butirMenir = results.butirMenir
            val butirMerah = results.butirMerah
            val butirRusak = results.butirRusak
            val butirKapur = results.butirKapur
            val butirGabah = results.butirGabah
            val sekam = results.sekam
            val kutu = results.kutu
            val batu = results.batu
            val total = results.total
            val url = results.url

            binding.apply {
                tvCreatedAt.text = "Diambil pada: $createdAt"

                Glide.with(this@DetailHistoryActivity)
                    .load(url)
                    .into(ivResult)

                tvQuality.text = quality

                when (quality) {
                    "Premium" -> {
                        tvQuality.setTextColor(getColor(R.color.premium))
                        tvQuality.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.bg_premium)
                    }

                    "Medium 1" -> {
                        tvQuality.setTextColor(getColor(R.color.medium_1))
                        tvQuality.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.bg_medium_1)
                    }

                    "Medium 2" -> {
                        tvQuality.setTextColor(getColor(R.color.medium_2))
                        tvQuality.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.bg_medium_2)
                    }

                    else -> {
                        tvQuality.setTextColor(getColor(R.color.bawah))
                        tvQuality.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.bg_bawah)
                    }
                }

                tvButirKepala.text = butirKepala
                tvButirPatah.text = butirPatah
                tvButirMenir.text = butirMenir
                tvButirMerah.text = butirMerah
                tvButirRusak.text = butirRusak
                tvButirKapur.text = butirKapur
                tvButirGabah.text = butirGabah
                tvSekam.text = sekam
                tvKutu.text = kutu
                tvBatu.text = batu
            }

            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.detail_history_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_delete -> {
                            showLogoutDialog(results)
                            true
                        }

                        else -> false
                    }
                }
            })
        }
    }

    private fun showLogoutDialog(data: ResultDetection) {
        MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )
            .setTitle(R.string.delete)
            .setMessage(R.string.confirmation_delete)
            .setCancelable(false)
            .setPositiveButton(R.string.delete) { _, _ ->
                historyViewModel.deleteResult(data)
                finish()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    companion object {
        const val EXTRA_HISTORY = "extra_history"
    }
}