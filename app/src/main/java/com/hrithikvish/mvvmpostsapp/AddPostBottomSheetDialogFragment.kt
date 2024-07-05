package com.hrithikvish.mvvmpostsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.hrithikvish.mvvmpostsapp.databinding.FragmentAddPostBottomSheetDialogBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.util.ChipUtility.Companion.addChipIntoChipGroup
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
        setListeners()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnSubmit.setOnClickListener {
            val postTitle = binding.txtTitle.text.toString()
            val postBody = binding.txtBody.text.toString()
            val tagsList = getTagsListFromTagsChipGroup()
            postViewModel.addPost(
                PostRequest(
                    userIdManager.getUserId(),
                    postTitle,
                    postBody,
                    tagsList
                )
            )
        }

        // clicking send button on keyboard adds the editText content as a "Tag" chip
        val tagString = binding.tagsEdt.text
        binding.tagsEdt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (!tagString.isNullOrEmpty() && actionId == EditorInfo.IME_ACTION_SEND) {
                    val tag = tagString.toString().trim()
                    addChipIntoChipGroup(requireActivity(), tag, binding.postTagsChipGroup)
                    binding.tagsEdt.text.clear()
                    return true
                }
                return false
            }
        })

        // solves the problem when trying to scroll editText the bottomSheet scrolls instead
        binding.txtBody.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP ->
                    v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }

    private fun getTagsListFromTagsChipGroup() = binding.postTagsChipGroup.children.map {
        (it as Chip).text.toString()
    }.toList()

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