package com.example.googlefontapp

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.recyclerview.widget.RecyclerView

class FontAdapter(private val fonts: List<FontData>, private val parent: AppCompatActivity) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

    inner class FontViewHolder(fontView: View) : RecyclerView.ViewHolder(fontView)

    var sampleText = String()
    val activeFontSamples = mutableListOf<TextView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.font_row_item, parent, false)
        return FontViewHolder(view)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.itemView.apply {
            val request = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                fonts[position].family,
                R.array.com_google_android_gms_fonts_certs_dev
            )

            // Request Font
            findViewById<TextView>(R.id.tvFontSample).apply {
                val handler = android.os.Handler(Looper.getMainLooper())
                val callback = FontContract(this, this@FontAdapter.parent)
                FontsContractCompat.requestFont(this@FontAdapter.parent, request, callback, handler)
                this.text = sampleText
                activeFontSamples.add(this)
            }

            findViewById<TextView>(R.id.tvFontFamily).text = fonts[position].family
            findViewById<TextView>(R.id.tvFontCategory).text = fonts[position].category
        }
    }

    override fun onViewRecycled(holder: FontViewHolder) {
        super.onViewRecycled(holder)

        holder.itemView.apply {
            activeFontSamples.remove(findViewById(R.id.tvFontSample))
        }
    }

    override fun getItemCount(): Int {
        return fonts.size
    }
}