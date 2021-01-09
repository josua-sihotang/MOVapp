package com.josua.movapp.sign.signup


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.josua.movapp.home.HomeActivity
import com.josua.movapp.R
import com.josua.movapp.utils.Preferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*


class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filepath: Uri

    lateinit var storage : FirebaseStorage
    lateinit var storageReferensi : StorageReference
    lateinit var preference : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preference = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReferensi = storage.getReference()

        tv_hello.text = "Selamat Datang\n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                iv_profile.setImageResource(R.drawable.photo)
            } else {
                Dexter.withActivity(this)
                    .withPermission(android.Manifest.permission.CAMERA)
                    .withListener(this)
                    .check()
            }
        }

        btn_home.setOnClickListener {
            finishAffinity()

            var goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
            startActivity(goHome)
        }
        btn_save.setOnClickListener {
            if (filepath != null) {
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                var ref = storageReferensi.child("images/"+UUID.randomUUID().toString())
                ref.putFile(filepath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preference.setValues("Url", it.toString())
                        }
                        finishAffinity()
                        var goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
                        startActivity(goHome)
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                        taskSnapshot -> var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload"+progress.toInt()+" %")
                    }

            } else {

            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
       Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
           takePictureIntent ->
           takePictureIntent.resolveActivity(packageManager)?.also {
               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
           }
       }
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan foto profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? klik tombol upload nanti aja", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filepath = data.getData()!!
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        }
    }

}
