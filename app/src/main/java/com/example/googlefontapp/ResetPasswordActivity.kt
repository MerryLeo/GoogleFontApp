package com.example.googlefontapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.doAfterTextChanged
import com.example.googlefontapp.databinding.ActivityResetPasswordBinding
import com.google.android.material.snackbar.Snackbar

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetPasswordBinding
    lateinit var errorSnackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SnackBar
        errorSnackBar =
            Snackbar.make(binding.root, getString(R.string.error_placeholder), Snackbar.LENGTH_LONG)

        // Cancel Button
        binding.btCancel.setOnClickListener {
            val backToLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(backToLoginIntent)
        }

        // Enable/Disable Confirm Button
        val oldPasswordText = binding.inputOldPassword.text
        val newPasswordText = binding.inputNewPassword.text
        val usernameText = binding.inputUsername.text

        val textListener = { _: Editable? ->
            binding.btConfirm.isEnabled =
                usernameText.isNotEmpty() &&
                        newPasswordText.isNotEmpty() &&
                        oldPasswordText.isNotEmpty()
        }

        binding.inputUsername.doAfterTextChanged(textListener)
        binding.inputOldPassword.doAfterTextChanged(textListener)
        binding.inputNewPassword.doAfterTextChanged(textListener)

        // Confirm Button
        binding.btConfirm.setOnClickListener {
            val username = binding.inputUsername.text.toString()
            val sharedPrefs = getSharedPreferences(username, MODE_PRIVATE)
            if (validateInfo(sharedPrefs)) {
                // Replace Old Password with new Password
                sharedPrefs.edit {
                    putString(
                        getString(R.string.password_key),
                        binding.inputNewPassword.text.toString()
                    )
                }
                // Back to login
                val backToLoginIntent = Intent(
                    this,
                    LoginActivity::class.java
                ).putExtra(getString(R.string.login_msg_key), getString(R.string.reset_successful))
                startActivity(backToLoginIntent)
            }
        }

        // Use Keyboard Confirm Button to press Confirm
        binding.inputOldPassword.setOnEditorActionListener { _, actionId, event ->
            if (event?.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)
                binding.btConfirm.performClick()
            false
        }
    }

    private fun validateInfo(sharedPrefs: SharedPreferences?): Boolean {
        val oldPassword = binding.inputOldPassword.text.toString()

        // Username and Old Password should match with sharedPreferences
        if (sharedPrefs == null || sharedPrefs.getString(
                getString(R.string.password_key),
                String()
            ) != oldPassword
        ) {
            errorSnackBar.setText(R.string.invalid_password_username).show()
            return false
        }

        // Old Password and New Password should not match
        val newPassword = binding.inputNewPassword.text.toString()
        if (oldPassword == newPassword) {
            errorSnackBar.setText(R.string.passwords_are_same).show()
            return false
        }
        return true
    }
}