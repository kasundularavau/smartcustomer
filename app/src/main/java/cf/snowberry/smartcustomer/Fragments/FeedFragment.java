package cf.snowberry.smartcustomer.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cf.snowberry.smartcustomer.Adapters.AdapterCompanies;
import cf.snowberry.smartcustomer.Adapters.AdapterPost;
import cf.snowberry.smartcustomer.Models.ModelCompanies;
import cf.snowberry.smartcustomer.Models.ModelPost;
import cf.snowberry.smartcustomer.ProfileActivity;
import cf.snowberry.smartcustomer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    RecyclerView feed_recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;



    public FeedFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        feed_recyclerView = view.findViewById(R.id.feed_recyclerView);
        LinearLayoutManager feedManager = new LinearLayoutManager(getActivity());
        feedManager.setStackFromEnd(true);
        feedManager.setReverseLayout(true);
        feed_recyclerView.setLayoutManager(feedManager);


        postList = new ArrayList<>();
        loadPosts();

        return view;
    }

    private void loadPosts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);
                    adapterPost = new AdapterPost(getActivity(),postList);
                    feed_recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_feed, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_profile) {

            startActivity(new Intent(getActivity(), ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}
