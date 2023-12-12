package com.berasil.ui.detection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.berasil.databinding.FragmentDetectionBinding
import com.berasil.getImageUri
import com.berasil.reduceFileImage
import com.berasil.ui.MlViewModelFactory
import com.berasil.ui.detection.result.ResultActivity
import com.berasil.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

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

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.buttonDetectionNow.setOnClickListener { detectionImage() }
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
        } else {
            showToast("Tidak ada gambar yang dipilih")
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
        }
    }

    private fun detectionImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()

            detectionViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
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
        } ?: showToast("Silakan pilih gambar terlebih dahulu")
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetection.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonDetectionNow.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.galleryButton.isEnabled = !isLoading
        binding.cameraButton.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}