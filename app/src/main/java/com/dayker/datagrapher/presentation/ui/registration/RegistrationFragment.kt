package com.dayker.datagrapher.presentation.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.FragmentRegistrationBinding
import com.dayker.datagrapher.presentation.ui.registration.viewmodel.RegistrationViewModel
import com.dayker.datagrapher.utils.Constants.REQ_ONE_TAP
import com.dayker.datagrapher.utils.UIUtilities.showSnackBar
import com.dayker.datagrapher.utils.Utilities.validEmailHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null
    private val viewModel: RegistrationViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        viewModel.registrationResult.observe(requireActivity()) { result ->
            if (result.isSuccess) {
                findNavController().navigate(R.id.action_registrationFragment_to_profileFragment)
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

        binding?.btnSignUp?.setOnClickListener {
            val email = binding?.editTextEmail?.text.toString()
            if (isRegistrationValid(email = email)) {
                val password = binding?.editTextPassword?.text.toString()
                viewModel.registerUserByEmail(email = email, password = password)
            }
        }

        binding?.editTextEmail?.setOnFocusChangeListener { _, focused ->
            binding?.layoutEmail?.helperText = null
        }

        binding?.editTextPassword?.setOnFocusChangeListener { _, focused ->
            binding?.layoutPassword?.helperText = null
        }

        binding?.editTextPasswordConfirmation?.setOnFocusChangeListener { _, focused ->
            binding?.layoutPasswordConfirmation?.helperText = null
        }

        binding?.googleSignIn?.setOnClickListener {
            viewModel.startGoogleSignIn(
                webClientId = getString(R.string.web_client_id),
                onSuccess = {
                    startIntentSenderForResult(
                        it, REQ_ONE_TAP,
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
            onSuccessAction = { findNavController().navigate(R.id.action_registrationFragment_to_profileFragment) },
            onFailureAction = { showAuthenticationFailedSnackBar() }
        )
    }


    private fun showAuthenticationFailedSnackBar() {
        binding?.root?.let {
            showSnackBar(
                message = getString(R.string.authentication_failed),
                view = it
            )
        }
    }

    private fun isRegistrationValid(email: String): Boolean {
        with(binding) {
            if (this != null) {
                val password = editTextPassword.text.toString().trim()
                val confirmation = editTextPasswordConfirmation.text.toString().trim()
                binding?.layoutPassword?.helperText = viewModel.validPasswordByLength(
                    password = password,
                    errorMessage = getString(R.string.invalid_password)
                )
                layoutEmail.helperText =
                    validEmailHelper(email = email, context = requireContext())
                layoutPasswordConfirmation.helperText = viewModel.validPasswordConfirmation(
                    password = password,
                    confirmation = confirmation,
                    errorMessage = getString(R.string.invalid_confirmation)
                )
                return layoutEmail.helperText == null && layoutPassword.helperText == null && layoutPasswordConfirmation.helperText == null
            }
        }
        return false
    }

}