package com.example.multiversenigeria2.others

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.multiversenigeria2.FirebaseUtils
import com.example.multiversenigeria2.NoInternet
import com.example.multiversenigeria2.R
import com.example.multiversenigeria2.databinding.ActivityUploadEbooksBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.pdf_upload_website.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class UploadEbooks : AppCompatActivity() {

    private lateinit var binding : ActivityUploadEbooksBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var category: String
    private lateinit var currentUserId : String

    var documentReference: DocumentReference? = null

    private lateinit var ebook_course_code: String
    private lateinit var ebook_course_name: String
    private lateinit var ebook_url_link: String
    private lateinit var ebook_university : String
    private lateinit var  name : String

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadEbooksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val id = FirebaseAuth.getInstance().currentUser
         currentUserId = id!!.uid

        val docReference: DocumentReference
        val firebaseFirestore = FirebaseFirestore.getInstance()
        docReference = firebaseFirestore.collection("Verse").document("Users").collection("Users").document(currentUserId)
        docReference.get()
            .addOnCompleteListener { task ->
                if (task.result.exists()) {
                     name = task.result.getString("name").toString()
                }

    }


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading....")
        progressDialog.setCancelable(false)

        val items = arrayOf(
            "Select Category",
            "Ebooks",
            "Past Question",

        )

        binding.ebookCategory.adapter =
            ArrayAdapter(this , android.R.layout.simple_spinner_dropdown_item , items)

        binding.ebookCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                category = binding.ebookCategory.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.uploadEbook.setOnClickListener {
             ebook_course_code = binding.ebookCourseCode.text.toString().trim()
             ebook_course_name = binding.ebookCourseName.text.toString().trim()
             ebook_url_link = binding.ebookUrlLink.text.toString().trim()
             ebook_university = binding.ebookUniversity.text.toString().trim()

            if ( ebook_course_code.isNotEmpty() && ebook_course_name.isNotEmpty() && ebook_url_link.isNotEmpty()  && ebook_university.isNotEmpty() ) {
                upload()
            }else if (category == "Select Category"){
                Toast.makeText(this , "Pick Category" , Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this , "Credentials not complete" , Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadWebsites.setOnClickListener {
            popup()
        }

    }

    private fun popup() {
        val builder = AlertDialog.Builder(this , R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.pdf_upload_website , null)
         builder.setView(view)
        val markup =  view.mark_up_click
        val filetransfer = view.file_transfer_io_click
        val googledrive = view.google_drive_click
        val wetransfer = view.we_transfer_click
        val donebtn = view.done

        donebtn.setOnClickListener {
        builder.dismiss()
        }

        markup.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://app.markup.io/")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }
        filetransfer.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://filetransfer.io/")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }
        googledrive.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://drive.google.com/")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }
        wetransfer.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://auth.wetransfer.com/")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upload() {
        progressDialog.show()
        documentReference = FirebaseUtils().fireStoreDatabase.collection("ShareVerse").document("Users").collection(category).document()
        val docId = documentReference!!.id
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val date = current.format(formatter)
        val hashMap = hashMapOf<String , Any>(
            "pdfCourseCode" to ebook_course_code,
            "pdfCourseName" to ebook_course_name,
            "pdfUrl" to ebook_url_link,
            "pdfUniversity" to ebook_university,
            "pdfDate" to date ,
            "category" to category,
            "pdfPostId" to    docId,
            "posterDocumentId" to currentUserId ,
             "posterName" to name
        )
        documentReference!!.set(hashMap)
            .addOnSuccessListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                binding.ebookUrlLink.setText("")
                binding.ebookCourseName.setText("")
                binding.ebookCourseCode.setText("")
                Toast.makeText(this, "Ebook Uploaded Successfully", Toast.LENGTH_SHORT)
                    .show()

            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Error Uploading Ebook $exception", Toast.LENGTH_SHORT).show()
            }


    }


    override fun onStart() {
        super.onStart()
        if (isConnected()){

        }else{
            startActivity(Intent(this, NoInternet::class.java))
        }
    }

    fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }

}