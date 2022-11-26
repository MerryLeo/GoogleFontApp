package com.example.googlefontapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.doAfterTextChanged
import com.example.googlefontapp.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var errorSnackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorSnackBar =
            Snackbar.make(binding.root, getString(R.string.error_placeholder), Snackbar.LENGTH_LONG)

        // Get User Inputs
        val usernameInput = binding.inputUsername.text
        val passwordInput = binding.inputPassword.text
        val confirmPasswordInput = binding.inputConfirmPassword.text

        // Update Confirm Button
        val textListener = { _: Editable? ->
            binding.btConfirm.isEnabled =
                passwordInput.toString() == confirmPasswordInput.toString() &&
                        usernameInput.isNotEmpty() &&
                        passwordInput.isNotEmpty()
        }

        binding.inputUsername.doAfterTextChanged(textListener)
        binding.inputPassword.doAfterTextChanged(textListener)
        binding.inputConfirmPassword.doAfterTextChanged(textListener)

        // Confirm Button
        // Back to Login Activity
        binding.btConfirm.setOnClickListener {
            if (validateUserInfo()) {
                // Add Password to Shared Preferences
                val sharedPrefs =
                    getSharedPreferences(binding.inputUsername.text.toString(), MODE_PRIVATE)
                sharedPrefs.edit {
                    putString("Password", binding.inputPassword.text.toString())
                }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // Remove unnecessary Username from SharedPreferences
                getSharedPreferences(usernameInput.toString(), MODE_PRIVATE).edit {
                    clear()
                }
            }
        }

        // Use Keyboard Confirm to press Confirm
        binding.inputConfirmPassword.setOnEditorActionListener { _, actionId, event ->
            if (event?.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)
                binding.btConfirm.performClick()
            false
        }

        // Cancel Button
        // Back to Login Activity
        binding.btCancel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Validate Register Information
    private fun validateUserInfo(): Boolean {
        // Get Input Values
        val username = binding.inputUsername.text
        val password = binding.inputPassword.text
        val confirmPassword = binding.inputConfirmPassword.text

        // Get Username Limits
        val usernameMinLength = resources.getInteger(R.integer.minUsernameLength)
        val usernameMaxLength = resources.getInteger(R.integer.maxUsernameLength)

        // Username is too short
        if (username.length < usernameMinLength) {
            errorSnackBar.setText(
                getString(
                    R.string.field_too_short, "Username", usernameMinLength
                )
            ).show()
            return false
            // Username is too long
        } else if (username.length > usernameMaxLength) {
            errorSnackBar.setText(
                getString(
                    R.string.field_too_large, "Username", usernameMaxLength
                )
            ).show()
            return false
            // Username already exist
        } else if (getSharedPreferences(username.toString(), MODE_PRIVATE).contains("Password")) {
            errorSnackBar.setText(getString(R.string.value_already_taken, username)).show()
            return false
        }

        // Get Password Limits
        val passwordMinLength = resources.getInteger(R.integer.minPasswordLength)
        val passwordMaxLength = resources.getInteger(R.integer.maxPasswordLength)

        // Password is too short
        if (password.length < passwordMinLength) {
            errorSnackBar.setText(
                getString(
                    R.string.field_too_short, "Password", passwordMinLength
                )
            ).show()
            return false
            // Password is too long
        } else if (password.length > passwordMaxLength) {
            errorSnackBar.setText(
                getString(
                    R.string.field_too_large, "Password", passwordMaxLength
                )
            ).show()
            return false
        }

        // Password and Confirm Password must match
        if (password.toString() != confirmPassword.toString()) {
            errorSnackBar.setText(
                getString(
                    R.string.field_must_be_equal, "Password", "Confirm Password"
                )
            ).show()
            return false
        }
        return true
    }
}