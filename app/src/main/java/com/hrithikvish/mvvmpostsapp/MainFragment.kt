package com.hrithikvish.mvvmpostsapp

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.hrithikvish.mvvmpostsapp.adapter.PostAdapter
import com.hrithikvish.mvvmpostsapp.databinding.FragmentMainBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.Post
import com.hrithikvish.mvvmpostsapp.util.ConnectivityChecker
import com.hrithikvish.mvvmpostsapp.util.Constants
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.Operation
import com.hrithikvish.mvvmpostsapp.util.UserIdManager
import com.hrithikvish.mvvmpostsapp.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val postViewModel by viewModels<PostViewModel>()

    private lateinit var postAdapter: PostAdapter

    @Inject
    lateinit var userIdManager: UserIdManager

    @Inject
    lateinit var connectivityChecker: ConnectivityChecker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        postAdapter = PostAdapter(::onPostClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        setClickListeners()
        getPosts()
        setPostRecyclerView()

    }

    private fun setPostRecyclerView() {
        binding.postRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.postRv.adapter = postAdapter
    }

    private fun getPosts() {
        if (connectivityChecker.isConnected) {
            postViewModel.getPosts()
        } else {
            showAlertDialog(
                title = "Internet Connectivity",
                message = "You're not connected to internet, kindly connect to internet.",
                positiveButtonText = "OK"
            )
        }
    }

    private fun showAlertDialog(
        title: String? = null,
        message: String? = null,
        positiveButtonText: String? = null,
        negativeButtonText: String? = null,
        neutralButtonText: String? = null
    ) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setOnDismissListener { getPosts() }

        positiveButtonText?.let {
            dialog.setPositiveButton(it) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        }
        negativeButtonText?.let {
            dialog.setNegativeButton(it) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        }
        neutralButtonText?.let {
            dialog.setPositiveButton(it) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        }

        dialog.show()
    }

    private fun setClickListeners() {
        binding.addPostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_postFragment)
        }
    }

    private fun bindObservers() {
        postViewModel.postResponseResponseLiveData.observe(viewLifecycleOwner) { networkResult ->
            binding.progressBar.isVisible = false
            when (networkResult) {
                is NetworkResult.Success -> {
                    networkResult.data?.let {
                        postAdapter.submitPostResponse(networkResult.data)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), networkResult.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

        postViewModel.addUpdateDeletePostResponseLiveData.observe(viewLifecycleOwner) { networkResult ->
            binding.progressBar.isVisible = false
            when(networkResult) {
                is NetworkResult.Success -> {
                    val operation = networkResult.operation
                    val successMessage = when(operation) {
                        Operation.ADD_POST -> "Post added successfully"
                        Operation.UPDATE_POST -> "Post updated successfully"
                        Operation.DELETE_POST -> "Post deleted successfully"
                        else -> "Operation completed successfully"
                    }
                    Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), networkResult.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun onPostClicked(post: Post) {
        val bundle = Bundle()
        bundle.putString(Constants.POST, Gson().toJson(post))
        findNavController().navigate(R.id.action_mainFragment_to_postFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}