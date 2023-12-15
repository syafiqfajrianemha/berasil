package com.berasil.helper

import android.app.AlertDialog
import android.content.Context
import com.berasil.R

class LoadingDialog(private val context: Context) {

    private var loadingDialog: AlertDialog? = null

    fun showLoading() {
        if (loadingDialog == null) {
            val builder = AlertDialog.Builder(context)
            builder.setView(R.layout.progress_layout)
            builder.setCancelable(false)
            loadingDialog = builder.create()
        }
        loadingDialog?.show()
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }
}