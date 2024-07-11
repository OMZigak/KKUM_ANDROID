package com.teamkkumul.feature.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivitySetProfileBinding
import com.teamkkumul.feature.signup.SetNameActivity.Companion.INPUT_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetProfileActivity : BindingActivity<ActivitySetProfileBinding>(R.layout.activity_set_profile) {
    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                selectedImageUri?.let {
                    with(binding) {
                        ivBtnSetProfile.load(selectedImageUri)
                        btnOkay.isEnabled = true
                    }
                }
            }
        }

    override fun initView() {
        val inputName = intent.getStringExtra("inputName")
        with(binding) {
            ivBtnSetProfile.setOnClickListener {
                openGallery()
            }
            btnOkay.setOnClickListener {
                inputName?.let { navigateToWelcome(it) }
            }
            tvBtnNotNow.setOnClickListener {
                inputName?.let { navigateToWelcome(it) }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        selectImageLauncher.launch(intent)
    }

    private fun navigateToWelcome(inputName: String) {
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            putExtra(INPUT_NAME, inputName)
        }
        startActivity(intent)
    }
}
