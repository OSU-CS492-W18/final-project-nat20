package com.tucker.nat20.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by eli on 2/13/2018.
 */

public class SpellDetailUtils
{
    public static class SpellDetail
    {
        public String name;
        public ArrayList<String> desc;
        public ArrayList<String> higherLevel;
        public String page;
        public String range;
        public ArrayList<String> components;
        public String material;
        public Boolean ritual;
        public String duration;
        public Boolean concentration;
        public String castingTime;
        public String level;
        public String school;
        public ArrayList<String> classes;
    }


    public static SpellDetail parseResults(String restResultsJSON)
    {
        try {
            SpellDetail spellDetail = new SpellDetail();
            spellDetail.desc = new ArrayList<>();
            spellDetail.higherLevel = new ArrayList<>();
            spellDetail.components = new ArrayList<>();
            spellDetail.classes = new ArrayList<>();
            JSONObject spell = new JSONObject(restResultsJSON);
            spellDetail.name = spell.getString("name");
            JSONArray descArr = spell.getJSONArray("desc");
            if (spell.has("higher_level"))
            {
                JSONArray higherLevelArr = spell.getJSONArray("higher_level");

                for (int i = 0; i < higherLevelArr.length(); i++)
                {
                    spellDetail.higherLevel.add(higherLevelArr.get(i).toString());
                }
            }
            spellDetail.page = spell.getString("page");
            spellDetail.range = spell.getString("range");
            JSONArray componentsArr = spell.getJSONArray("components");
            if (spell.has("material"))
                spellDetail.material = spell.getString("material");
            spellDetail.ritual = spell.getString("ritual").equals("yes");
            spellDetail.duration = spell.getString("duration");
            spellDetail.concentration = spell.getString("concentration").equals("yes");
            spellDetail.castingTime = spell.getString("casting_time");
            spellDetail.level = spell.getString("level");
            JSONObject schoolObject = spell.getJSONObject("school");
            spellDetail.school = schoolObject.getString("name");
            JSONArray classesArr = spell.getJSONArray("classes");
            for (int i = 0; i < descArr.length(); i++)
            {
                spellDetail.desc.add(descArr.get(i).toString());
            }
            for (int i = 0; i < componentsArr.length(); i++)
            {
                spellDetail.components.add(componentsArr.get(i).toString());
            }
            for (int i = 0; i < classesArr.length(); i++)
            {
                spellDetail.classes.add(classesArr.get(i).toString());
            }
            return spellDetail;
        } catch (JSONException e) {
            return null;
        }
    }
}
