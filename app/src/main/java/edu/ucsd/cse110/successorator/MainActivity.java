package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.itemList.ItemListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.ReccurringListFragment;
import edu.ucsd.cse110.successorator.ui.itemList.TomorrowListFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(view.getRoot());

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

        if (id == R.id.action_today) {
            swapFragments(ItemListFragment.class);
            return true;
        } else if (id == R.id.action_tomorrow) {
            swapFragments(TomorrowListFragment.class);
            return true;
        } else if (id == R.id.action_reccurring) {
            swapFragments(ReccurringListFragment.class);
            return true;
        }
        // Continue with other options...

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // Optional: Add transaction to back stack
                    .commit();
        }
    }
}
