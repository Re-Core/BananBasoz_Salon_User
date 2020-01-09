package com.recore.bananbasozsalon.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.recore.bananbasozsalon.Fragment.AppointmentFragment;
import com.recore.bananbasozsalon.Fragment.CartFragment;
import com.recore.bananbasozsalon.Fragment.CategorieFragment;
import com.recore.bananbasozsalon.Fragment.HomeFragment;
import com.recore.bananbasozsalon.Fragment.ProfileFragment;
import com.recore.bananbasozsalon.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                            break;

                        case R.id.menu_categories:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategorieFragment()).commit();
                            break;

                        case R.id.menu_new_appointment:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AppointmentFragment()).commit();
                            break;

                        case R.id.menu_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                            break;

                        case R.id.menu_bag:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CartFragment()).commit();
                            break;
                    }
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }
}
