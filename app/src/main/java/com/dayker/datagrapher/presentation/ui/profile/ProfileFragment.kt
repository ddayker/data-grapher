package com.dayker.datagrapher.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        binding?.tv?.text = currentUser?.email

        binding?.btnSignOut?.setOnClickListener {
            Firebase.auth.signOut()
            println("${auth.currentUser}")
            findNavController().navigate(R.id.action_profileFragment_to_authorizationFragment)
        }

        binding?.btnHome?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }

}