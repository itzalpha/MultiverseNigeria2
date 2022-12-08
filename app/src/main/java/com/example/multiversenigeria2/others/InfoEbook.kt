package com.example.multiversenigeria2.others

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria2.FirebaseUtils
import com.example.multiversenigeria2.R
import com.example.multiversenigeria2.adapter.ReviewItemAdapter
import com.example.multiversenigeria2.databinding.ActivityInfoEbookBinding
import com.example.multiversenigeria2.model.ReviewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class InfoEbook : AppCompatActivity() {

    private lateinit var binding : ActivityInfoEbookBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var currentUserId : String
    var documentReference: DocumentReference? = null
    private lateinit var comment_text : String
    private lateinit var  coursePdfId : String
    private lateinit var courseUrl : String
    private lateinit var userArrayList: ArrayList<ReviewModel>
    private lateinit var myAdapter: ReviewItemAdapter
    private  var db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoEbookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val id = FirebaseAuth.getInstance().currentUser
        currentUserId = id!!.uid

        val courseCode = intent.getStringExtra("courseCode")
        val courseTitle = intent.getStringExtra("courseTitle")
        val courseDate = intent.getStringExtra("courseDate")
        coursePdfId = intent.getStringExtra("coursePdfId").toString()
        courseUrl = intent.getStringExtra("courseUrl").toString()
        val courseUniversity = intent.getStringExtra("courseUniversity")
        val coursePosterName = intent.getStringExtra("coursePosterName")

        binding.courseCode.text = "Course Code : $courseCode"
        binding.courseTitle.text = "Course Title : $courseTitle"
        binding.courseUploadedDate.text = "Course Uploaded Date : $courseDate"
        binding.courseUniversity.text = "Course University : $courseUniversity"
        binding.uploadedBy.text = "Uploaded by : $coursePosterName"

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading....")

        binding.commentButton.setOnClickListener {
            comment_text = binding.commentText.text.toString().trim()

            if (comment_text.isEmpty() ){
                Toast.makeText(this , "Comment Empty" , Toast.LENGTH_SHORT).show()
            }else if (comment_text.length < 10){
                Toast.makeText(this , "Comment too short" , Toast.LENGTH_SHORT).show()
            } else{
                progressDialog.show()
                documentReference = FirebaseUtils().fireStoreDatabase.collection("ShareVerse").document("Users").collection("Comments").document("Ebooks & Past Questions").collection(coursePdfId.toString()).document(currentUserId)
                val docId = documentReference!!.id

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                val date = current.format(formatter)

                val hashMap = hashMapOf<String , Any>(
                    "comment" to comment_text,
                    "commentDate" to date ,
                    "commentPostId" to    docId,
                    "commentPosterDocumentId" to currentUserId
                )

                documentReference!!.set(hashMap)
                    .addOnSuccessListener {
                        if (progressDialog.isShowing) progressDialog.dismiss()
                        binding.commentText.setText("")
                        Toast.makeText(this, "Comment Added", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener { exception ->
                        if (progressDialog.isShowing)progressDialog.dismiss()
                        Toast.makeText(this, "Error Adding Comment $exception", Toast.LENGTH_SHORT).show()
                    }
            }

        }

        binding.shareBtn.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.app_name)
                .setText("Download the Ebook with code ~ $courseCode ~ and title ~ $courseTitle ~ on the app Share Verse. \nGet in Playstore https://play.google.com/store/apps/details?id="+applicationContext.packageName)
                .startChooser()
        }

        binding.downloadBtn.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(courseUrl)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val recyclerView : RecyclerView = binding.recyclerviewComment
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = ReviewItemAdapter(this, userArrayList)
        recyclerView.adapter = myAdapter
        EventChangeListener()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()

        db.collection("ShareVerse").document("Users").collection("Comments").document("Ebooks & Past Questions").collection(coursePdfId)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null){
                        return
                    }
                    for (dc : DocumentChange in  value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(ReviewModel::class.java))
                            myAdapter.notifyDataSetChanged()
                        }
                    }

                }
            })
    }

}

