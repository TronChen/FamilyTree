package com.tron.familytree.profile.qrcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.Result
import com.tron.familytree.MainActivity
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentQrCodeReaderBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView


class QrCodeReaderFragment : Fragment(), ZXingScannerView.ResultHandler {

    lateinit var scannerView: ZXingScannerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentQrCodeReaderBinding.inflate(inflater, container, false)

        scannerView =  binding.zxScannerView

        binding.conShowMyCode.setOnClickListener {
            findNavController().navigate(R.id.action_global_qrCodeFragment)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun handleResult(p0: Result?) {
    }



//    private fun isCameraPermissionGranted(): Boolean {
//        val selfPermission =
//            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
//        return selfPermission == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (isCameraPermissionGranted()) {
//                textureView.post { startCamera() }
//            } else {
//                Toast.makeText(requireContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show()
////                finish()
//            }
//        }
//    }

}