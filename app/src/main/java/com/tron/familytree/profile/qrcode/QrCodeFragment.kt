package com.tron.familytree.profile.qrcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentQrCodeBinding
import com.tron.familytree.util.UserManager


class QrCodeFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val ivCode: ImageView = binding.imageCode
            val encoder = BarcodeEncoder()
            try {
                val bit = encoder.encodeBitmap(
                    UserManager.email
                    , BarcodeFormat.QR_CODE, 4000, 4000
                )
                ivCode.setImageBitmap(bit)
            } catch (e: WriterException) {
                e.printStackTrace()
            }


        // Inflate the layout for this fragment
        return binding.root
    }


}