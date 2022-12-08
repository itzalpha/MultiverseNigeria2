package com.example.multiversenigeria2.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.example.multiversenigeria2.databinding.ActivityJoinOurTeamBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JoinOurTeam extends AppCompatActivity {

    ActivityJoinOurTeamBinding binding;
    FirebaseFirestore db;
    ProgressDialog pd;
    DocumentReference documentReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinOurTeamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        binding.sendToAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yourSkill = binding.yourSkill.getText().toString().trim();
                String aboutYourSelf = binding.aboutYourself.getText().toString().trim();
                String whyAreYourInterested = binding.whyAreYouInterested.getText().toString().trim();
                String contact = binding.contact.getText().toString().trim();

                if (TextUtils.isEmpty(yourSkill) ){
                    Toast.makeText(JoinOurTeam.this, "Your Skill is Missing", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(aboutYourSelf)){
                    Toast.makeText(JoinOurTeam.this, "About YourSelf is Missing", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(whyAreYourInterested) ){
                    Toast.makeText(JoinOurTeam.this, "Why You Interested is Missing", Toast.LENGTH_SHORT).show();
                } else  if (TextUtils.isEmpty(contact)){
                    Toast.makeText(JoinOurTeam.this, "Your Contact is Missing", Toast.LENGTH_SHORT).show();
                } else {
                    saveReport(yourSkill ,aboutYourSelf , whyAreYourInterested , contact );
                }

            }
        });

    }

    private void saveReport(String yourSkill, String aboutYourSelf, String whyAreYourInterested, String contact) {
        pd.setMessage("Please Wait!");
        pd.show();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
        String  currentUserId = id.getUid();
        documentReference = db.collection("ShareVerse").document("Admin").collection("ShareVerseJoinOurTeam").document();

        Map<String,Object> user = new HashMap<>();
        user.put("shareVerseJoinOurTeamYourSkill", yourSkill);
        user.put("shareVerseJoinOurTeamAboutYourself" , aboutYourSelf);
        user.put("shareVerseJoinOurTeamWhyAreYouInterested", whyAreYourInterested);
        user.put("shareVerseJoinOurTeamContact",contact);
        user.put("shareVerseReportUserId",currentUserId);
        user.put("shareVerseJoinOurTeamDate" , strDate);

        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        binding.yourSkill.setText("");
                        binding.aboutYourself.setText("");
                        binding.whyAreYouInterested.setText("");
                        binding.contact.setText("");
                        Toast.makeText(JoinOurTeam.this,"Sent to the Administrator",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(JoinOurTeam.this,"Failed to send Report",Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
