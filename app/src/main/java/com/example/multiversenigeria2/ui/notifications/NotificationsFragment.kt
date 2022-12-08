package com.example.multiversenigeria2.ui.notifications

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.example.multiversenigeria2.R
import com.example.multiversenigeria2.SplashActivity
import com.example.multiversenigeria2.databinding.FragmentNotificationsBinding
import com.example.multiversenigeria2.me.*
import com.example.multiversenigeria2.others.SubscribeForTheMonth
import com.example.multiversenigeria2.others.UploadEbooks
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.uploadEbook.setOnClickListener {
            startActivity(Intent(context ,  UploadEbooks::class.java))
        }

        binding.subscribeForTheMonth.setOnClickListener {
            startActivity(Intent(context ,  SubscribeForTheMonth::class.java))
        }

        binding.reportaproblem.setOnClickListener {
            startActivity(Intent(context ,  ReportAProblem::class.java))
        }

        binding.sendfeedback.setOnClickListener {
            startActivity(Intent(context ,  SendFeedback::class.java))
        }

        binding.joinOurTeam.setOnClickListener {
            startActivity(Intent(context ,  JoinOurTeam::class.java))
        }

        binding.shareApp.setOnClickListener {
            ShareCompat.IntentBuilder.from(context as Activity)
                .setType("text/plain")
                .setChooserTitle("Skill Verse")
                .setText("http://play.google.com/store/apps/details?id=" + (context as Activity).applicationContext.packageName)
                .startChooser()
        }

        binding.rateApp.setOnClickListener {
            val uri =
                Uri.parse("https://play.google.com/store/apps/details?id=" + (context as Activity).applicationContext.packageName)
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
            try {
                startActivity(i)
            } catch (e: Exception) {
                Toast.makeText(
                    context, """Unable to Open
                ${e.message}""", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.aboutShareVerse.setOnClickListener {
            startActivity(Intent(context ,  AboutShareVerse::class.java))
        }

        binding.verseCityTechCompany.setOnClickListener {
            startActivity(Intent(context ,  Multiverse::class.java))
        }

        binding.privacypolicy.setOnClickListener {
            startActivity(Intent(context ,  PrivacyPolicy::class.java))
        }

        binding.communityguidelines.setOnClickListener {
            startActivity(Intent(context ,  CommunityGuidelines::class.java))
        }

        binding.logout.setOnClickListener {
            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle(R.string.app_name)
            builder?.setIcon(R.mipmap.ic_launcher)
            builder?.setMessage("Do you want to Log out")
                ?.setCancelable(false)
                ?.setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(context, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT)
                        .show()
                }?.setNegativeButton(
                    "No"
                ) { dialogInterface, _ -> dialogInterface.cancel() }
            val alert = builder?.create()
            alert?.show()


        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}