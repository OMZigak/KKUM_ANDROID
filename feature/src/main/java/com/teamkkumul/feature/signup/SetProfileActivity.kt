package com.teamkkumul.feature.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import coil.load
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivitySetProfileBinding
import com.teamkkumul.feature.signup.SetNameActivity.Companion.INPUT_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SetProfileActivity :
    BindingActivity<ActivitySetProfileBinding>(R.layout.activity_set_profile) {

    private val setProfileViewModel: SetProfileViewModel by viewModels()

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
                selectedImageUri?.let { uri ->
                    val file = uriToFile(uri)
                    file?.let { setProfileViewModel.updateImage(it) }
                }
            }
        }

    override fun initView() {
        val inputName = intent.getStringExtra(INPUT_NAME)
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
        //observeViewModel()
    }

//    private fun observeViewModel() {
//        setProfileViewModel.updateImageResult.observe(this) { result ->
//            result.onSuccess {
//                toast("Image updated successfully")
//            }.onFailure {
//                toast("Failed to update image")
//            }
//        }
//    }

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

    private fun uriToFile(uri: Uri): File? {
        val contentResolver = applicationContext.contentResolver
        val tempFile = File.createTempFile("profile_image", ".jpg", cacheDir)
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream = tempFile.outputStream()
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return tempFile
    }
}
