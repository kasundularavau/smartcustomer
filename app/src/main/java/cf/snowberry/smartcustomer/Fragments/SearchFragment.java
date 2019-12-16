package cf.snowberry.smartcustomer.Fragments;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cf.snowberry.smartcustomer.Adapters.AdapterCompanies;
import cf.snowberry.smartcustomer.Models.ModelCompanies;
import cf.snowberry.smartcustomer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    RecyclerView companies_recyclerView;

    AdapterCompanies adapterCompanies;
    List<ModelCompanies> companiesList;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        companies_recyclerView = view.findViewById(R.id.companies_recyclerView);
        companies_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        companiesList = new ArrayList<>();

        getAllCompanies();

        return view;
    }

    private void getAllCompanies() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companiesList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelCompanies modelCompanies = ds.getValue(ModelCompanies.class);

                    companiesList.add(modelCompanies);

                    adapterCompanies = new AdapterCompanies(getActivity(), companiesList);
                    companies_recyclerView.setAdapter(adapterCompanies);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchCompany(final String query) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companiesList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelCompanies modelCompanies = ds.getValue(ModelCompanies.class);

                    if (modelCompanies.getCompanyName().toLowerCase().contains(query.toLowerCase()) ||
                            modelCompanies.getCompanyDescription().toLowerCase().contains(query.toLowerCase())){

                        companiesList.add(modelCompanies);
                    }

                    adapterCompanies = new AdapterCompanies(getActivity(), companiesList);
                    adapterCompanies.notifyDataSetChanged();
                    companies_recyclerView.setAdapter(adapterCompanies);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_companies, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query.trim())){

                    searchCompany(query);
                }else {
                    getAllCompanies();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText.trim())){
                    searchCompany(newText);
                }else {
                    getAllCompanies();
                }

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
