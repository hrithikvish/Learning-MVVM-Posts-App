package com.hrithikvish.mvvmpostsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hrithikvish.mvvmpostsapp.R
import com.hrithikvish.mvvmpostsapp.databinding.FragmentLoginBinding
import com.hrithikvish.mvvmpostsapp.model.userModel.UserRequest
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.UserIdManager
import com.hrithikvish.mvvmpostsapp.util.ValidationUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var userIdManager: UserIdManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val userRequest = getUserRequest()
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.loginUser(UserRequest(userRequest.username, userRequest.password))
            } else {
                binding.txtError.text = validationResult.second
            }
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObservers()
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    userIdManager.saveUserId(it.data?.id!!)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                    binding.progressBar.isVisible = false
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun getUserRequest(): UserRequest {
        val username = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(username, password)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return ValidationUtil.validateCredential(userRequest.username!!, userRequest.password!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}