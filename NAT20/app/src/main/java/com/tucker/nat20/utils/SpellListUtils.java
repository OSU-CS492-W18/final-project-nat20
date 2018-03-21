package com.tucker.nat20.utils;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by eli on 2/7/2018.
 */

public class SpellListUtils
{
    final static String BASE_URL = "http://www.dnd5eapi.co/api/";

    public static class SpellResult
    {
        public String name;
        public String url;

        public SpellResult(String name, String url)
        {
            this.name = name;
            this.url = url;
        }
    }

    public static class SpellResponse
    {
        public int count;
        public ArrayList<SpellResult> spellResults;
    }


    public static String buildRestUrl(String restUrl)
    {
        return Uri.parse(BASE_URL).buildUpon().appendPath(restUrl).build().toString();
    }


    public static SpellResponse parseResults(String restResultsJSON)
    {
        try{
            JSONObject restResultsObj = new JSONObject(restResultsJSON);

            SpellResponse res = new SpellResponse();
            res.spellResults = new ArrayList<>();
            res.count = restResultsObj.getInt("count");
            for (int i = 0; i < res.count; i++)
            {
                JSONObject item = (JSONObject) restResultsObj.getJSONArray("results").get(i);
                res.spellResults.add(new SpellResult(item.getString("name"), item.getString("url")));
            }
            return res;
        } catch (JSONException e) {
            return null;
        }
    }
}
