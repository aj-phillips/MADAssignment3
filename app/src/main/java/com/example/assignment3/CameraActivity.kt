package com.example.assignment3

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    //ListenableFuture interface listens for async operations external to
    //main thread
    //requires type of activity being observed - ProcessCameraProvider
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    //used to decide whether to use front or back camera
    private lateinit var cameraSelector: CameraSelector
    //use case for capturing images private
    var imageCapture: ImageCapture? = null
    //interface that extends Executor to provide thread for capturing an image
    private lateinit var imgCaptureExecutor: ExecutorService

    //static variables
    companion object {
        //used for messages output in debugger
        val TAG = "CameraActivity"
        //used to check request code
        private var REQUEST_CODE = 101 }

    override fun onCreate(savedInstanceState: Bundle?) {
        //instantiate imgCaptureExecutor
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //get instance of ProcessCameraProvider
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        //set default to back camera
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        //check for permissions (similar to other sensors)
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED )
        //request permissions
            ActivityCompat.requestPermissions( this, arrayOf(
                //array containing required permissions
                Manifest.permission.CAMERA ), REQUEST_CODE )

        else {
            //if permission already granted, start camera
            startCamera() }

        //set up event listener for btnCapture click
        val btnTakePhoto: Button = findViewById(R.id.btnTakePhoto)
        btnTakePhoto.setOnClickListener {
            takePhoto()
        }
    }

    //invoked when permissions change
    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //check that request code matches and permission granted
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            //if permission now granted, start camera
            startCamera()
        }
    }

    //listen for data from camera fun
    fun startCamera() {
        cameraProviderFuture.addListener({
            //create ProcessCameraProvider instance
            val cameraProvider = cameraProviderFuture.get()
            //connect preview use case to the preview in the xml file
            val preview: PreviewView = findViewById(R.id.preview)
            val previewCase = Preview.Builder().build().also {
                it.setSurfaceProvider(preview.surfaceProvider)
            }

            // Instantiate capture use case
            imageCapture = ImageCapture.Builder().build()

            try {
                //clear all bindings to previous use cases first
                cameraProvider.unbindAll()
                //bind lifecycle of camera to lifecycle of application
                cameraProvider.bindToLifecycle(this, cameraSelector, previewCase)
                cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)
            }
            catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }
        },

        //run asynchronous operation being listened to by cameraProviderFuture
        ContextCompat.getMainExecutor(this))
    }

    //take photo
    fun takePhoto() {
        imageCapture?.let {
            //create file with fileName
            val file = File(externalMediaDirs[0],
                "ProfileImage.jpg")

            //save image in above file
            val outputFileOptions =
                ImageCapture.OutputFileOptions.Builder(file).build()

            //call takePicture method with where to find image
            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                //set up callbacks for when picture is taken
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults:
                                              ImageCapture.OutputFileResults) {
                        Log.i(TAG, "Image saved in ${file.toUri()}")
                    }
                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText( getApplicationContext(), "Error taking photo", Toast.LENGTH_LONG ).show()
                        Log.i(TAG, "Error taking photo: $exception")
                    }
                }
            )
        }
        animateFlash()
        showImageTakenDialog()
    }

    //flash to provide feedback that photo taken
    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        val preview: PreviewView = findViewById(R.id.preview)

        preview.postDelayed({
            preview.foreground = ColorDrawable(Color.argb(125, 255, 255, 255))
            preview.postDelayed({
                preview.foreground = null
            }, 30)
        }, 60)
    }

    private fun showImageTakenDialog() {
        val builder = AlertDialog.Builder(this)
        val finishButton = { dialog: DialogInterface, which: Int ->
            finish()
        }

        builder.setTitle("Image Taken")
        builder.setMessage("Your profile picture has now been taken!")

        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = finishButton))

        builder.show()
    }
}