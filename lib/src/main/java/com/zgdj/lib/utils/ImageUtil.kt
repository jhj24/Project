package com.zgdj.lib.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.zgdj.lib.R

object ImageUtil {

    val options = RequestOptions()
            .placeholder(R.mipmap.ic_placeholder)

    val roundOptions = RequestOptions()
            .transform(RoundedCorners(18))
            .placeholder(R.mipmap.ic_placeholder)

    val circleOptions = RequestOptions()
            .placeholder(R.mipmap.ic_placeholder)
            .transform(CircleCrop())

    val noCacheOptions = RequestOptions()
            .placeholder(R.mipmap.ic_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

    val avatarOptions = RequestOptions()
            .transform(CircleCrop())
            .placeholder(R.mipmap.ic_avatar)


    fun show(context: Context, imgPath: Any, imageView: ImageView, options: RequestOptions) {
        Glide.with(context)
                .asBitmap()
                .load(imgPath)
                .apply(options)
                .into(imageView)
    }
}