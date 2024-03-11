package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.itemList.ItemListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.PendingListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.RecurringListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.TomorrowListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

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

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dropdown_menu, menu);
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
