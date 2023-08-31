package com.dayker.datagrapher.presentation.ui.authorization

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.FragmentAuthorizationBinding
import com.dayker.datagrapher.presentation.ui.authorization.viewmodel.AuthorizationViewModel
import com.dayker.datagrapher.utils.Constants
import com.dayker.datagrapher.utils.UIUtilities.showSnackBar
import com.dayker.datagrapher.utils.Utilities.validEmailHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private var binding: FragmentAuthorizationBinding? = null
    private val viewModel: AuthorizationViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        viewModel.authorizationResult.observe(requireActivity()) { result ->
            if (result.isSuccess) {
                findNavController().navigate(R.id.action_authorizationFragment_to_profileFragment)
            } else {
                showSnackBar(
                    message = result.exceptionOrNull()?.message.toString(),
                    view = binding?.root!!
                )
            }
        }

        binding?.appBar?.toolBar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding?.tvSingUp?.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }
        binding?.tvForgotPassword?.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_passwordResetFragment)
        }
        binding?.btnLogIn?.setOnClickListener {
            val email = binding?.editTextEmail?.text.toString()
            if (isEmailValid(email)) {
                val password = binding?.editTextPassword?.text.toString()
                viewModel.authorizeUserByEmail(email = email, password = password)
            }
        }

        binding?.editTextEmail?.setOnFocusChangeListener { _, focused ->
            binding?.layoutEmail?.helperText = null
        }

        binding?.googleSignIn?.setOnClickListener {
            viewModel.startGoogleSignIn(
                webClientId = getString(R.string.web_client_id),
                onSuccess = {
                    startIntentSenderForResult(
                        it, Constants.REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                },
                onFailureAction = {
                    showAuthenticationFailedSnackBar()
                })
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleActivityResult(
            requestCode,
            resultCode,
            data,
            onSuccessAction = { findNavController().navigate(R.id.action_authorizationFragment_to_profileFragment) },
            onFailureAction = { showAuthenticationFailedSnackBar() }
        )
    }

    private fun isEmailValid(email: String): Boolean {
        with(binding) {
            if (this != null) {
                layoutEmail.helperText = validEmailHelper(email = email, context = requireContext())
                return layoutEmail.helperText == null
            }
        }
        return false
    }

    private fun showAuthenticationFailedSnackBar() {
        binding?.root?.let {
            showSnackBar(
                message = getString(R.string.authentication_failed),
                view = it
            )
        }
    }
}