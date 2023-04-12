package com.embrace.quizapp.utils


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.embrace.quizapp.R

object ImageLoader {

    @JvmStatic
    @JvmOverloads
    fun loadImage(
        url: String,
        imageView: ImageView,
        placeholder: Int = R.drawable.embrace_it,
        error: Int = R.drawable.embrace_it
    ) {
        if (url.isNotEmpty()) {
            Glide.with(imageView.context).load(url).error(error).placeholder(placeholder)
                .into(imageView)
        } else {
            Glide.with(imageView.context).load(placeholder).error(error).placeholder(placeholder)
                .into(imageView)
        }
    }
}