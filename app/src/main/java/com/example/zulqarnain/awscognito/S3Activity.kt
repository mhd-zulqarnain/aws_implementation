package com.example.zulqarnain.awscognito

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amazonaws.mobile.client.AWSMobileClient;
import android.content.Intent
import android.database.Cursor
import android.view.View
import kotlinx.android.synthetic.main.activity_s3.*
import android.provider.MediaStore
import android.widget.Toast
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File
import com.amazonaws.auth.BasicAWSCredentials
import java.util.regex.Pattern


class S3Activity : AppCompatActivity() {
    private  var mImagePath:Uri?=null
    private val PICKIMGREQUEST: Int = 1
    var s3Client: AmazonS3? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s3)
        uploadWithTransferUtility()
    }

    fun uploadWithTransferUtility() {

        val credentials = BasicAWSCredentials("SEC_KEY","ACCESS_KEY")
        val s3Client = AmazonS3Client(credentials)
        val transferUtility = TransferUtility.builder()
                .context(this.applicationContext)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .s3Client(s3Client).build()

            var uploadObserver: TransferObserver

            btn_done.setOnClickListener {
                if (mImagePath != null) {
                    var path = getImageFilePath(this, mImagePath!!)
                    println("the file path " + path)
                    var fileName =getFileName(this, mImagePath!!)
                    
                    img_name.setText(fileName)
                    uploadObserver = transferUtility.upload("joviparks/profile_picture",
                            "Myfi1",  File(path))
                    uploadObserver.setTransferListener(object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState) {
                        if (TransferState.COMPLETED == state) {
                            Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                            println("the object file "+state+" the int id "+id)
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                        val percentDonef = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                        val percentDone = percentDonef.toInt()
                        Toast.makeText(applicationContext, "uploading.....", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(id: Int, ex: Exception) {
                        // Handle errors
                        Toast.makeText(applicationContext, "error " + ex, Toast.LENGTH_SHORT).show()
                    }

                })
            }}

        // If you prefer to long-poll for updates
        /*  if (uploadObserver.state == TransferState.COMPLETED) {
              *//* Handle completion *//*
        }

        val bytesTransferred = uploadObserver.bytesTransferred*/
    }

    fun addImage(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICKIMGREQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == PICKIMGREQUEST && resultCode == Activity.RESULT_OK) {
            mImagePath = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mImagePath)

            img_view.setImageBitmap(bitmap)
        }
    }

    fun getImageFilePath(context: Context, uri: Uri): String? {
        var cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var image_id = cursor.getString(0)
        image_id = image_id.substring(image_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = context.contentResolver.query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(image_id), null)
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

fun getFileName(context: Context, uri: Uri):String?{
    val path = getImageFilePath(context, uri)
    val filename = path!!.substring(path.lastIndexOf("/") + 1)
    val fileWOExtension: String
    if (filename.indexOf(".") > 0) {
        fileWOExtension = filename.substring(0, filename.lastIndexOf("."))
    } else {
        fileWOExtension = filename
    }
    return filename
}

}

