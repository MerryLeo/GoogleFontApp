package com.example.googlefontapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googlefontapp.databinding.ActivityGoogleFontBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class GoogleFontActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGoogleFontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val endpoint = getString(R.string.endpoint, BuildConfig.API_KEY)

        lifecycleScope.launch(Dispatchers.Default) {
            try {
                // Get Fonts
                val apiContent = URL(endpoint).readText()
                val fonts = Gson().fromJson(apiContent, FontWrapper::class.java).items

                // Bind FontData Adapter
                val fontAdapter = FontAdapter(fonts, this@GoogleFontActivity)
                this@GoogleFontActivity.runOnUiThread {
                    binding.rvGoogleFont.adapter = fontAdapter
                    binding.rvGoogleFont.layoutManager =
                        LinearLayoutManager(this@GoogleFontActivity)
                }

            } catch (e: Exception) {
                // Disable RecyclerView and display an AlertDialog
                this@GoogleFontActivity.runOnUiThread {
                    binding.rvGoogleFont.isVisible = false
                    AlertDialog.Builder(this@GoogleFontActivity).apply {
                        setTitle(R.string.error_dialog_title)
                        setMessage(getString(R.string.error_json))
                        setNegativeButton(R.string.bt_dismiss) { _: DialogInterface, _: Int ->
                            val intent = Intent(this@GoogleFontActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        setCancelable(false)
                    }.show()
                }
            }
        }
    }
}