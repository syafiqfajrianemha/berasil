package com.berasil.ui.price

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

class DialogCheckPriceFragment(private var quality: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogCheckPriceBinding

    private var priceTypeValue: Int? = null
    private var provIdValue: Int? = null

    private var priceListener: OnPriceCheckedListener? = null

    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    private val dialogCheckPriceViewModel by viewModels<DialogCheckPriceViewModel> {
        BiViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogCheckPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.buttonCheckPrice.isEnabled = false
                }
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
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.buttonCheckPrice.isEnabled = false
            }
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
                val price = CurrencyFormat().rupiah(it.first().nilai.toInt())
                priceListener?.onPriceChecked(price)
                dialog?.dismiss()
            }
        }
    }

    fun setOnPriceCheckedListener(listener: OnPriceCheckedListener) {
        this.priceListener = listener
    }

    interface OnPriceCheckedListener {
        fun onPriceChecked(price: String)
    }
}