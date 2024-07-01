package com.hrithikvish.mvvmpostsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hrithikvish.mvvmpostsapp.databinding.FragmentAddPostBottomSheetDialogBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.util.UserIdManager
import com.hrithikvish.mvvmpostsapp.viewmodel.PostViewModel
import javax.inject.Inject

class AddPostBottomSheetDialogFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentAddPostBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel

    @Inject
    lateinit var userIdManager: UserIdManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPostBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTransparentBackground()
        setWidthHeight()
        setFullHeight()
        initViewModel()
        setClickListeners()
    }

    private fun initViewModel() {
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
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