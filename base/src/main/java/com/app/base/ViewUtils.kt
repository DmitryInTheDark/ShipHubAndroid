package com.app.base

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager

object ViewUtils {
    @Suppress("DEPRECATION")
    fun Activity.requireLoadingDialog(withCancelable: Boolean, cancelAction: () -> Unit): Dialog {   // ← лучше использовать Dialog, а не AlertDialog

        return Dialog(this, R.style.TransparentDialogStyle).apply {
            setContentView(R.layout.dialog_loading)

            // Основные настройки окна
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )

                // Копируем параметры статус-бара из Activity
                statusBarColor = this@requireLoadingDialog.window.statusBarColor
                decorView.systemUiVisibility = this@requireLoadingDialog.window.decorView.systemUiVisibility
            }

            setCancelable(withCancelable)
            setOnCancelListener { cancelAction.invoke() }

            // Дополнительная страховка
            setOnShowListener {
                window?.statusBarColor = this@requireLoadingDialog.window.statusBarColor
            }
        }
    }
}