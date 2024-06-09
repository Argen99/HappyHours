package com.example.presentation.ui.fragments.profile

import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.core.Constants
import com.example.core_ui.base.BaseFragment
import com.example.core_ui.extensions.loadImageWithGlide
import com.example.core_ui.extensions.showShortToast
import com.example.domain.models.User
import com.example.presentation.R
import com.example.presentation.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)
    override val viewModel by activityViewModel<ProfileViewModel>()

    override fun setupListeners() {
        binding.containerProfile.setOnClickListener {
            navigateToEditProfile()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog(){
        MaterialAlertDialogBuilder(requireContext(),
            androidx.appcompat.R.style.AlertDialog_AppCompat)
            .setMessage("Do you really want to leave?")
            .setTitle("Exit")
            .setPositiveButton("Logout") { dialog, which ->
                dialog.dismiss()
                viewModel.logout()
            }
            .setNegativeButton("Cancel"){ dialog, which ->
                dialog.dismiss()
            }
            .show()
    }


    override fun launchObservers() {
        viewModel.userState.spectateUiState(
            success = { user ->
                setUserData(user)
            },
            error = {
                showShortToast(it)
            }
        )

        viewModel.userLogoutState.spectateUiState(
            success = {
                logout()
                viewModel.resetUserLogoutState()
            },
            error = {
                showShortToast(it)
            }
        )
    }

    private fun setUserData(user: User): Unit = with(binding) {
        user.avatar?.let { ivUserAvatar.loadImageWithGlide(it) }
        tvUserName.text = user.name
        tvUserEmail.text = user.email
    }

    private fun navigateToEditProfile() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
    }

    private fun logout() {
        val request = NavDeepLinkRequest.Builder
            .fromUri(Constants.Deeplink.DEEPLINK_NAV_TO_AUTH_MODULE.toUri())
            .build()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph_main, false)
            .build()
        findNavController().navigate(request, navOptions)
    }
}