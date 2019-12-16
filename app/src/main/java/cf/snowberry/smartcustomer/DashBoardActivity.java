package cf.snowberry.smartcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cf.snowberry.smartcustomer.Fragments.SearchFragment;
import cf.snowberry.smartcustomer.Fragments.FeedFragment;
import cf.snowberry.smartcustomer.Fragments.FrequentlyListFragment;

public class DashBoardActivity extends AppCompatActivity {

    ActionBar actionBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        actionBar = getSupportActionBar();

        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        actionBar.setTitle("Feed");
        FeedFragment fragment2 = new FeedFragment();
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.replace(R.id.content, fragment2,"");
        ft2.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){

                        case R.id.nav_companies:

                            actionBar.setTitle("Companies");
                            SearchFragment fragment1 = new SearchFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1,"");
                            ft1.commit();
                            return true;

                        case R.id.nav_feed:

                            actionBar.setTitle("Feed");
                            FeedFragment fragment2 = new FeedFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2,"");
                            ft2.commit();
                            return true;

                        case R.id.nav_frequent_list:

                            actionBar.setTitle("Frequently List");
                            FrequentlyListFragment fragment3 = new FrequentlyListFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3,"");
                            ft3.commit();
                            return true;

                    }

                    return false;
                }
            };



    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    private void checkUserStatus() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){

        }else {
            startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
            finish();
        }
    }
}
