package cf.snowberry.smartcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cf.snowberry.smartcustomer.Adapters.AdapterCompanies;
import cf.snowberry.smartcustomer.Adapters.AdapterPost;
import cf.snowberry.smartcustomer.Adapters.AdapterProductsAndServices;
import cf.snowberry.smartcustomer.Models.ModelCompanies;
import cf.snowberry.smartcustomer.Models.ModelPost;
import cf.snowberry.smartcustomer.Models.ModelProductsAndServices;

public class ProductAndServicesActivity extends AppCompatActivity {

    RecyclerView productAndServicesRv;
    ArrayList<ModelProductsAndServices> modelProductsAndServicesList;
    AdapterProductsAndServices adapterProductsAndServices;

    String thisCompanyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_and_services);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Products And Services");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);




        productAndServicesRv = findViewById(R.id.productAndServicesRv);

        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        productAndServicesRv.setLayoutManager(manager);

        Intent intent = getIntent();
        thisCompanyId = intent.getStringExtra("companyId");

        modelProductsAndServicesList = new ArrayList<>();

        loadProducts();
    }

    private void loadProducts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProductsAndServices");

        Query query = reference.orderByChild("companyId").equalTo(thisCompanyId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelProductsAndServicesList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelProductsAndServices modelProductsAndServices = ds.getValue(ModelProductsAndServices.class);

                    modelProductsAndServicesList.add(modelProductsAndServices);
                    Collections.reverse(modelProductsAndServicesList);
                    adapterProductsAndServices = new AdapterProductsAndServices(ProductAndServicesActivity.this,modelProductsAndServicesList);
                    productAndServicesRv.setAdapter(adapterProductsAndServices);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductAndServicesActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProduct(final String query) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProductsAndServices");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelProductsAndServicesList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelProductsAndServices modelProductsAndServices = ds.getValue(ModelProductsAndServices.class);

                    if (modelProductsAndServices.getProductName().toLowerCase().contains(query.toLowerCase()) ||
                            modelProductsAndServices.getProductDescription().toLowerCase().contains(query.toLowerCase())){

                        modelProductsAndServicesList.add(modelProductsAndServices);
                    }

                    adapterProductsAndServices = new AdapterProductsAndServices(ProductAndServicesActivity.this, modelProductsAndServicesList);
                    adapterProductsAndServices.notifyDataSetChanged();
                    productAndServicesRv.setAdapter(adapterProductsAndServices);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_products_and_services, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query.trim())) {
                    searchProduct(query);
                } else {
                    loadProducts();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText.trim())) {
                    searchProduct(newText);
                } else {
                    loadProducts();
                }

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
