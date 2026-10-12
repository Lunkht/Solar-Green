package com.solargreen.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.solargreen.app.ui.account.AccountFragment;
import com.solargreen.app.ui.catalog.CatalogFragment;
import com.solargreen.app.ui.contact.ContactFragment;
import com.solargreen.app.ui.devices.DevicesFragment;
import com.solargreen.app.ui.dialogs.ConnectionBottomSheet;
import com.solargreen.app.ui.home.HomeFragment;
import com.solargreen.app.ui.settings.SettingsFragment;
import com.solargreen.app.ui.stats.StatsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String KEY_TAB = "tab";
    public static final String EXTRA_TAB = "extra_tab";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNav = findViewById(R.id.bottom_nav);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_stats) {
                showFragment(new StatsFragment(), false);
                return true;
            } else if (id == R.id.nav_devices) {
                showFragment(new DevicesFragment(), false);
                return true;
            } else if (id == R.id.nav_settings) {
                showFragment(new SettingsFragment(), false);
                return true;
            } else {
                showFragment(new HomeFragment(), false);
                return true;
            }
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
        handleTab(getIntent());
    }

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            goToTab(R.id.nav_home);
        } else if (id == R.id.nav_catalog) {
            openCatalog();
        } else if (id == R.id.nav_connection) {
            new ConnectionBottomSheet().show(getSupportFragmentManager(), "conn_sheet");
        } else if (id == R.id.nav_stats) {
            goToTab(R.id.nav_stats);
        } else if (id == R.id.nav_devices) {
            goToTab(R.id.nav_devices);
        } else if (id == R.id.nav_contact) {
            openContact();
        } else if (id == R.id.nav_settings) {
            openSettings();
        }

        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void openCatalog() {
        showFragment(new CatalogFragment(), true);
    }

    public void openContact() {
        showFragment(new ContactFragment(), true);
    }

    public void openSettings() {
        showFragment(new SettingsFragment(), true);
    }

    public void openAccount() {
        showFragment(new AccountFragment(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleTab(intent);
    }

    private void handleTab(Intent intent) {
        if (bottomNav != null && intent != null && intent.hasExtra(EXTRA_TAB)) {
            bottomNav.setSelectedItemId(intent.getIntExtra(EXTRA_TAB, R.id.nav_home));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TAB, bottomNav.getSelectedItemId());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int tab = savedInstanceState.getInt(KEY_TAB, R.id.nav_home);
            bottomNav.setSelectedItemId(tab);
        }
    }

    public void goToTab(int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }

    private void showFragment(Fragment fragment, boolean addToBackStack) {
        if (!addToBackStack) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (bottomNav != null && bottomNav.getSelectedItemId() != R.id.nav_home) {
            goToTab(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }
}