package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLink;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.fragment.FeedFragment;
import com.cursoandroid.gabriel.instagramclone.fragment.PerfilFragment;
import com.cursoandroid.gabriel.instagramclone.fragment.PesquisaFragment;
import com.cursoandroid.gabriel.instagramclone.fragment.PostagemFragment;
import com.cursoandroid.gabriel.instagramclone.helper.KeepStateNavigator;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderPicasso;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final Fragment feedFragment = new FeedFragment();
    final Fragment pesquisaFragment = new PesquisaFragment();
    final Fragment postagemFragment = new PostagemFragment();
    final Fragment perfilFragment = new PerfilFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = feedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        fm.beginTransaction().add(R.id.nav_host_fragment,feedFragment, "1").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, perfilFragment, "4").hide(perfilFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, postagemFragment, "2").hide(postagemFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, pesquisaFragment, "2").hide(pesquisaFragment).commit();


        configurarBottomNavigationView();

    }

    private void configurarBottomNavigationView() {

        //BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        //bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.black));

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.nav_view);
        bottomNavigationBar.setBarBackgroundColor(R.color.black);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_home_24, "Feed"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_search_24, "Pesquisa"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_add_box_24, "Postagem"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_person_24, "Perfil"))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        fm.beginTransaction().hide(active).show(feedFragment).commit();
                        active = feedFragment;
                        return;

                    case 1:
                        fm.beginTransaction().hide(active).show(pesquisaFragment).commit();
                        active = pesquisaFragment;
                        return;

                    case 2:
                        fm.beginTransaction().hide(active).show(postagemFragment).commit();
                        active = postagemFragment;
                        return;

                    case 3:
                        fm.beginTransaction().hide(active).show(perfilFragment).commit();
                        active = perfilFragment;

                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });





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
        return false;
    }
}