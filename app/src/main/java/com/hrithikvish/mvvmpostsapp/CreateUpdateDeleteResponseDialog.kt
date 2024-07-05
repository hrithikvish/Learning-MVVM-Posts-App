package com.hrithikvish.mvvmpostsapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import com.hrithikvish.mvvmpostsapp.databinding.DialogCreateUpdateDeleteResponseBinding
import com.hrithikvish.mvvmpostsapp.model.postModel.Post

class CreateUpdateDeleteResponseDialog(
    context: Context,
    private val responseHeadText: String,
    private val post: Post?
): Dialog(context) {

    private var _binding: DialogCreateUpdateDeleteResponseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DialogCreateUpdateDeleteResponseBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setInitialData()
        populateViews()
        setClickListeners()

    }

    private fun setInitialData() {
        binding.showRawResponseIv.tag = R.drawable.ic_code
    }

    private fun setClickListeners() {
        binding.showRawResponseIv.setOnClickListener {
            if (binding.showRawResponseIv.tag == R.drawable.ic_code) {
                togglePostViewMode(true)
                setDrawableAndTag(binding.showRawResponseIv, R.drawable.ic_code_off)
            } else {
                togglePostViewMode(false)
                setDrawableAndTag(binding.showRawResponseIv, R.drawable.ic_code)
            }
        }
    }

    private fun setDrawableAndTag(imageView: ImageView, drawableRes: Int) {
        imageView.setImageResource(drawableRes)
        imageView.tag = drawableRes
    }

    private fun togglePostViewMode(viewRaw: Boolean) {
        binding.apply {
            postTitle.visibility = if(viewRaw) View.GONE else View.VISIBLE
            postBody.visibility = if(viewRaw) View.GONE else View.VISIBLE
            rawResponseTv.visibility = if(viewRaw) View.VISIBLE else View.GONE
        }
        toggleToolTip(viewRaw)
    }

    private fun toggleToolTip(setRawToolTip: Boolean) {
        binding.showRawResponseIv.tooltipText = if(setRawToolTip) "View normal" else "View raw"
    }

    private fun populateViews() {
        binding.responseTvHead.text = responseHeadText
        binding.postTitle.text = post?.title
        binding.postBody.text = post?.body
        binding.rawResponseTv.text = post?.toString()
    }

}