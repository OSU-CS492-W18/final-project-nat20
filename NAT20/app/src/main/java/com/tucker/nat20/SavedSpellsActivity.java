package com.tucker.nat20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tucker.nat20.adapters.SavedSpellAdapter;
import com.tucker.nat20.database.SpellContract;
import com.tucker.nat20.database.SpellDBHelper;
import com.tucker.nat20.utils.SpellListUtils;

import java.util.ArrayList;

/**
 * Created by eli on 3/20/2018.
 */

public class SavedSpellsActivity extends AppCompatActivity
        implements SavedSpellAdapter.OnSpellItemClickListener {

    private SQLiteDatabase mDB;

    private RecyclerView savedSpellsRV;
    private SavedSpellAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_spells);

        adapter = new SavedSpellAdapter(this);

        savedSpellsRV = (RecyclerView) findViewById(R.id.rv_saved_spells);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        savedSpellsRV.setLayoutManager(linearLayoutManager);
        savedSpellsRV.setHasFixedSize(true);
        savedSpellsRV.setAdapter(adapter);

        SpellDBHelper dbHelper = new SpellDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        adapter.updateResults(getAllSavedSpellsFromDB());
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    private ArrayList<SpellListUtils.SpellResult> getAllSavedSpellsFromDB() {
        Cursor cursor = mDB.query(
                SpellContract.SavedSpells.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SpellContract.SavedSpells.COLUMN_TIMESTAMP
        );

        ArrayList<SpellListUtils.SpellResult> savedResults = new ArrayList<>();
        while (cursor.moveToNext()) {
            SpellListUtils.SpellResult result = new SpellListUtils.SpellResult(null, null);
            result.name = cursor.getString(
                    cursor.getColumnIndex(SpellContract.SavedSpells.COLUMN_NAME)
            );
            result.url = cursor.getString(
                    cursor.getColumnIndex(SpellContract.SavedSpells.COLUMN_URL)
            );
            savedResults.add(result);
        }
        cursor.close();
        return savedResults;
    }


    @Override
    public void onSpellItemClick(String url) {
        Intent intent = new Intent(this, SpellDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
