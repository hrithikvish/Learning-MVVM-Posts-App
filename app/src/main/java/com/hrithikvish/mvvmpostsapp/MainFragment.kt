package com.hrithikvish.mvvmpostsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.hrithikvish.mvvmpostsapp.databinding.FragmentMainBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.PostUpdateRequest
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.Operation
import com.hrithikvish.mvvmpostsapp.util.UserIdManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val postViewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var userIdManager: UserIdManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testUpdatePost()
        bindObservers()
    }

    private fun testUpdatePost() {
        postViewModel.updatePost(PostUpdateRequest(userIdManager.getUserId() , "Hrithik title", null, null), 11)
    }

    private fun bindObservers() {
        postViewModel.addUpdateDeletePostResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    val operation = it.operation
                    val successMessage = when(operation) {
                        Operation.GET_POSTS -> "Post fetched successfully"
                        Operation.ADD_POST -> "Post added successfully"
                        Operation.UPDATE_POST -> "Post updated successfully"
                        Operation.DELETE_POST -> "Post deleted successfully"
                        else -> "Operation completed successfully"
                    }
                    Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}