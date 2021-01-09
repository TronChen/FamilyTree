package com.tron.familytree.profile.qrcode

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.zxing.Result
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentQrCodeReaderBinding
import com.tron.familytree.ext.getVmFactory
import me.dm7.barcodescanner.zxing.ZXingScannerView


class QrCodeReaderFragment : Fragment(), ZXingScannerView.ResultHandler {

    lateinit var scannerView: ZXingScannerView

    private var cameraPermission = false
    var CAMERA_PERMISSION_ID = 1010

    private val viewModel by viewModels<QrCodeReaderViewModel> { getVmFactory(
        QrCodeReaderFragmentArgs.fromBundle(
            requireArguments()
        ).userProperties)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentQrCodeReaderBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        scannerView =  binding.zxScannerView

        getCameraPermission()
        if (!cameraPermission){
            binding.conPermission.visibility = View.VISIBLE
        }

        viewModel.scanResult.observe(viewLifecycleOwner, Observer {
            viewModel.findUserById(it)
        })

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("QrCodeUser", it.toString())
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
//            findNavController().navigate(QrCodeReaderFragmentDirections.actionGlobalAddPeopleDialog(it))

            when (viewModel.selectedProperty.value?.name){

                "No mate" ->{
                    viewModel.updateMemberMateId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }

                "No father" -> {
                    viewModel.updateMemberFatherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No mother" -> {
                    viewModel.updateMemberMotherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No mateFather" -> {
                    viewModel.updateMemberFatherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No mateMother" -> {
                    viewModel.updateMemberMotherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No child" -> {
                    viewModel.updateChild(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
            }
        })

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

    override fun handleResult(rawResult: Result) {
        Toast.makeText(
            activity, "Contents = " + rawResult.text +
                    ", Format = " + rawResult.barcodeFormat.toString(), Toast.LENGTH_SHORT
        ).show()

        viewModel.scanResult.value =  rawResult.text
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        val handler = Handler()
        handler.postDelayed(
            Runnable { scannerView.resumeCameraPreview(this@QrCodeReaderFragment) },
            2000
        )
    }


    private fun getCameraPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermission = false
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_ID)
        } else {
            cameraPermission = true
        }
    }

//    private fun isCameraPermissionGranted(): Boolean {
//        val selfPermission =
//            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
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
////                textureView.post { startCamera() }
//            } else {
//                Toast.makeText(requireContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show()
////                finish()
//            }
//        }
//    }

}