package com.teamkkumul.feature.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivitySetProfileBinding
import com.teamkkumul.feature.signup.SetNameActivity.Companion.INPUT_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
                selectedImageUri?.let {
                    with(binding) {
                        ivBtnSetProfile.load(selectedImageUri)
                        btnOkay.isEnabled = true
                    }
                    setProfileViewModel.setPhotoUri(selectedImageUri.toString())
                }
            }
        }

    override fun initView() {
        inputName = intent.getStringExtra(INPUT_NAME)
        initObserveImageState()
        initSetProfileBtnClick()
        initOkayBtnClick()
        initNotNowBtnClick()
    }

    private fun initObserveImageState() {
        setProfileViewModel.updateImageState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    inputName?.let { navigateToWelcome(it) }
                    val resultIntent = Intent().apply {
                        putExtra("updated_photo_uri", setProfileViewModel.photoUri)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish() // set profile activity 종료
                }

                is UiState.Failure -> {}
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initSetProfileBtnClick() {
        binding.ivBtnSetProfile.setOnClickListener {
            openGallery()
        }
    }

    private fun initOkayBtnClick() {
        binding.btnOkay.setOnClickListener {
            setProfileViewModel.photoUri?.let { uriString ->
                setProfileViewModel.updateImage(uriString)
            }
        }
    }

    private fun initNotNowBtnClick() {
        binding.tvBtnNotNow.setOnClickListener {
            inputName?.let { navigateToWelcome(it) }
            finish()
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
