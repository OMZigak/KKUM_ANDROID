package com.teamkkumul.feature.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import coil.load
import com.kakao.sdk.common.KakaoSdk.type
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentSetProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetProfileFragment : BindingFragment<FragmentSetProfileBinding>(R.layout.fragment_set_profile) {
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
        with(binding) {
            ivBtnSetProfile.setOnClickListener {
                openGallery()
            }
            btnOkay.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_set_profile_to_fragment_welcome)
            }
            tvBtnNotNow.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_set_profile_to_fragment_welcome)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        selectImageLauncher.launch(intent)
    }
}
