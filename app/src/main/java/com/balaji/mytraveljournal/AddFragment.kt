package com.balaji.mytraveljournal

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.balaji.mytraveljournal.api.Retrofit_Client
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var addImageButton: Button
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        imageView = view.findViewById(R.id.imageView)
        addImageButton = view.findViewById(R.id.addImageButton)
        val etname=view.findViewById<EditText>(R.id.inputjname)
        val etdesc=view.findViewById<EditText>(R.id.inputjdesc)
        val etlocation=view.findViewById<EditText>(R.id.inputjlocation)
        val postbtn=view.findViewById<Button>(R.id.postjournalbtn)
        val userid=getUserId()
        postbtn.setOnClickListener {
            val name=etname.text.toString()
            val description=etdesc.text.toString()
            val location=etlocation.text.toString()
            uploadJournal(name,description,location,userid)
        }

        addImageButton.setOnClickListener {
            showImagePickerDialog()
        }

        requestPermissions() // Ensure permissions are granted

        return view
    }

    private fun getUserId():String?{
        val sharedPreferences=requireActivity().getSharedPreferences("UserPrefs",AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("user_id",null)
    }

    private fun uploadJournal(jname:String,jdesc:String,jlocation:String,userid:String?){
        if(userid==null){
            Toast.makeText(requireContext(),"userid is null",Toast.LENGTH_SHORT).show()
            return
        }
        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), userid) // Change with actual user ID
        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), jname)
        val description = RequestBody.create("text/plain".toMediaTypeOrNull(), jdesc)
        val location = RequestBody.create("text/plain".toMediaTypeOrNull(), jlocation)

        var imagePart: MultipartBody.Part? = null

        imageUri?.let { uri ->
            val file = copyUriToFile(requireContext(), uri) // Convert URI to File
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        val apiService = Retrofit_Client.instance
        val call = apiService.uploadJournal(userId, name, description, location, imagePart)

        call.enqueue(object : retrofit2.Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {
                if (response.isSuccessful) {
                    // Successfully uploaded
                    Toast.makeText(requireContext(), "Journal posted successfully!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to upload journal", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("main activity","failuer in "+t.message)
            }
        })
    }

    private fun copyUriToFile(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg") // Save in cache directory
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file
    }


    private fun showImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Take a Photo")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> pickImageFromGallery()
                1 -> captureImageFromCamera()
            }
        }
        builder.show()
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageView.setImageURI(it)
            imageUri = it
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri?.let {
                imageView.setImageURI(it)
            }
        }
    }

    private fun pickImageFromGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun captureImageFromCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        photoFile?.let {
            imageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                it
            )
            cameraLauncher.launch(imageUri)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissions.toTypedArray(), 101)
        }
    }
}
