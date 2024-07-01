package com.hrithikvish.mvvmpostsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.hrithikvish.mvvmpostsapp.databinding.FragmentPostBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.Post
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.model.postModel.PostUpdateRequest
import com.hrithikvish.mvvmpostsapp.util.Constants
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.Operation
import com.hrithikvish.mvvmpostsapp.util.UserIdManager
import com.hrithikvish.mvvmpostsapp.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val postViewModel by viewModels<PostViewModel>()
    private var post: Post? = null

    @Inject
    lateinit var userIdManager: UserIdManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        setClickListeners()
        bindObservers()
    }

    private fun setClickListeners() {
        binding.btnDelete.setOnClickListener {
            post?.let {
                postViewModel.deletePost(it.id!!)
            }
        }
        binding.btnSubmit.setOnClickListener {
            val postTitle = binding.txtTitle.text.toString()
            val postBody = binding.txtBody.text.toString()
            if(post != null) {
                postViewModel.updatePost(PostUpdateRequest(userIdManager.getUserId(), postTitle, postBody, null), post?.id!!)
            }
            else {
                postViewModel.addPost(PostRequest(userIdManager.getUserId(), postTitle, postBody))
            }
        }
    }

    private fun bindObservers() {
        postViewModel.updateDeletePostResponseLiveData.observe(viewLifecycleOwner) { networkResult ->
            when(networkResult) {
                is NetworkResult.Success -> {
                    val successMessage: String
                    when (networkResult.operation) {
                        Operation.UPDATE_POST -> {
                            successMessage = "Post updated successfully"
                            binding.responseTvHead.text = "Update Response:"
                        }
                        Operation.DELETE_POST -> {
                            successMessage = "Post deleted successfully"
                            binding.responseTvHead.text = "Delete Response:"
                        }
                        else -> {
                            successMessage = "Operation completed successfully"
                            binding.responseTvHead.text = "Operation Response:"
                        }
                    }
                    Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                    binding.responseTv.text = networkResult.data.toString()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), networkResult.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun setInitialData() {
        val jsonPost = arguments?.getString(Constants.POST)
        if (jsonPost != null) {
            post = Gson().fromJson(jsonPost, Post::class.java)
            post?.let {
                binding.txtTitle.setText(it.title)
                binding.txtBody.setText(it.body)
            }
        } else {
            binding.addEditText.text = "Add Post"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}