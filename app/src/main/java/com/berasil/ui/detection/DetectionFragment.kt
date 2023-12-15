package com.berasil.ui.detection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.berasil.databinding.FragmentDetectionBinding
import com.berasil.getImageUri
import com.berasil.helper.DetectionListener
import com.berasil.helper.LoadingDialog
import com.berasil.reduceFileImage
import com.berasil.ui.MlViewModelFactory
import com.berasil.ui.detection.result.ResultActivity
import com.berasil.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class DetectionFragment : Fragment(), DetectionListener {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    var data = arrayOf(
        "Objek yang difoto hanya butir beras, gabah, sekam dan benda asing seperti kutu dan batu.",
        "Ratakan beras, jangan terlalu berimpit dan jangan bertumpuk.",
        "Foto di tempat yang terang atau pencahayaan yang cukup baik.",
        "Gunakan background foto polos dan kontras."
    )
    var selectedData = booleanArrayOf(false, false, false, false)

    private var currentImageUri: Uri? = null
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    private val detectionViewModel by viewModels<DetectionViewModel> {
        MlViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentImageUri = null

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        detectionViewModel.isImageUploaded.observe(viewLifecycleOwner) { isUploaded ->
            if (isUploaded) {
                if (currentImageUri != null) {
                    binding.buttonDetectionNow.isEnabled = true
                    binding.buttonDetectionNow.setOnClickListener {
                        val alertDialog =
                            ValidationDialog(requireContext(), data, selectedData, this)
                        alertDialog.show(parentFragmentManager, "multi choose dialog")
                        alertDialog.isCancelable = false
                    }
                }
            }
        }
    }

    override fun onDetectionRequested() {
        detectionImage()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun detectionImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()

            detectionViewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) loadingDialog.showLoading() else loadingDialog.hideLoading()
            }

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val file = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            detectionViewModel.detectionImage(file)

            detectionViewModel.detectionResponse.observe(viewLifecycleOwner) {
                val toResultActivity = Intent(activity, ResultActivity::class.java)
                toResultActivity.putExtra(ResultActivity.EXTRA_DETECTION_RESPONSE, it)
                startActivity(toResultActivity)
                activity?.finish()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
            detectionViewModel.setImageUploaded(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}