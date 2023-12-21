package com.berasil.ui.price

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.berasil.R
import com.berasil.databinding.FragmentDialogCheckPriceBinding
import com.berasil.helper.CurrencyFormat
import com.berasil.helper.DateHelper
import com.berasil.helper.LoadingDialog
import com.berasil.ui.BiViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogCheckPriceFragment(private var quality: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogCheckPriceBinding

    private var priceTypeValue: Int? = null
    private var provIdValue: Int? = null

    private var priceListener: OnPriceCheckedListener? = null

    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    private val dialogCheckPriceViewModel by viewModels<DialogCheckPriceViewModel> {
        BiViewModelFactory.getInstance()
    }

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogCheckPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val priceType = resources.getStringArray(R.array.priceType)
        val adapterPriceType = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            priceType
        )
        binding.spinnerPriceType.adapter = adapterPriceType
        binding.spinnerPriceType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    priceTypeValue = position + 1
                    saveSelectedValue("priceType", priceTypeValue!!)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.buttonCheckPrice.isEnabled = false
                }
            }

        val savedPriceType = sharedPref.getInt("priceType", -1)
        if (savedPriceType != -1) {
            binding.spinnerPriceType.setSelection(savedPriceType - 1)
        }

        val provId = resources.getStringArray(R.array.provId)
        val adapterProvId = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            provId
        )
        binding.spinnerProvId.adapter = adapterProvId
        binding.spinnerProvId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                provIdValue = position + 1
                saveSelectedValue("provId", provIdValue!!)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.buttonCheckPrice.isEnabled = false
            }
        }

        val savedProvId = sharedPref.getInt("provId", -1)
        if (savedProvId != -1) {
            binding.spinnerProvId.setSelection(savedProvId - 1)
        }

        when (quality) {
            "Premium" -> {
                quality = "1_5"
            }

            "Medium 1" -> {
                quality = "1_3"
            }

            "Medium 2" -> {
                quality = "1_4"
            }

            "Bawah" -> {
                quality = "1_1"
            }
        }

        dialogCheckPriceViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.showLoading() else loadingDialog.hideLoading()
        }

        binding.buttonCheckPrice.setOnClickListener {
            dialogCheckPriceViewModel.checkRicePrice(
                DateHelper.getYesterdayDate(),
                quality,
                priceTypeValue!!,
                provIdValue!!
            )

            dialogCheckPriceViewModel.ricePrice.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    val price = CurrencyFormat().rupiah(it.first().nilai.toInt())
                    priceListener?.onPriceChecked(price)
                } else {
                    showDialog()
                }
                dialog?.dismiss()
            }
        }
    }

    private fun saveSelectedValue(key: String, value: Int) {
        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun setOnPriceCheckedListener(listener: OnPriceCheckedListener) {
        this.priceListener = listener
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Gagal")
            .setMessage("Harga Pasar di Provinsi Anda Belum Tersedia dengan Segmentasi Pasar yang Anda Pilih")
            .setCancelable(false)
            .setPositiveButton("Tutup") { _, _ ->
                dismiss()
            }
            .show()
    }

    interface OnPriceCheckedListener {
        fun onPriceChecked(price: String)
    }
}