package com.tucker.nat20;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;

import com.tucker.nat20.utils.NetworkUtils;
import com.tucker.nat20.utils.SpellListUtils;

import java.io.IOException;

/**
 * Created by eli on 3/20/2018.
 */

public class SpellLoader extends AsyncTaskLoader<String> {
    private final static String TAG = SpellLoader.class.getSimpleName();
    private final static String SPELL_URL = "spells";

    private String cachedSpellJSON;

    public SpellLoader(Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading() {
        if (cachedSpellJSON != null) {
            Log.d(TAG, "Using cached spell data");
            deliverResult(cachedSpellJSON);
        } else {
            forceLoad();
        }
    }


    @Nullable
    @Override
    public String loadInBackground()
    {
        String spellJSON = null;
        Log.d(TAG, "Loading spell list");
        try {
            spellJSON = NetworkUtils.doHttpGet(SpellListUtils.buildRestUrl(SPELL_URL));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return spellJSON;
    }


    @Override
    public void deliverResult(@Nullable String data)
    {
        cachedSpellJSON = data;
        super.deliverResult(data);
    }


    public String getCachedResults()
    {
        return cachedSpellJSON;
    }
}
