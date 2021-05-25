package ru.netology.view

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.netology.R

fun ImageView.load(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
    Glide.with(this)
        .load(url)
        .timeout(1_000)
        .placeholder(R.drawable.ic_avatar_loading_foreground)
        .error(R.drawable.ic_avatar_error_foreground)
        .transform(*transforms)
        .into(this)

fun ImageView.loadCircleCrop(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
    load(url, CircleCrop(), *transforms)
