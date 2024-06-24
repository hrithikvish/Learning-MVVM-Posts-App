package com.hrithikvish.mvvmpostsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hrithikvish.mvvmpostsapp.databinding.PostItemBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.Post
import com.hrithikvish.mvvmpostsapp.model.postModel.PostResponse

class PostAdapter(
    private val onNoteClicked: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val postsList = mutableListOf<Post?>()

    fun submitPostResponse(postResponse: PostResponse) {
        postsList.clear()
        postsList.addAll(postResponse.posts ?: emptyList())
        notifyDataSetChanged()
    }

    class PostViewHolder(
        val binding: PostItemBinding
    ): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList[position]
        post?.let {
            holder.binding.postTitle.text = it.title
            holder.binding.postBody.text = it.body
        }
        holder.binding.root.setOnClickListener {
            onNoteClicked(post!!)
        }
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

}
