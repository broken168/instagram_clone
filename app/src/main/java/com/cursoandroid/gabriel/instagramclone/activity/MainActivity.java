package com.cursoandroid.gabriel.instagramclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.helper.NavigationExtensionsKt;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity {

    private LiveData<NavController> currentNavController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        configurarBottomNavigationView();

    }

    private void configurarBottomNavigationView() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        List<Integer> navGraphsIds = Arrays.asList(R.navigation.feed, R.navigation.search, R.navigation.post, R.navigation.profile);

        LiveData<NavController> controller = NavigationExtensionsKt.setupWithNavController(bottomNavigationView,
                navGraphsIds, 
                getSupportFragmentManager(), 
                R.id.nav_host_container, 
                getIntent());

        controller.observe(this, new Observer() {
            public void onChanged(Object var1) {
                this.onChanged((NavController)var1);
            }

            public final void onChanged(NavController navController) {
                NavigationUI.setupActionBarWithNavController(MainActivity.this, navController);
            }
        });
        this.currentNavController = controller;


        /*
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        navView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_search, R.id.navigation_post, R.id.navigation_profile)
                .build();

        NavOptions animationOptions = new NavOptions.Builder().setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim).build();


        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


         */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_sair){
            finish();
            deslogarUsuario();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void deslogarUsuario(){
        new MySharedPreferences(this).removeToken();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (currentNavController != null) {
            NavController navController = currentNavController.getValue();
            if (navController != null) {
                return navController.navigateUp();
            }
        }
        return false;
    }

}