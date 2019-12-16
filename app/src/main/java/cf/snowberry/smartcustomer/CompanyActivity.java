package cf.snowberry.smartcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cf.snowberry.smartcustomer.Adapters.AdapterPost;
import cf.snowberry.smartcustomer.Models.ModelPost;
import de.hdodenhof.circleimageview.CircleImageView;


public class CompanyActivity extends AppCompatActivity {

    ImageView coverOnCompanyIv;
    CircleImageView logoOnCompanyCiv;
    ImageButton callIb, chatIb, productAndServicesIb;
    TextView descriptionOnCompanyTv;
    RecyclerView postsOnCompanyRv;

    ActionBar actionBar;

    String thisCompanyId;
    String thisCompanyIdToGive;

    FirebaseDatabase database;
    DatabaseReference reference;

    ArrayList<ModelPost> postArrayList;
    AdapterPost adapterPost;

    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        thisCompanyId = intent.getStringExtra("thisCompanyId");

        coverOnCompanyIv = findViewById(R.id.coverOnCompanyIv);
        logoOnCompanyCiv = findViewById(R.id.logoOnCompanyCiv);
        callIb = findViewById(R.id.callIb);
        chatIb = findViewById(R.id.chatIb);
        productAndServicesIb = findViewById(R.id.productAndServicesIb);
        descriptionOnCompanyTv = findViewById(R.id.descriptionOnCompanyTv);
        postsOnCompanyRv = findViewById(R.id.postsOnCompanyRv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        postsOnCompanyRv.setLayoutManager(linearLayoutManager);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Companies");

        Query query = reference.orderByChild("companyID").equalTo(thisCompanyId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String cover = "" + ds.child("companyCoverPhoto").getValue();
                    String logo = "" + ds.child("companyLogo").getValue();
                    String name = "" + ds.child("companyName").getValue();
                    String description = "" + ds.child("companyDescription").getValue();
                    phone = "" + ds.child("companyPhone").getValue();
                    thisCompanyIdToGive = "" + ds.child("companyID").getValue();

                    actionBar.setTitle(name);

                    try {
                        Picasso.get().load(cover).placeholder(R.drawable.image_place_holder).into(coverOnCompanyIv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.image_place_holder).into(coverOnCompanyIv);
                    }

                    try {
                        Picasso.get().load(logo).placeholder(R.drawable.company_place_holder).into(logoOnCompanyCiv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.company_place_holder).into(logoOnCompanyCiv);
                    }

                    descriptionOnCompanyTv.setText(description);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postArrayList = new ArrayList<>();
        loadThisCompanyPosts();


        chatIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CompanyActivity.this, ChatActivity.class);
                intent1.putExtra("companyID", thisCompanyIdToGive);
                startActivity(intent1);
            }
        });

        callIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(CompanyActivity.this, "Starting to Call "+ actionBar.getTitle().toString(), Toast.LENGTH_LONG).show();

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));

                startActivity(callIntent);

            }
        });

        productAndServicesIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(CompanyActivity.this, ProductAndServicesActivity.class);
                intent1.putExtra("companyId",thisCompanyIdToGive);
                startActivity(intent1);

            }
        });

    }

    private void loadThisCompanyPosts() {

        FirebaseDatabase dbPosts = FirebaseDatabase.getInstance();
        DatabaseReference refPosts = dbPosts.getReference("Posts");

        Query query = refPosts.orderByChild("uid").equalTo(thisCompanyId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postArrayList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postArrayList.add(modelPost);
                    adapterPost = new AdapterPost(CompanyActivity.this, postArrayList);
                    postsOnCompanyRv.setAdapter(adapterPost);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CompanyActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
