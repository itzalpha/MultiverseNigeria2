package com.example.multiversenigeria2.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria2.R
import com.example.multiversenigeria2.model.EbookModel
import com.example.multiversenigeria2.others.InfoEbook
import kotlinx.android.synthetic.main.pdf_item_layout.view.*
import java.util.*


class EbookItemAdapter(val context: Context, private var userList : ArrayList<EbookModel>) :
    RecyclerView.Adapter<EbookItemAdapter.ViewHolder>() {
    private lateinit var progressDialog : ProgressDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pdf_item_layout ,parent , false  )
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading....")
        return ViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : EbookModel = userList[position]


        /* TODO:Assign Value to xml object */
        holder.courseCode.text = user.pdfCourseCode
        holder.courseTitle.text =user.pdfCourseName


        /* TODO:Perform Action in xml object */

        holder.ebookDownload.setOnClickListener {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(user.pdfUrl)
                    )
                )
            } catch (e: ActivityNotFoundException) {

                Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        holder.ebookLayout.setOnClickListener {
            val intent = Intent(context, InfoEbook::class.java)
            intent.putExtra("courseCode", user.pdfCourseCode)
            intent.putExtra("courseTitle", user.pdfCourseName)
            intent.putExtra("courseDate", user.pdfDate)
            intent.putExtra("coursePdfId", user.pdfPostId)
            intent.putExtra("courseUrl", user.pdfUrl)
            intent.putExtra("courseUniversity" , user.pdfUniversity)
            intent.putExtra("coursePosterName", user.posterName)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun Filteredlist(filterlist: ArrayList<EbookModel>) {
        userList = filterlist
        notifyDataSetChanged()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val courseCode = view.courseCode
        val courseTitle = view.courseTitle
        val ebookDownload = view.ebookDownload
        val ebookLayout = view.ebook_layout
    }

}