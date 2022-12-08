package com.example.multiversenigeria2.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multiversenigeria2.adapter.EbookItemAdapter
import com.example.multiversenigeria2.databinding.FragmentHomeBinding
import com.example.multiversenigeria2.model.EbookModel
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_pre_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userArrayList: ArrayList<EbookModel>
    private lateinit var myAdapter: EbookItemAdapter
    private  var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root


        return root

    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = binding.ebookRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = context?.let { EbookItemAdapter(it, userArrayList) }!!
        recyclerView.adapter = myAdapter
        EventChangeListener()


        binding.search.setOnClickListener {
            Toast.makeText(context, "Search by Course  Code , Course Title , University", Toast.LENGTH_SHORT).show()
            if(binding.searchEditText.isShown){
                binding.searchEditText.visibility = (View.GONE)
                binding.textEPQ.visibility = View.VISIBLE

            }else
            {
                binding.textEPQ.visibility = View.GONE
                binding.searchEditText.visibility = (View.VISIBLE)
                binding.searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                    override fun afterTextChanged(s: Editable) {
                        filters(s.toString())
                    }
                })
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("ShareVerse").document("Users").collection("Ebooks")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null){
                        return
                    }

                    for (dc : DocumentChange in  value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(EbookModel::class.java))
                            myAdapter.notifyDataSetChanged()
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                }

            })
    }

    private fun filters(search: String) {
        val filterlist: java.util.ArrayList<EbookModel> = java.util.ArrayList<EbookModel>()
        for (  item in userArrayList) {
            if (item.pdfCourseName?.toLowerCase()?.contains(search.toLowerCase()) == true || item.pdfCourseCode?.toLowerCase()?.contains(search.toLowerCase()) == true || item.pdfUniversity?.toLowerCase()?.contains(search.toLowerCase()) == true )

            {
                filterlist.add(item)
            }
        }
        myAdapter.Filteredlist(filterlist)

    }


}