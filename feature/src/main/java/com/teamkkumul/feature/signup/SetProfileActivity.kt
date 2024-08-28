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
import com.teamkkumul.feature.utils.KeyStorage.MY_PAGE_FRAGMENT
import com.teamkkumul.feature.utils.KeyStorage.SET_NAME_ACTIVITY
import com.teamkkumul.feature.utils.KeyStorage.SOURCE_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SetProfileActivity :
    BindingActivity<ActivitySetProfileBinding>(R.layout.activity_set_profile) {

    private val setProfileViewModel: SetProfileViewModel by viewModels()
    private var inputName: String? = null
    private var selectedImageUri: String? = null

    private val sourceFragment: String by lazy {
        intent.getStringExtra(SOURCE_FRAGMENT) ?: ""
    }

    private val profileImageUrl: String? by lazy {
        intent.getStringExtra(PROFILE_IMAGE_URL)
    }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                selectedImageUri?.let { uri ->
                    this.selectedImageUri = uri.toString()
                    with(binding) {
                        ivBtnSetProfile.load(uri)
                        btnOkay.isEnabled = true
                    }
                    setProfileViewModel.setPhotoUri(uri.toString())
                }
            }
        }

    override fun initView() {
        inputName = intent.getStringExtra(INPUT_NAME)
        loadProfileImage()
        initObserveImageState()
        initSetProfileBtnClick()
        initOkayBtnClick()
        initNotNowBtnClick()
    }

    private fun loadProfileImage() {
        profileImageUrl?.let {
            binding.ivBtnSetProfile.load(it)
        }
    }

    private fun initObserveImageState() {
        setProfileViewModel.updateImageState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    when (sourceFragment) {
                        SET_NAME_ACTIVITY -> {
                            inputName?.let { navigateToWelcome(it) }
                        }
                        MY_PAGE_FRAGMENT -> {
                            val intent = Intent().apply {
                                putExtra(PROFILE_IMAGE_URL, selectedImageUri)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }
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
            if (sourceFragment == MY_PAGE_FRAGMENT) {
                finish()
            } else {
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

    companion object {
        const val PROFILE_IMAGE_URL = "profile_image_url"
    }
}
