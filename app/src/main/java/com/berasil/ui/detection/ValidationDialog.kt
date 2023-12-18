package com.berasil.ui.detection

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.berasil.R
import com.berasil.helper.DetectionListener

class ValidationDialog(
    private val _context: Context,
    private val data: Array<String>,
    private val selectedData: BooleanArray,
    private val detectionListener: DetectionListener
) : DialogFragment() {

    private var builder: AlertDialog.Builder? = null
    private var nextButton: AlertDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(_context)
        builder!!.setTitle("Pastikan gambar yang diunggah sesuai dengan persyaratan berikut:")
        builder!!.setMultiChoiceItems(data, selectedData) { _, _, _ ->
            updateNextButtonStatus()
        }
        builder!!.setPositiveButton(R.string.next) { dialogInterface, _ ->
            dialogInterface.dismiss()
            detectionListener.onDetectionRequested()
        }
        builder!!.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
            for (i in selectedData.indices) {
                selectedData[i] = false
            }
            dialogInterface.dismiss()
        }

        nextButton = builder!!.create()

        updateNextButtonStatus()

        return nextButton!!
    }

    override fun onStart() {
        super.onStart()
        updateNextButtonStatus()
    }

    private fun updateNextButtonStatus() {
        val isAllSelected = selectedData.all { it }
        nextButton?.getButton(Dialog.BUTTON_POSITIVE)?.isEnabled = isAllSelected
    }
}