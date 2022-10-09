package dev.adryanev.dicodingstory.shared.presentation.pages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.adryanev.dicodingstory.core.presentations.error_handler.showToast
import dev.adryanev.dicodingstory.core.presentations.setSingleClick
import dev.adryanev.dicodingstory.core.utils.createFile
import dev.adryanev.dicodingstory.databinding.FragmentCameraPreviewBinding
import dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels.NewStoryViewModel
import timber.log.Timber

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CameraPreviewFragment : Fragment() {

    private var _binding: FragmentCameraPreviewBinding? = null
    private val binding: FragmentCameraPreviewBinding
        get() = _binding ?: throw UninitializedPropertyAccessException()

    private val viewModel: NewStoryViewModel by activityViewModels()

    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val requestPermissionCamera = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        Timber.d("Permission Results: $it")
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requireContext().showToast("Permission not granted by user")
        }

    }

    private fun navigateBack() {
        findNavController().navigateUp()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraPreviewBinding.inflate(inflater, container, false)
        binding.cameraPreviewFlipButton.setSingleClick {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }

        binding.imageCaptureButton.setSingleClick {
            Timber.d("Capturing image")
            takePhoto()
        }

        binding.cameraPreviewBackButton.setSingleClick {
            navigateBack()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCamera()
    }

    private fun setupCamera() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionCamera.launch(
                REQUIRED_PERMISSIONS
            )
        }

    }

    private fun startCamera() {
        Timber.d("Starting Camera")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraViewFinder.surfaceProvider)
            }

            // Select back camera as a default
            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(1080, 1920))
                .build()
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Timber.e("Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {

        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireActivity().application)


        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.e("Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded"
                    requireContext().showToast(msg)
                    Timber.d(msg)
                    viewModel.setStoryPicture(photoFile)
                    navigateBack()
                }
            })

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}