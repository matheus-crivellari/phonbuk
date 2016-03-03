package com.example.matheus.phonebuk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //private static String DB_PATH = "//data/data/com.example.matheus.phonbuk/databases/";
    //private static String DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+
    private static String DB_PATH;
    private static String DB_NAME = "bd";

    private SQLiteDatabase database;
    private final Context context;

    /**
     * DatabaseHelper for easing the connection and handling of the SQLite database.
     * Takes and keeps a reference of the context passed in order to access the application assets and resources.
     * @param context The base context passed.
     */
    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,1);
        StringBuilder strb = new StringBuilder(context.getFilesDir().getAbsolutePath());
        strb = strb.replace(strb.indexOf("files"),strb.indexOf("files")+"files".length(),"databases").append('/');
        DB_PATH = strb.toString();

        this.context = context;
    }

    /**
     * Creates an empty database if it doesn't exist yet and overwrites it with the application's database.
     */
    public void createDatabase() throws IOException {
        boolean dbExists = checkDataBase();

        if(!dbExists){
            // Creates an empty database into the default system path to be overwritten by the application.
            this.getReadableDatabase();
            copyDataBase();
        }
    }

    /**
     * Check if the database already exists to avoid copying the file again overwriting the current database usage.
     * @return Returns either true if the database exists or false if it doesn't.
     */
    private boolean checkDataBase() {
        SQLiteDatabase check = null;
        try{
            String path = DB_PATH + DB_NAME;
            check = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            // database doesn't exist.
        }

        if(check != null){
            check.close();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Copies application's database from local assets-folder to the just crated database in system default folder from where it can be accessed and handled.
     * Done by bit transfer.
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        InputStream input = context.getAssets().open(DB_NAME);
        String path = DB_PATH + DB_NAME;

        OutputStream output = new FileOutputStream(path);

        byte[] buffer = new byte[1024];
        int length;

        int times = 0;
        while((length = input.read(buffer))>0){
            output.write(buffer,0,length);
            times++;
        }

        output.flush();
        output.close();
        input.close();
    }

    /**
     * Opens the database located at DB_PATH by the name of DB_NAME.
     */
    public void openDataBase(){
        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * Synchronized method for closing the current open database.
     * Necessary for the virtual machine to handle the concurrent processing.
     */
    public synchronized void close(){
        if(database != null){
            database.close();
        }
        super.close();
    }

    /**
     * Inserts a new Category to the SQLite Database.
     * @param name    Represents the category's name.
     * @param colorId Represents the category's color id.
     * @param systemProtected Represents if the category is either protected from editing / deletion by the system (1) or not (0).
     */
    public void insertCategory(String name, int colorId, int systemProtected){
        String sql = "insert into category(name,id_color,system_category) values ('" + name +"',"+ colorId +","+ systemProtected +"); commit;";
        openDataBase();

        getReadableDatabase().execSQL(sql);
    }

    /**
     * Updates a Category represented by categoryId in the database.
     * @param name    Represents the new category's name.
     * @param colorId Represents new the category's color id.
     * @param systemProtected Represents the new state telling if the category is either protected from editing / deletion by the system (1) or not (0).
     * @param categoryId Represents the desired Category's categoryId which will be updated.
     */
    public void updateCategory(String name, int colorId, int systemProtected, int categoryId){
        String sql = "update category set name='"+ name +"'," +
                "id_color="+ colorId +"," +
                "system_category="+ systemProtected +" " +
                "where _id="+ categoryId +"; commit;";

        openDataBase();
        getReadableDatabase().execSQL(sql);
    }

    /**
     * Deletes a Category represented by categoryId from the database.
     * @param categoryId Represents the desired Category's categoryId which will be deleted.
     */
    public void deleteCategory(int categoryId){
        String sql = "delete from category where _id="+ categoryId +"; commit;";

        openDataBase();
        getReadableDatabase().execSQL(sql);
    }

    /**
     * Returns the list of contacts parsed to @see {@link com.example.matheus.phonebuk.Contact}s class instances list filtered by a category name.
     * @param category A string containing the name of the desired category to have its contacts listed.
     * @return ArrayList<Contact> A list of Contacts or null.
     */
    public ArrayList<Contact> getContatcsByCategoryName(String category){
        String sql = "select con._id, con.name as NAME, con.birthdate AS BIRTHDATE, con.adress as ADDRESS, cat.name as CATEGORY, phones.number as NUMBER, con.image_path as IMAGE " +
                "from contact as con, category as cat, con_cat as concat, phone_numbers as phones " +
                "where concat.contact_id = con._id and " +
                "concat.category_id = cat._id and " +
                "phones.contact_id = con._id and " +
                "cat.name = '"+category+"' " +
                "group by con._id;";

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        openDataBase();

        try{
            Cursor cursor = getReadableDatabase().rawQuery(sql,null);

            if(cursor.moveToFirst()){
                do {
                    Contact cc = new Contact(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                            cursor.getString(cursor.getColumnIndex("NAME")),
                            cursor.getString(cursor.getColumnIndex("BIRTHDATE")),
                            cursor.getString(cursor.getColumnIndex("ADDRESS")),
                            cursor.getString(cursor.getColumnIndex("IMAGE"))
                    );
                    contacts.add(cc);
                }while(cursor.moveToNext());

                cursor.close();
                return contacts;
            }else{
                cursor.close();
                return null;
            }

        }catch(Exception e){
            return null;
        }
    }

    /**
     * Returns the list of categories parsed to a @see {@link com.example.matheus.phonebuk.ContactCategory} class instances list.
     * @return ArrayList<ContactCategory> A list of ContactCategory or null.
     */
    public ArrayList<ContactCategory> getCategories(){
        File file = new File(DB_PATH+DB_NAME);

        ArrayList<ContactCategory> categories = new ArrayList<ContactCategory>();
        openDataBase();

        try{
            Cursor cursor = getReadableDatabase().rawQuery("select * from category",null);

            if(cursor.moveToFirst()){
                do{
                    categories.add(new ContactCategory(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                            cursor.getString(cursor.getColumnIndex("name")),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_color"))),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("system_category")))
                    ));
                }while(cursor.moveToNext());

                cursor.close();

                return categories;
            }else{
                cursor.close();

                return null;
            }
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Inserts a new Contact to the SQLite database.
     * @param name      Represents the Contact's name.
     * @param address   Represents the Contact's address.
     * @param birthDate Represents the Contact's birth date.
     * @param imgPath   Represents the Contact's avatar image path.
     */
    public void addNewContact(String name, String phone, String email, String address, String birthDate, String imgPath){

        insertContact(name, address, birthDate, imgPath);
        int id = getLastContactId();

        insertTelephone(phone, id);
        insertMail(email, id);
        insertConCat(id, 1);
    }

    /**
     * Deletes a Contact represented by id from the database.
     * @param id Represents the desired Contact's id which will be deleted.
     */
    public void removeContact(int id){
        //TO-DO
    }

    /**
     * Add a Contact to the CONTACT table.
     * @param name      Contact's name.
     * @param address   Contact's address.
     * @param birthDate Contact's birth date.
     * @param imgPath   Contact's image path.
     */
    public void insertContact(String name, String address, String birthDate, String imgPath){
        String sql = "INSERT INTO contact " +
                "(name,adress,birthdate,image_path) " +
                "VALUES " +
                "('"+name+"','"+address+"','"+birthDate+"','"+imgPath+"'); " +
                "commit;";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            // TO-DO
        }
    }

    /**
     * Returns the last Contact's id added to the database.
     * @return An integer representing the last Contact's id added to the database.
     */
    public int getLastContactId(){
        String sql = "SELECT _id, name " +
                "FROM contact " +
                "ORDER BY _id DESC LIMIT 1";

        try{
            Cursor cursor = getReadableDatabase().rawQuery(sql,null);
            if(cursor.moveToFirst()){
                int lastId  = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                return lastId;

            }else{
                return 0;
            }
        }catch(Exception e){
            return 0;
        }
    }

    /**
     * Inserts a new phone number and links it to the Contact represented by the contactId via foreign keying.
     * @param number    An integer representing the desired Contact's phone number.
     * @param contactId An integer representing the desired to-be-updated-Contact's id.
     */
    public void insertTelephone(String number, int contactId){
        String sql = "INSERT INTO phone_numbers " +
                "(number,id_type,contact_id) " +
                "VALUES " +
                "('"+number+"',1,"+contactId+");";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            // TO-DO
        }
    }

    /**
     * Inserts a new e-mail address and links it to the Contact represented by the contactId via foreign keying.
     * @param email     An integer representing the desired Contact's e-mail address.
     * @param contactId An integer representing the desired to-be-updated-Contact's id.
     */
    public void insertMail(String email, int contactId){
        String sql = "INSERT INTO email " +
                "(email,id_type,contact_id) " +
                "VALUES " +
                "('"+email+"',0,"+contactId+");";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            // TO-DO
        }
    }

    /**
     * Inserts a Contact's id and a Category's id to the CON_CAT TABLE.
     * This binds a Contact to a Category by their id.
     * @param contactId  An integer representing the Contact's id to link.
     * @param categoryId An integer representing the Category's id to be linked to.
     */
    public void insertConCat(int contactId, int categoryId){
        String sql = "INSERT INTO con_cat " +
                "(category_id, contact_id, date_added) " +
                "VALUES " +
                "("+categoryId+","+contactId+",(SELECT date('now')));";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            // TO-DO
        }
    }

    /**
     * Updates an existing Contact represented by contactId in the database.
     * @param name       A string representing the new desired Contact's name.
     * @param address    A string representing the new desired Contact's address.
     * @param birthDate  A string representing the new desired Contact's birth date.
     * @param imgPath    A string representing the new desired Contact's avatar image path.
     * @param phone      A string representing the new desired Contact's phone number.
     * @param telId      An integer representing the new desired Contact's phone number id.
     * @param email      A string representing the new desired Contact's e-mail address.
     * @param emailId    An integer representing the new desired Contact's e-mail address id.
     * @param contactId  An integer representing the desired to-be-updated-Contact's id.
     */
    public void updateExistingContact(String name, String address, String birthDate, String imgPath, String phone, int telId, String email, int emailId, int contactId) {
        updateTelephone(phone, telId);
        updateEmail(email, emailId);

        updateContact(name, address, birthDate, imgPath, contactId);
    }

    /**
     * Updates an existing Contact's phone number represented by telId in the database.
     * @param phone A string representing the new desired phone number.
     * @param telId An integer representing the desired to-be-updated-phone's id.
     */
    public void updateTelephone(String phone, int telId){
        String sql = "UPDATE phone_numbers " +
                "SET number = '"+phone+"' " +
                "WHERE _id = "+telId+"; commit;";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            // TO-DO
        }
    }

    /**
     * Updates an existing Contact's e-mail address represented by emailId in the database.
     * @param email   A string representing the new desired e-mail address.
     * @param emailId An integer representing the desired to-be-updated-email's id.
     */
    public void updateEmail(String email, int emailId){
        String sql = "update email " +
                "set email = '"+email+"' " +
                "where _id = "+emailId+"; commit;";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            //TO-DO
        }
    }

    /**
     * Updates an existing Contact's represented by contactId in the database.
     * @param name       A string representing the new desired Contact's name.
     * @param address    A string representing the new desired Contact's address.
     * @param birthDate  A string representing the new desired Contact's birth date.
     * @param imgPath    A string representing the new desired Contact's avatar image path.
     * @param contactId  An integer representing the desired to-be-updated-Contact's id.
     */
    public void updateContact(String name, String address, String birthDate, String imgPath, int contactId){
        String sql = "UPDATE contact " +
                "SET name='"+name+"',adress='"+address+"',birthdate='"+birthDate+"',image_path='"+imgPath+"' " +
                "WHERE _id = "+contactId+"; commit;";

        try{
            getReadableDatabase().execSQL(sql);
        }catch(Exception e){
            // TO-DO
        }
    }

    /**
     * Returns specific details from a Contact represented by id.
     * @param id An integer representing the desired Contact.
     * @return Returns either a filled Contact instance if the contact exists or null.
     */
    public ContactDetail getContactDetail(int id){
        ContactDetail contact = null;

        String sql = "select con._id, con.name as NAME, con.birthdate AS BIRTHDATE, con.adress as ADDRESS, cat.name as CATEGORY, phones._id as TELID, phones.number as NUMBER, emails._id as EMAILID, emails.email as EMAIL, con.image_path as IMAGE " +
                "from contact as con, category as cat, con_cat as concat, phone_numbers as phones, email as emails " +
                "where concat.contact_id = con._id and " +
                "emails.contact_id = con._id and " +
                "concat.category_id = cat._id and " +
                "phones.contact_id = con._id and " +
                "con._id = "+id+" " +
                "group by con._id;";
        openDataBase();

        try{
            Cursor cursor = getReadableDatabase().rawQuery(sql,null);

            if(cursor.moveToFirst()){
                contact = new ContactDetail(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("NAME")),
                        cursor.getString(cursor.getColumnIndex("BIRTHDATE")),
                        cursor.getString(cursor.getColumnIndex("ADDRESS")),
                        cursor.getString(cursor.getColumnIndex("IMAGE")),
                        cursor.getString(cursor.getColumnIndex("EMAIL")),
                        cursor.getString(cursor.getColumnIndex("NUMBER"))
                );

                contact.setTelId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("TELID"))));
                contact.setEmailId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("EMAILID"))));

                cursor.close();

                return contact;
            }else{
                cursor.close();

                return null;
            }
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Overridden method required because of the inheritance of SQLiteOpenHelper.
     * @param sqLiteDatabase created database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TO-DO
    }

    /**
     * Overridden method required because of the inheritance of SQLiteOpenHelper.
     * @param sqLiteDatabase database reference.
     * @param i the current updating row index.
     * @param i2
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
