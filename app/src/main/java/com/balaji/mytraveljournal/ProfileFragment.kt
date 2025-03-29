package com.balaji.mytraveljournal

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        val editbtn=view.findViewById<Button>(R.id.editprofilebtn)
        editbtn.setOnClickListener {
            imageUri=null
            showEditProfileDialog()
        }
        requestPermissions()
        return view
    }

    private fun showEditProfileDialog(){
        val dialogview=LayoutInflater.from(requireContext()).inflate(R.layout.edit_profile_view,null)
        val editname=dialogview.findViewById<EditText>(R.id.editnewname)
        val editemail=dialogview.findViewById<EditText>(R.id.editnewemail)
        val selectpimage=dialogview.findViewById<Button>(R.id.addprofileimagebtn)
        val userid=getUserId()
        imageView=dialogview.findViewById(R.id.profileimagepv)
        selectpimage.setOnClickListener {
            showImagePickerDialog()
        }
        val dialog=AlertDialog.Builder(requireContext())
            .setView(dialogview)
            .setPositiveButton("Update"){_,_->
                val newname=editname.text.toString()
                val newemail=editemail.text.toString()
                updateUser(userid,newname,newemail)
            }
            .setNegativeButton("Cancel",){_,_->
                imageUri=null
            }
            .create()
        dialog.show()
    }

    private fun updateUser(id:String?,uname:String,uemail:String){
        if(id==null){
            Toast.makeText(requireContext(),"userid is null", Toast.LENGTH_SHORT).show()
            return
        }
        val name= RequestBody.create("text/plain".toMediaTypeOrNull(),uname)
        val email=RequestBody.create("text/plain".toMediaTypeOrNull(),uemail)
        var imagePart:MultipartBody.Part?=null

        imageUri?.let { uri ->
            val file = copyUriToFile(requireContext(), uri) // Convert URI to File
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            imagePart = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
        }
        val apiService=Retrofit_Client.instance
        val call=apiService.updateUser(id,name,email,imagePart)
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"Details Updated Successfully",Toast.LENGTH_SHORT).show()
                    Log.d("main activity","sent successfully")
                }
                else{
                    Toast.makeText(requireContext(),"Details Not Updated",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("main activity","on failure"+t.message)
            }
        })
    }

    private fun getUserId():String?{
        val sharedPreferences=requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("user_id",null)
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