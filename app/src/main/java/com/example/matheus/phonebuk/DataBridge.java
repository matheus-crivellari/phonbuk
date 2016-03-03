package com.example.matheus.phonebuk;

/**
 * Singleton for bridging the shared info and data among all activities.
 */
public class DataBridge {

    /* Private Properties */
    private static DataBridge instance = new DataBridge();
    private static DatabaseHelper database = null;

    /**
     * Constructor
     * DataBridge constructor.
     */
    private DataBridge() {
        // Do Nothing.
    }

    /* Getters */
    public static DataBridge getInstance() { return instance; }
    public static DatabaseHelper getDatabaseHelper(){ return database; }
    public static DatabaseHelper getDataBase(){ return database; }

    /*Setters*/
    public static void setDatabaseHelper(DatabaseHelper db){
        database = db;
    }
}
