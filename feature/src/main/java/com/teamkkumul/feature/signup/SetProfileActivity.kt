package com.teamkkumul.feature.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivitySetProfileBinding
import com.teamkkumul.feature.signup.SetNameActivity.Companion.INPUT_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetProfileActivity :
    BindingActivity<ActivitySetProfileBinding>(R.layout.activity_set_profile) {

    private val setProfileViewModel: SetProfileViewModel by viewModels()
    private var inputName: String? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                selectedImageUri?.let { uri ->
                    val mimeType = contentResolver.getType(uri)
                    if (mimeType == "image/heif" || mimeType == "image/heic") {
                        toast("HEIF 형식의 이미지는 지원되지 않습니다. 다른 형식의 이미지를 선택해 주세요.")
                    } else {
                        with(binding) {
                            ivBtnSetProfile.load(selectedImageUri)
                            btnOkay.isEnabled = true
                        }
                        setProfileViewModel.setPhotoUri(selectedImageUri.toString())
                    }
                }
            }
        }

    override fun initView() {
        inputName = intent.getStringExtra(INPUT_NAME)
        with(binding) {
            ivBtnSetProfile.setOnClickListener {
                openGallery()
            }
            btnOkay.setOnClickListener {
                setProfileViewModel.photoUri.value?.let { uriString ->
                    setProfileViewModel.updateImage(inputName ?: "", uriString)
                }
            }
            tvBtnNotNow.setOnClickListener {
                inputName?.let { navigateToWelcome(it) }
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            setProfileViewModel.updateImageState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        toast("Image updated successfully")
                        inputName?.let { navigateToWelcome(it) }
                    }
                    is UiState.Failure -> {
                        toast("Failed to update image: ${state.errorMessage}")
                    }
                    else -> Unit
                }
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
