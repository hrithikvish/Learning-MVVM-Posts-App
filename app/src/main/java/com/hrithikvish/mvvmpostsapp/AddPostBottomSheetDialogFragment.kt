package com.hrithikvish.mvvmpostsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hrithikvish.mvvmpostsapp.databinding.FragmentAddPostBottomSheetDialogBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.UserIdManager
import com.hrithikvish.mvvmpostsapp.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class AddPostBottomSheetDialogFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentAddPostBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private val postViewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var userIdManager: UserIdManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTransparentBackground()
        setWidthHeight()
        setFullHeight()
        setClickListeners()
        bindObservers()
    }

    private fun bindObservers() {
        postViewModel.addPostResponseLiveData.observe(viewLifecycleOwner) { networkResult ->
            when(networkResult) {
                is NetworkResult.Success -> {
                    binding.createPostProgressBar.isVisible = false
                    CreateUpdateDeleteResponseDialog(
                        requireContext(),
                        "Created Post:",
                        networkResult.data
                    ).show()
                }
                is NetworkResult.Error -> {
                    binding.createPostProgressBar.isVisible = false
                    binding.responseTvHead.isVisible = true
                    binding.responseTvHead.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_error))
                    binding.responseTvHead.text = "Something went wrong, try again."
                    Toast.makeText(requireContext(), networkResult.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.createPostProgressBar.isVisible = true
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnSubmit.setOnClickListener {
            val postTitle = binding.txtTitle.text.toString()
            val postBody = binding.txtBody.text.toString()
            postViewModel.addPost(PostRequest(userIdManager.getUserId(), postTitle, postBody))
        }
    }

    private fun setFullHeight() {
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setWidthHeight() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun setTransparentBackground() {
        dialog?.apply {
            setOnShowListener {
                val bottomSheet = findViewById<View?>(R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}