package com.teamkkumul.feature.signup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.showPermissionAppSettingsDialog
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

    private lateinit var getGalleryLauncher: ActivityResultLauncher<String>
    private lateinit var getPhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> openGallery()
                false -> handlePermissionDenied()
            }
        }

    private fun handlePermissionDenied() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
            showPermissionAppSettingsDialog()
        }
    }

    private val sourceFragment: String by lazy {
        intent.getStringExtra(SOURCE_FRAGMENT) ?: ""
    }

    private val profileImageUrl: String? by lazy {
        intent.getStringExtra(PROFILE_IMAGE_URL)
    }

    override fun initView() {
        inputName = intent.getStringExtra(INPUT_NAME)
        loadProfileImage()
        initNotNowBtnText()
        initObserveImageState()
        initSetProfileBtnClick()
        initOkayBtnClick()
        initNotNowBtnClick()
        initPhotoPickerLauncher()
        initGalleryLauncher()
    }

    private fun loadProfileImage() {
        profileImageUrl?.let {
            binding.ivBtnSetProfile.load(it)
        }
    }

    private fun initNotNowBtnText() {
        binding.tvBtnNotNow.text = when (sourceFragment) {
            MY_PAGE_FRAGMENT -> getString(R.string.set_profile_to_basic_image)
            else -> getString(R.string.not_now)
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
            getGalleryPermission()
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            getGalleryLauncher.launch("image/*")
        } else {
            getPhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        }
    }

    private fun getGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            openGallery()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requestPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun initPhotoPickerLauncher() {
        getPhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uploadSelectedProfileImage(it)
            }
    }

    private fun initGalleryLauncher() {
        getGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                uploadSelectedProfileImage(it)
            }
    }

    private fun uploadSelectedProfileImage(imageUri: Uri?) {
        imageUri?.let {
            selectedImageUri = it.toString()
            binding.ivBtnSetProfile.load(it)
            setProfileViewModel.setPhotoUri(selectedImageUri)
            binding.btnOkay.isEnabled = true
        }
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
