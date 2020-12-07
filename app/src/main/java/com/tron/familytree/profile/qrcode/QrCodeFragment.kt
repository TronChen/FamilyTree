package com.tron.familytree.profile.qrcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tron.familytree.databinding.FragmentQrCodeBinding


class QrCodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val btn = binding.buttonCode

        btn.setOnClickListener {

            val ivCode: ImageView = binding.imageCode
            val etContent = binding.editCode
            val encoder = BarcodeEncoder()
            try {
                val bit = encoder.encodeBitmap(
                    etContent.text.toString()
                    , BarcodeFormat.QR_CODE, 250, 250
                )
                ivCode.setImageBitmap(bit)
            } catch (e: WriterException) {
                e.printStackTrace()
            }

        }


        // Inflate the layout for this fragment
        return binding.root
    }


}