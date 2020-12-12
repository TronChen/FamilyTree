package com.tron.familytree

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tron.familytree.data.User

@BindingAdapter("mainImage")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Log.d("AnAn","imgUrl = ${it}")
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.album)
                    .error(R.drawable.circle))
            .into(imgView)
    }
}

//@BindingAdapter("userImage")
//fun userImage(imgView: ImageView, imgUrl: String? = "") {
//    imgUrl?.let {
//        Log.d("AnAn","imgUrl = ${it}")
//        val imgUri = imgUrl.toUri().buildUpon().scheme("gs").build()
//        Glide.with(imgView.context)
//            .load(imgUri)
//            .apply(
//                RequestOptions()
//                    .placeholder(R.drawable.album)
//                    .error(R.drawable.circle))
//            .into(imgView)
//    }
//}

@BindingAdapter("updateImage")
fun updateImage(imgView: ImageView, filePath: String?) {
    filePath?.let {
        Glide.with(imgView.context).load(it)
            .into(imgView)
    }
}

@BindingAdapter("AddMemberTitle")
fun bindAddTitle(text : TextView, user: User) {
    user?.let {
        if (user.name == "No child"){
            text.text =  "${user.fatherId} 與 ${user.motherId} 的 孩子"
        }
        if (user.name == "No mate"){
            text.text =  "${user.mateId} 的 配偶"
        }
        if (user.name == "No father"){
            text.text = "${user.fatherId} 的 父親"
        }
        if (user.name == "No mother"){
            text.text = "${user.motherId} 的 母親"
        }
        if (user.name == "No mateFather"){
            text.text = "${user.fatherId} 的 父親"
        }
        if (user.name == "No mateMother"){
            text.text = "${user.motherId} 的 母親"
        }
    }
}