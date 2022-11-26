package com.example.googlefontapp

import android.graphics.Typeface
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontsContractCompat

class FontContract(val textView: TextView, val parent: AppCompatActivity) : FontsContractCompat.FontRequestCallback() {
    override fun onTypefaceRequestFailed(reason: Int) {
        super.onTypefaceRequestFailed(reason)
        if (reason == FAIL_REASON_FONT_LOAD_ERROR)
            textView.text = parent.getString(R.string.font_unable_to_load)
        else if (reason == FAIL_REASON_WRONG_CERTIFICATES)
            textView.text = parent.getString(R.string.wrong_certificate)
    }

    // Set the type face on success
    override fun onTypefaceRetrieved(typeface: Typeface?) {
        super.onTypefaceRetrieved(typeface)
        parent.runOnUiThread {
            textView.typeface = typeface
        }
    }
}