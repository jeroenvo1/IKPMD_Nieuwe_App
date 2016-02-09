package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.Custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jeroen_van_ottelen on 08-02-16.
 * Class aan de hand van de volgende class gemaakt:
 * https://github.com/kcochibili/TinyDB--Android-Shared-Preferences-Turbo
 */
public class CustomSharedPreferences
{

    private SharedPreferences sharedPreferences;

    public CustomSharedPreferences(Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putString(String key, String value) {
        checkForNullKey(key);
        checkForNullValue(value);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key)
    {
        return sharedPreferences.getString(key, null);
    }

    public void putSubject(String name, Subject subject)
    {
        ArrayList<Subject> subjectList = getListObject(name);
        subjectList.add(subject);
        putListObject(name, subjectList);
    }

    public ArrayList<Subject> getListObject(String key)
    {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Subject> objects =  new ArrayList<>();

        for(String jObjString : objStrings){
            Subject value  = gson.fromJson(jObjString,  Subject.class);
            objects.add(value);
        }
        return objects;
    }

    public void putListObject(String key, ArrayList<Subject> objArray)
    {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for(Object obj : objArray){
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    public void putListString(String key, ArrayList<String> stringList)
    {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        sharedPreferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public ArrayList<String> getListString(String key)
    {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(sharedPreferences.getString(key, ""), "‚‗‚")));
    }

    public Boolean isListEmpty(String name)
    {

        if(getListObject(name).size() != 0)
        {
            return true;
        } else
        {
            return false;
        }

    }

    public void clear()
    {
        sharedPreferences.edit().clear().apply();
    }

    public void checkForNullKey(String key)
    {
        if (key == null){
            throw new NullPointerException();
        }
    }

    public void checkForNullValue(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
    }
}
