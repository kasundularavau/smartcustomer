package cf.snowberry.smartcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import cf.snowberry.smartcustomer.Adapters.AdapterComment;
import cf.snowberry.smartcustomer.Models.ModelComment;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView pImageIv;
    CircleImageView pLogoCi, proPicInCommentTypeBar;
    TextView cNameTv, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv, pCommentsTv;
    ImageButton addCommentIb;
    EditText commentEt;

    RecyclerView commentsRv;
    AdapterComment adapterComment;
    List<ModelComment> commentList;

    String postId;

    String myUid, myProPic, myName;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        postId = intent.getStringExtra("thisPostId");


        pImageIv = findViewById(R.id.pImageIv);
        pLogoCi = findViewById(R.id.pLogoCi);
        cNameTv = findViewById(R.id.cNameTv);
        pTimeTv = findViewById(R.id.pTimeTv);
        pTitleTv = findViewById(R.id.pTitleTv);
        pDescriptionTv = findViewById(R.id.pDescriptionTv);
        pLikesTv = findViewById(R.id.pLikesTv);
        pCommentsTv = findViewById(R.id.pCommentsTv);
        proPicInCommentTypeBar = findViewById(R.id.proPicInCommentTypeBar);
        addCommentIb = findViewById(R.id.addCommentIb);
        commentEt = findViewById(R.id.commentEt);

        commentsRv = findViewById(R.id.commentsRv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        commentsRv.setLayoutManager(layoutManager);

        checkUserStatus();

        loadPostDetails();

        loadUserInfo();

        commentList = new ArrayList<>();

        loadComments();

        addCommentIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
    }

    private void loadComments() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    ModelComment modelComment = ds.getValue(ModelComment.class);

                    commentList.add(modelComment);
                    adapterComment = new AdapterComment(PostDetailsActivity.this, commentList, myUid, postId);
                    commentsRv.setAdapter(adapterComment);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PostDetailsActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postComment() {

        final String comment = commentEt.getText().toString().trim();

        if (TextUtils.isEmpty(comment)){
            Toast.makeText(this, "comment is empty...", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String commentId = UUID.randomUUID().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", commentId);
        hashMap.put("comment", comment);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("uId", myUid);
        hashMap.put("uProPic", myProPic);
        hashMap.put("uName", myName);

        ref.child(commentId).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(PostDetailsActivity.this, "Comment Added..", Toast.LENGTH_SHORT).show();
                        commentEt.setText("");
                        commentEt.setFocusable(false);
                        updateCommentCount();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(PostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCommentCount() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ref.child("pComments").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = ref.orderByChild("uid").equalTo(myUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    myName = ""+ds.child("name").getValue();
                    myProPic = ""+ds.child("profile_pic").getValue();

                    try {
                        Picasso.get().load(myProPic).placeholder(R.drawable.user_place_holder).into(proPicInCommentTypeBar);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.user_place_holder).into(proPicInCommentTypeBar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    postId = ""+ds.child("pId").getValue();
                    String companyId = ""+ds.child("uid").getValue();
                    String postTitle = ""+ds.child("pTitle").getValue();
                    String postDescription = ""+ds.child("pDescription").getValue();
                    String postImage = ""+ds.child("pImage").getValue();
                    String pTimeStamp = ""+ds.child("pTime").getValue();
                    String companyName = ""+ds.child("uName").getValue();
                    String companyLogo = ""+ds.child("uDp").getValue();
                    String postLikes = ""+ds.child("pLikes").getValue();
                    String postCommentCount = ""+ds.child("pComments").getValue();

                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String postedTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

                    cNameTv.setText(companyName);
                    pTimeTv.setText(postedTime);
                    pTitleTv.setText(postTitle);
                    pDescriptionTv.setText(postDescription);
                    pLikesTv.setText(postLikes + " Likes");
                    pCommentsTv.setText(postCommentCount + " Comments");

                    String cPostedTime = DateFormat.format("dd/MM/yyyy", cal).toString();
                    actionBar.setTitle(postTitle);
                    actionBar.setSubtitle(companyName + " posted" + " on " + cPostedTime);

                    try{
                        Picasso.get()
                                .load(companyLogo)
                                .resizeDimen(R.dimen.company_logo_post_width, R.dimen.company_logo_post_height)
                                .placeholder(R.drawable.company_place_holder)
                                .into(pLogoCi);
                    }catch (Exception e){

                    }

                    Transformation transformation = new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {

                            int  targetWidth = pImageIv.getWidth();

                            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                            int targetHeight = (int) (targetWidth * aspectRatio);
                            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight,false);
                            if (result != source){
                                source.recycle();
                            }
                            return result;
                        }

                        @Override
                        public String key() {
                            return "transformation" + "desireWidth";
                        }
                    };

                    try {
                        Picasso.get()
                                .load(postImage)
                                .transform(transformation)
                                .placeholder(R.drawable.post_place_holder)
                                .into(pImageIv);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.post_place_holder).into(pImageIv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkUserStatus() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            myUid = user.getUid();

        }else {
            startActivity(new Intent(PostDetailsActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
