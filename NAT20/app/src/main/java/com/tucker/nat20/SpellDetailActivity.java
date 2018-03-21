package com.tucker.nat20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tucker.nat20.database.SpellContract;
import com.tucker.nat20.database.SpellDBHelper;
import com.tucker.nat20.utils.NetworkUtils;
import com.tucker.nat20.utils.SpellDetailUtils;
import com.tucker.nat20.utils.SpellListUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eli on 2/13/2018.
 */

public class SpellDetailActivity extends AppCompatActivity
{
    private TextView tvName;
    private TextView tvDesc;
    private TextView tvHigherLevel;
    private TextView tvPage;
    private TextView tvRange;
    private TextView tvComponents;
    private TextView tvMaterial;
    private TextView tvRitual;
    private TextView tvDuration;
    private TextView tvConcentration;
    private TextView tvCastTime;
    private TextView tvLevel;
    private TextView tvSchool;
    private TextView tvClass;

    private String url;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_detail);

        tvName = (TextView)findViewById(R.id.tv_name);
        tvDesc = (TextView)findViewById(R.id.tv_desc);
        tvHigherLevel = (TextView)findViewById(R.id.tv_higher_level);
        tvPage = (TextView)findViewById(R.id.tv_page);
        tvRange = (TextView)findViewById(R.id.tv_range);

        tvCastTime = (TextView)findViewById(R.id.tv_casting_time);

        tvDesc.setMovementMethod(new ScrollingMovementMethod());
        tvHigherLevel.setMovementMethod(new ScrollingMovementMethod());

        SpellDBHelper dbHelper = new SpellDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("url"))
        {
            url = intent.getStringExtra("url");
            doRestCall(url);
        }
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spell_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<SpellListUtils.SpellResult> spellsInDB = getAllSavedSpellsFromDB();
                String spellName = tvName.getText().toString();
                if (!spellsInDB.contains(new SpellListUtils.SpellResult(spellName, url))) {
                    mDB.execSQL(
                            "INSERT INTO " + SpellContract.SavedSpells.TABLE_NAME + " (" +
                            SpellContract.SavedSpells.COLUMN_NAME + ", " + SpellContract.SavedSpells.COLUMN_URL +
                            ") VALUES ('" + spellName + "', '" + url + "');");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    private void doRestCall(String restURL)
    {
        new RestCall().execute(restURL);
    }


    public class RestCall extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... urls)
        {
            String restCallURL = urls[0];
            String results = null;

            try {
                results = NetworkUtils.doHttpGet(restCallURL);
            } catch (IOException e){
                e.printStackTrace();
            }
            return results;
        }


        @Override
        protected void onPostExecute(String s)
        {
            StringBuilder desc = new StringBuilder();
            StringBuilder higherLevel = new StringBuilder();
            SpellDetailUtils.SpellDetail spellDetailResponse = SpellDetailUtils.parseResults(s);
            for (int i = 0; i < spellDetailResponse.desc.size(); i++)
            {
                if (i > 0)
                    desc.append("\n");
                desc.append(spellDetailResponse.desc.get(i));
            }
            for (int i = 0; i < spellDetailResponse.higherLevel.size(); i++)
            {
                if (i > 0)
                    higherLevel.append("\n");
                higherLevel.append(spellDetailResponse.higherLevel.get(i));
            }
            tvName.setText(spellDetailResponse.name);
            tvDesc.setText(desc.toString());
            tvHigherLevel.setText(higherLevel.toString());
            tvPage.setText(spellDetailResponse.page);
            tvRange.setText(spellDetailResponse.range);
            //tvComponents.setText(spellDetailResponse.components.get(0));
            //tvMaterial.setText(spellDetailResponse.material);
            //tvDuration.setText(spellDetailResponse.duration);
            tvCastTime.setText(spellDetailResponse.castingTime);
            //tvLevel.setText(spellDetailResponse.level);
            //tvSchool.setText(spellDetailResponse.school);
            //tvClass.setText(spellDetailResponse.classes.get(0));
        }
    }
}
