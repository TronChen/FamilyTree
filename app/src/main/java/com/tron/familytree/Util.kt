package com.tron.familytree

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tron.familytree.data.Family
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime

/**
 * According to [LoadApiStatus] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
    }
}

@BindingAdapter("setupApiStatusForLottie")
fun bindApiStatus(view: ConstraintLayout, status: LoadApiStatus?) {
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
    }
}

/**
 * According to [message] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiErrorMessage")
fun bindApiErrorMessage(view: TextView, message: String?) {
    when (message) {
        null, "" -> {
            view.visibility = View.GONE
        }
        else -> {
            view.text = message
            view.visibility = View.VISIBLE
        }
    }
}

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

@BindingAdapter(value = ["selectProperties","user","mate"],requireAll = false)
fun bindAddTitle(text : TextView,selectProperties: User?, user: String?, mate: String?) {
        if (selectProperties?.name == "No child"){
            text.text =  "$user 與 $mate 的 孩子"
        }
        if (selectProperties?.name  == "No mate"){
            text.text =  "$user 的 配偶"
        }
        if (selectProperties?.name == "No father"){
            text.text = "$user 的 父親"
        }
        if (selectProperties?.name == "No mother"){
            text.text = "$user 的 母親"
        }
        if (selectProperties?.name == "No mateFather"){
            text.text = "$user 的 父親"
        }
        if (selectProperties?.name == "No mateMother"){
            text.text = "$user 的 母親"
        }
}

@BindingAdapter("familyName")
fun familyName(text : TextView, family: Family?) {
   family?.let {
           text.text = family.title
   }
}

@ExperimentalTime
@BindingAdapter("timeToHrMin")
fun bindTimeToHrMin(text: TextView, time : Date?){
    time?.let {
        text.text = "${SimpleDateFormat("HH:mm").format(it.time)}"
    }
}