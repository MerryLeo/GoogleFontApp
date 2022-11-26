package com.example.googlefontapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import com.example.googlefontapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var errorSnackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Error SnackBar
        errorSnackBar =
            Snackbar.make(binding.root, getString(R.string.error_placeholder), Snackbar.LENGTH_LONG)

        // Toggle Login Button on/off
        val inputListener = { _: Editable? ->
            binding.btLogin.isEnabled =
                binding.inputUsername.text.isNotEmpty() && binding.inputPassword.text.isNotEmpty()
        }
        binding.inputUsername.doAfterTextChanged(inputListener)
        binding.inputPassword.doAfterTextChanged(inputListener)

        // Login Button
        binding.btLogin.setOnClickListener {
            if (validateLoginInfo()) {
                val goToFontIntent = Intent(this, GoogleFontActivity::class.java)
                startActivity(goToFontIntent)
            }
        }

        // Register Button
        binding.btRegister.setOnClickListener {
            val goToRegisterIntent = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegisterIntent)
        }

        // Reset Password Button
        binding.btResetPassword.setOnClickListener {
            val goToResetPasswordIntent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(goToResetPasswordIntent)
        }

        // Display Intent Message from Reset Password
        val resetPasswordMessage = intent.getStringExtra(getString(R.string.login_msg_key))
        if (resetPasswordMessage?.isNotEmpty() == true) {
            errorSnackBar.setText(resetPasswordMessage).show()
        }

        // Use Keyboard Confirm to press Login
        binding.inputPassword.setOnEditorActionListener { _, actionId, event ->
            if (event?.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)
                binding.btLogin.performClick()
            false
        }
    }

    // Validate User Information with shared preferences
    private fun validateLoginInfo(): Boolean {
        // Get User Information
        val username = binding.inputUsername.text.toString()
        val password = binding.inputPassword.text.toString()

        // Validate Username and Password
        val sharedPref = getSharedPreferences(username, MODE_PRIVATE)
        if (sharedPref == null || sharedPref.getString("Password", String()) != password) {
            errorSnackBar.setText(R.string.invalid_password_username).show()
            return false
        }
        return true
    }
}