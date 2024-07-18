package com.teamkkumul.feature.newgroup.addnewgroup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.core.ui.util.context.dialogFragmentResize
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogInvitationCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DialogInvitationCodeFragment :
    BindingDialogFragment<FragmentDialogInvitationCodeBinding>(R.layout.fragment_dialog_invitation_code) {

    private val sourceFragment: String by lazy {
        requireArguments().getString(SOURCE_FRAGMENT) ?: ""
    }

    private val viewModel by activityViewModels<AddNewGroupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun initView() {
        getInvitationCode()
        setupDialogBtn()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun getInvitationCode() {
        viewModel.invitationCode.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { invitationCode ->
                binding.tvInvitationCode.text = invitationCode
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupDialogBtn() {
        binding.ivBtnCopy.setOnClickListener {
            copyToClipboard(binding.tvInvitationCode.text.toString())
            handleNavigation()
            dismiss()
        }
        binding.ivBtnInviteLater.setOnClickListener {
            handleNavigation()
            dismiss()
        }
    }

    private fun handleNavigation() {
        when (sourceFragment) {
            "AddNewGroupFragment" -> {
                findNavController().navigate(R.id.action_fragment_add_new_group_to_fragment_add_my_group_complete)
            }
            "MyGroupDetailFragment" -> {}
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("invitation_code", text)
        clipboard.setPrimaryClip(clip)
    }

    companion object {
        private const val SOURCE_FRAGMENT = "sourceFragment"

        @JvmStatic
        fun newInstance(sourceFragment: String) = DialogInvitationCodeFragment().apply {
            arguments = Bundle().apply {
                putString(SOURCE_FRAGMENT, sourceFragment)
            }
        }
    }
}
