package com.tucker.nat20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tucker.nat20.adapters.SpellListAdapter;
import com.tucker.nat20.utils.SpellListUtils;

public class MainMenu extends AppCompatActivity
        implements SpellListAdapter.OnSpellClickListener,
        LoaderManager.LoaderCallbacks<String>,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainMenu.class.getSimpleName();
    private final static int LOADER_ID = 0;

    private RecyclerView spellListRV;
    private SpellListAdapter spellListAdapter;

    private ProgressBar loadingIndicator;
    private TextView loadingErrorMessage;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loadingIndicator = findViewById(R.id.pb_loading_indicator);
        loadingErrorMessage = findViewById(R.id.tv_loading_error_message);

        spellListRV = (RecyclerView)findViewById(R.id.rv_rest_results);

        spellListRV.setLayoutManager(new LinearLayoutManager(this));
        spellListRV.setHasFixedSize(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = findViewById(R.id.nv_navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        spellListAdapter = new SpellListAdapter(this);
        spellListRV.setAdapter(spellListAdapter);

        loadingIndicator.setVisibility(View.VISIBLE);


        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saved_spells:
                drawerLayout.closeDrawers();
                Intent savedIntent = new Intent(this, SavedSpellsActivity.class);
                startActivity(savedIntent);
                return true;
            case R.id.action_external_link:
                drawerLayout.closeDrawers();
                Uri uri = Uri.parse("http://engl393-dnd5th.wikia.com/wiki/D%26D_5th_Edition_Wiki");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(webIntent);
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onSpellClick(String url)
    {
        Intent intent = new Intent(this, SpellDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new SpellLoader(this);
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "Got Spell List from Loader");
        loadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            SpellListUtils.SpellResponse spellResponse = SpellListUtils.parseResults(data);
            spellListAdapter.updateSearchResults(spellResponse);
            loadingErrorMessage.setVisibility(View.INVISIBLE);
            spellListRV.setVisibility(View.VISIBLE);
        } else {
            spellListRV.setVisibility(View.INVISIBLE);
            loadingErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) { }
}
