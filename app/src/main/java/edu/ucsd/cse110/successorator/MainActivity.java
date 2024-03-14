package edu.ucsd.cse110.successorator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.itemList.ItemListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.PendingListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.RecurringListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.TomorrowListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    public DrawerLayout drawerLayout;


    private SharedPreferences sharedPreferences;
    private String focusMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(view.getRoot());

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        sharedPreferences = getApplicationContext().getSharedPreferences("formatted_date", Context.MODE_PRIVATE);


        NavigationView navigationView = findViewById(R.id.nav_view);



        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            int id = item.getItemId();
            Fragment currFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (id == R.id.home){
                editor.putString("focus_mode", "HOME");
            } else if (id == R.id.school){
                editor.putString("focus_mode", "SCHOOL");
            } else if (id == R.id.errands){
                editor.putString("focus_mode", "ERRAND");
            } else if (id == R.id.work){
                editor.putString("focus_mode", "WORK");
            } else if (id == R.id.cancel_focus){
                editor.putString("focus_mode", "NONE");
            }

            editor.apply();

            swapFragments(currFrag.getClass());
            // Close the drawer after an item is selected
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dropdown_menu, menu);
        //getMenuInflater().inflate(R.menu.hamburger_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.today) {
            swapFragments(ItemListFragment.class);
            return true;
        } else if (id == R.id.Tomorrow) {
            swapFragments(TomorrowListFragment.class);
            return true;
        } else if (id == R.id.Recurring) {
            swapFragments(RecurringListFragment.class);
            return true;
        } else if (id == R.id.Pending) {
            swapFragments(PendingListFragment.class);
            return true;
        }

        // Continue with other options...
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void swapFragments(Class<? extends Fragment> fragmentClass) {
        Fragment fragment = null;
        try {
            // Assuming the fragment has a public no-args constructor
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            // Get transaction
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            // Get current fragment
            Fragment currFrag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currFrag != null) {
                fragTrans.detach(currFrag);
            }

            // Add the new fragment to the container if it's not already added
            if (!fragment.isAdded()) {
                fragTrans.add(R.id.fragment_container, fragment);
            }

            fragTrans.attach(fragment)
                    .commit();
        }
    }
}
