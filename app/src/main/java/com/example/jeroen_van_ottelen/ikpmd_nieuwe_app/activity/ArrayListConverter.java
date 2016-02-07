package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

/**
 * Created by jeroen_van_ottelen on 07-02-16.
 */
public class ArrayListConverter
{

    private static ArrayListConverter _instance = null;

    private ArrayListConverter () {}

    private synchronized static void createInstance () {
        if (_instance == null) _instance = new ArrayListConverter ();
    }

    public static ArrayListConverter getInstance () {
        if (_instance == null) createInstance();
        return _instance;
    }

}
