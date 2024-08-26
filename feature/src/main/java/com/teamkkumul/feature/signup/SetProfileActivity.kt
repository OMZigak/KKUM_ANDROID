package com.teamkkumul.feature.signup

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.showPermissionAppSettingsDialog
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivitySetProfileBinding
import com.teamkkumul.feature.signup.SetNameActivity.Companion.INPUT_NAME
import com.teamkkumul.feature.utils.KeyStorage.UPDATED_PHOTO_URI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SetProfileActivity :
    BindingActivity<ActivitySetProfileBinding>(R.layout.activity_set_profile) {

    private val setProfileViewModel: SetProfileViewModel by viewModels()
    private var inputName: String? = null

//    private val requestPermissions =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                lifecycleScope.launch {
//                    try {
//                        selectImage()
//                    } catch (e: Exception) {
//                        toast("error")
//                    }
//                }
//            } else {
//                Timber.tag("permission").d("권한 거부")
//                showPermissionAppSettingsDialog()
//            }
//        }

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

//    private val getPhotoPickerLauncher =
//        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { selectedImageUri: Uri? ->
//            selectedImageUri?.let {
//                binding.ivBtnSetProfile.load(it)
//            }
//        }
//
//    private fun selectImage() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) { // tiramisu 미만은 갤러리에서 이미지 선택
//            val getPictureIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
//            selectImageLauncher.launch(getPictureIntent)
//        } else { // tiramisu 이상은 포토피커 사용
//            getPhotoPickerLauncher.launch(
//                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
//            )
//        }
//    }

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
                        putExtra(UPDATED_PHOTO_URI, setProfileViewModel.photoUri)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }

                is UiState.Failure -> {}
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

//    private fun getGalleryPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            selectImage()
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestPermissions.launch(READ_MEDIA_IMAGES)
//        } else {
//            requestPermissions.launch(READ_EXTERNAL_STORAGE)
//        }
//    }

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
