package com.berasil.ui.detection.result

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.berasil.R
import com.berasil.data.local.entity.ResultDetection
import com.berasil.data.remote.response.DetectionResponse
import com.berasil.databinding.ActivityResultBinding
import com.berasil.helper.DateHelper
import com.berasil.ui.BerasilViewModelFactory
import com.berasil.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private val resultViewModel by viewModels<ResultViewModel>() {
        BerasilViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@ResultActivity, MainActivity::class.java))
                finish()
            }
        })

        val results = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(
                EXTRA_DETECTION_RESPONSE,
                DetectionResponse::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DETECTION_RESPONSE)
        }

        if (results != null) {
            val butirKepala = results.butirKepala.toString()
            val butirPatah = results.butirPatah.toString()
            val butirMenir = results.butirMenir.toString()
            val butirMerah = results.butirMerah.toString()
            val butirRusak = results.butirRusak.toString()
            val butirKapur = results.butirKapur.toString()
            val butirGabah = results.butirGabah.toString()
            val sekam = results.sekam.toString()
            val kutu = results.kutu.toString()
            val batu = results.batu.toString()
            val total = results.total.toString()
            val url = results.url

            binding.apply {
                Glide.with(this@ResultActivity)
                    .load(url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivResult)

                val quality = detectionQuality(
                    butirKepala.toFloat(),
                    butirPatah.toFloat(),
                    butirMenir.toFloat(),
                    butirMerah.toFloat(),
                    butirRusak.toFloat(),
                    butirKapur.toFloat(),
                    butirGabah.toFloat(),
                    sekam.toFloat(),
                    kutu.toFloat(),
                    batu.toFloat()
                )

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

                resultViewModel.insertResult(
                    ResultDetection(
                        null,
                        quality,
                        DateHelper.getCurrentDate(),
                        butirKepala,
                        butirPatah,
                        butirMenir,
                        butirMerah,
                        butirRusak,
                        butirKapur,
                        butirGabah,
                        sekam,
                        kutu,
                        batu,
                        total,
                        url
                    )
                )
            }
        }
    }

    private fun detectionQuality(
        butirKepala: Float,
        butirPatah: Float,
        butirMenir: Float,
        butirMerah: Float,
        butirRusak: Float,
        butirKapur: Float,
        butirGabah: Float,
        sekam: Float,
        kutu: Float,
        batu: Float
    ): String {

        var qualityResult = "Bawah"

        val sumRice = butirKepala + butirPatah + butirMenir
        val sumForeignObject = batu + kutu + sekam

        val percentageButirKepala = percentageRice(butirKepala, sumRice)
        val percentageButirPatah = percentageRice(butirPatah, sumRice)
        val percentageButirMenir = percentageRice(butirMenir, sumRice)
        val percentageButirMerah = percentageRice(butirMerah, sumRice)
        val percentageButirRusak = percentageRice(butirRusak, sumRice)
        val percentageButirKapur = percentageRice(butirKapur, sumRice)
        val percentageButirGabah = percentageRice(butirGabah, sumRice)

        val percentageForeignObject = percentageForeignObject(sumForeignObject, sumRice)

        if (
            percentageButirKepala >= 85.00 &&
            percentageButirPatah <= 14.5 &&
            percentageButirMenir <= 0.50 &&
            percentageButirMerah <= 0.50 &&
            percentageButirRusak <= 0.50 &&
            percentageButirKapur <= 0.50 &&
            // Benda Asing
            percentageForeignObject <= 0.01 &&
            // Gabah
            percentageButirGabah <= 1.00
        ) {
            qualityResult = "Premium"
        } else if (
            percentageButirKepala >= 80.00 &&
            percentageButirPatah <= 18.00 &&
            percentageButirMenir <= 2.00 &&
            percentageButirMerah <= 2.00 &&
            percentageButirRusak <= 2.00 &&
            percentageButirKapur <= 2.00 &&
            percentageForeignObject <= 0.02 &&
            percentageButirGabah <= 2.00
        ) {
            qualityResult = "Medium 1"
        } else if (
            percentageButirKepala >= 75.00 &&
            percentageButirPatah <= 22.00 &&
            percentageButirMenir <= 3.00 &&
            percentageButirMerah <= 3.00 &&
            percentageButirRusak <= 3.00 &&
            percentageButirKapur <= 3.00 &&
            percentageForeignObject <= 0.03 &&
            percentageButirGabah <= 3.00
        ) {
            qualityResult = "Medium 2"
        }

        return qualityResult
    }

    private fun percentageForeignObject(sumForeignObject: Float, sumRice: Float): Float {
        return (sumForeignObject / sumRice) * 100
    }

    private fun percentageRice(total: Float, sumRice: Float): Float {
        return (total / sumRice) * 100
    }

    companion object {
        const val EXTRA_DETECTION_RESPONSE = "extra_detection_response"
    }
}