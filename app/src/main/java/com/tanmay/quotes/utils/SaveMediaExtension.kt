package com.tanmay.quotes.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun Bitmap.saveMediaToStorage(
    context: Context,
    requestForPermission: ActivityResultLauncher<Array<String>>,
    fileName: String
) {

    //Generating a file name
    val filename = "${fileName}.jpg"

    //Output stream
    var fos: OutputStream? = null

    //inline Functions
    fun askForPermission() {
        val storagePermission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestForPermission.launch(storagePermission)
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
            fos?.use {
                //Finally writing the bitmap to the output stream that we opened
                this.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            Toasty.success(
                context,
                " Saved Image Successfully to $imagesDir!",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    //For devices running android >= Q
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //getting the contentResolver
        context.contentResolver?.also { resolver ->

            //Content resolver will process the contentvalues
            val contentValues = ContentValues().apply {

                //putting file information in content values
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            //Inserting the contentValues to contentResolver and getting the Uri
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            //Opening an outputstream with the Uri that we got
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            this.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        Toasty.success(
            context,
            "Saved Image Successfully to /storage/emulated/0/Pictures!",
            Snackbar.LENGTH_SHORT
        ).show()
    } else {
        //These for devices running on android < Q
        //So I don't think an explanation is needed here
        askForPermission()
    }

}