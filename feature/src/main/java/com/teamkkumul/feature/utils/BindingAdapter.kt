package com.teamkkumul.feature.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.teamkkumul.core.ui.util.context.colorOf
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

@BindingAdapter("setStateBackgroundAndTextColor")
fun TextView.setStateBackgroundAndTextColor(state: String) {
    when (state) {
        "준비중" -> {
            setBackgroundResource(R.drawable.shape_stroke_green_fill_white_20)
            setTextColor(context.colorOf(R.color.main_color))
            this.text = state
        }

        "이동중" -> {
            setBackgroundResource(R.drawable.shape_fill_green2_20)
            setTextColor(context.colorOf(R.color.main_color))
            this.text = state
        }

        "도착" -> {
            setBackgroundResource(R.drawable.shape_fill_main_green_20)
            setTextColor(context.colorOf(R.color.white0))
            this.text = state
        }

        "꾸물중" -> {
            setBackgroundResource(R.drawable.shape_stroke_gray3_fill_white_20)
            setTextColor(context.colorOf(R.color.gray3))
            this.text = state
        }
    }
}
