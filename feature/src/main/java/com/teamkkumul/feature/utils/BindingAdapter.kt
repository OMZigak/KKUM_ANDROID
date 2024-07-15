package com.teamkkumul.feature.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.teamkkumul.feature.R

/*@BindingAdapter("imageUrl")
fun loadImage(
    view: ImageView,
    url: String?,
) {
    if (url?.isBlank() == true) {
        view.load(R.drawable.ic_sign_up_profile_person)
    } else {
        view.load(url) {
            placeholder(R.drawable.ic_sign_up_profile_person) // 기본 이미지 설정
        }
    }
}*/

@BindingAdapter("setCircleImage")
fun ImageView.setCircleImage(img: String?) {
    load(img) {
        transformations(RoundedCornersTransformation(1000f))
    }
}

@BindingAdapter("setEmptyImageUrl")
fun ImageView.setEmptyImageUrl(img: String?) {
    if (img.isNullOrEmpty()) {
        load(R.drawable.ic_profile_basic_44)
    } else {
        load(img)
    }
}
