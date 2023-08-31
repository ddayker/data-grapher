package com.dayker.datagrapher.presentation.ui.passwordreset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.FragmentPasswordResetBinding
import com.dayker.datagrapher.presentation.ui.passwordreset.viewmodel.PasswordResetViewModel
import com.dayker.datagrapher.utils.UIUtilities.showSnackBar
import com.dayker.datagrapher.utils.Utilities.validEmailHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetFragment : Fragment() {

    private var binding: FragmentPasswordResetBinding? = null
    private val viewModel: PasswordResetViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.appBar?.toolBar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.resetResult.observe(requireActivity()) { result ->
            if (result.isSuccess) {
                binding?.root?.let { it ->
                    showSnackBar(
                        message = getString(R.string.message_sent),
                        view = it
                    )
                }
            } else {
                showSnackBar(
                    message = result.exceptionOrNull()?.message.toString(),
                    view = binding?.root!!
                )
            }
        }

        binding?.btnReset?.setOnClickListener {
            val email = binding?.editTextEmail?.text.toString()
            if (isEmailValid(email)) {
                viewModel.resetUserPassword(email = email)
            }
        }

        binding?.editTextEmail?.setOnFocusChangeListener { _, focused ->
            binding?.layoutEmail?.helperText = null
        }
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

}
