package com.example.autoviewpager

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

@BindingAdapter("app:image_blurred")
fun ImageView.setBlurImage(src : String?) {

    Glide.with(this).load(src)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
        .into(this)
}

@BindingAdapter("app:image_blurred")
fun ImageView.setBlurImage(@DrawableRes image : Int) {

    Glide.with(this).load(image)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
        .into(this)
}

@BindingAdapter("android:src")
fun ImageView.setImageResource( @DrawableRes image : Int){

    this.setImageResource(image)
}