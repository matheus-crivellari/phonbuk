package com.example.matheus.phonebuk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsActivity extends Activity {

    private LinearLayout activityLayout;
    private final HashMap<View,ContactCategory> map = new HashMap<>();
    private ArrayList<View> tabList;
    private View contextCategoryTab;
    private View contextContactItem;

    private ListView contactListView;

    private ContactCategory cc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        activityLayout  = (LinearLayout) findViewById(R.id.activity_contacts_main_layout);
        contactListView = (ListView) findViewById(R.id.contact_list);

        registerForContextMenu(contactListView);
        fillCategoryListView();
        fillContactsList("all");
    }

    /**
     * Clears this Activity Category List my removing any buttons that are its children.
     */
    private void resetCategoryListView(){
        for(View v : tabList){
            ((ViewGroup) v.getParent()).removeView(v);
        }
    }

    /**
     * Fills this Activity Category List with buttons representing each Category for the user to be filtering his/her contacts by a desired category.
     * It's built by filling up a LinearLayout with a button representing each Category.
     */
    private void fillCategoryListView(){
        ArrayList<ContactCategory> categories = DataBridge.getDatabaseHelper().getCategories();
        LinearLayout list = (LinearLayout) findViewById(R.id.categories_button_list);
        tabList = new ArrayList<View>();


        if(categories == null){
            // Do nothing.
        }else{
            for (ContactCategory category : categories){
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.button_category_contacts, null);
                map.put(view,category);
                tabList.add(view);

                TextView tv = (TextView) view.getChildAt(0);
                try {
                    tv.setText(getResources().getIdentifier(category.getName(), "string", getPackageName()));
                }catch(Exception e){
                    tv.setText(category.getName());
                }

                int color;
                try{
                    color = getResources().getColor(getResources().getIdentifier(Color.getColor(category.getIdColor()) + "", "color", getPackageName()));
                    tv.setBackgroundColor(color);
                }catch(Exception e){
                    color = getResources().getColor(R.color.app_bg);
                    tv.setBackgroundColor(color);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectCategory(view,map);
                    }
                });

                registerForContextMenu(view);
                list.addView(view);
            }
        }
    }

    /**
     * Fills this Activity Contact List with buttons representing each Contact filtered by the desired Category's name.
     * @param category A string representing the desired Category's name from which the Contacts will be retrieved.
     */
    private void fillContactsList(String category){
        final ArrayList<Contact> contacts = DataBridge.getDatabaseHelper().getContatcsByCategoryName(category);

        if(contacts == null){
            contactListView.setAdapter(null);
        }else{
            ContactArrayAdapter adapter = new ContactArrayAdapter(getBaseContext(),R.layout.contacts_list_item,contacts);
            contactListView.setAdapter(adapter);

            contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Contact con = (Contact) view.getTag();
                    viewContactDetails(con.getId());
                }
            });
        }
    }

    /**
     * Event fired when a Contact List's item is clicked.
     * It drives the user to the Contact Details Activity where he/she can view the current Contact's details.
     * @param contactId An integer representing the desired Contact's id.
     */
    private void viewContactDetails(int contactId){
        Intent intent = new Intent(getBaseContext(), ContactDetailsActivity.class);
        intent.putExtra("contactId", contactId);
        startActivity(intent);
    }

    /**
     * Checks if the passed View is either the Contact List or not.
     * @param v View to be checked.
     * @return Returns if the View v is the Contact List (true) or not (false).
     */
    private boolean checkIfViewIsContactList(View v){
        return (v == findViewById(R.id.contact_list));
    }

    /**
     * Checks if the passed View is either a Category Tab or not.
     * @param v View to be checked.
     * @return Returns if the View v is a Category Tab (true) or not (false).
     */
    private boolean checkIfViewIsACategoryTab(View v){
        boolean ret = false;

        for(View view : tabList){
            if(v == view){
                ret = true;
            }
        }

        return ret;
    }

    /**
     * Event fired when Add Category button is clicked. Drives the user to New Category Activity for him to submit a new Category to the app.
     * @param v Represents the Add Category button. It's received from the Android API.
     */
    public void onAddCategoryClicked(View v){
        Intent intent = new Intent(getBaseContext(), EditCategoryActivity.class);
        intent.putExtra("action",EditCategoryActivity.NEW_CATEGORY);
        startActivity(intent);
    }

    /**
     * Event fired when Add Contact button is clicked. Drives the user to New Contact Activity for him to submit a new Contact to the app.
     * @param v Represents the Add Contact button. It's received from the Android API.
     */
    public void onAddContactButtonClicked(View v){
        Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
        intent.putExtra("action",EditContactActivity.NEW_CONTACT);
        startActivity(intent);
    }

    /**
     * Event fired when a previously called activity finishes and returns a result to the calling activity.
     * @param requestCode Represents the request code passed from this activity to the called activity.
     * @param resultCode  Result code passed from the called activity to this one. Indicates the request's success/failure.
     * @param data        The result itself, an Intent containing the resulting data from the request made.
     */
    public void onResult(int requestCode, int resultCode, Intent data){

    }

    /**
     * Event fired when the previous called activity finishes and the app comes back to this activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        resetCategoryListView();
        fillCategoryListView();

        if(cc != null){
            fillContactsList("none");
            fillContactsList(cc.getName());
        }else{
            fillContactsList("all");
        }
    }

    /**
     * Event fired when a Category Button is clicked.
     * It filters the Contact's List by categories and refreshes the list shown.
     * @param view A View representing the current specific Category button clicked by the user.
     */
    /**
     * Event fired when a Category Button is clicked.
     * It filters the Contact's List by categories and refreshes the list shown.
     * @param view Represents the Category button. It's received from the Android API.
     * @param map  A HashMap relating all Category's Views to all ContactCategory's in list.
     */
    public void selectCategory(View view, HashMap<View,ContactCategory> map){
        cc = map.get(view);

        try{
            int color = getResources().getColor(getResources().getIdentifier(Color.getColor(cc.getIdColor()) + "", "color", getPackageName()));
            activityLayout.setBackgroundColor(color);
        }catch(Exception e){
            int color = getResources().getColor(Color.BLUE);
            activityLayout.setBackgroundColor(color);
        }

        fillContactsList(cc.getName());
    }

    /**
     * Event fired when the user long clicks every element registered for Context Menu usage.
     * It's built separating Category Tab List from Contact List by defining Context Menu's groups (0 for Category Tab List's items and 1 for Contact List's items).
     * @param menu        The Context Menu itself.
     * @param v           The View which is calling the Context Menu.
     * @param menuInfo    Information sent from the API to the Context Menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(checkIfViewIsACategoryTab(v)){
            contextCategoryTab = v;
            menu.setHeaderTitle(R.string.category_options);
            menu.add(0, 0, 0, R.string.edit_category);//groupId, itemId, order, title
            menu.add(0, 1, 0, R.string.remove_category);
            menu.add(0, 2, 0, R.string.context_menu_cancel);

        }else if(checkIfViewIsContactList(v)){
            contextContactItem = v;
            menu.setHeaderTitle(R.string.contact_options);
            menu.add(1, 0, 0, R.string.edit_contact);
            menu.add(1, 1, 0, R.string.choose_category);
            menu.add(1, 2, 0, R.string.remove_contact);
            menu.add(1, 2, 0, R.string.remove_contact_good);
            menu.add(1, 3, 0, R.string.context_menu_cancel);
        }
    }

    /**
     * Event fired when the user selects an option from the context menu.
     * @param item    User selected item.
     * @return        Return the event.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final View view = contextCategoryTab;
        final ContactCategory cat = map.get(view);
        if(item.getGroupId() == 0){ // Category item selected
            switch (item.getItemId()){
                case 0:
                    if(cat.getCategoryBelongsToSystem() == 1) {
                        AlertDialog.Builder cannotDeleteBuilder = new AlertDialog.Builder(ContactsActivity.this);
                        cannotDeleteBuilder.setMessage(R.string.cant_delete_category);
                        cannotDeleteBuilder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog cannotDeleteAlert = cannotDeleteBuilder.create();
                        cannotDeleteAlert.show();
                    }else{
                        Intent intent = new Intent(getBaseContext(), EditCategoryActivity.class);
                        intent.putExtra("action",EditCategoryActivity.EDIT_CATEGORY);

                        intent.putExtra("catId"      ,cat.getId());
                        intent.putExtra("catName"    ,cat.getName());
                        intent.putExtra("catColor"   ,cat.getIdColor());
                        intent.putExtra("catSystem"  ,cat.getCategoryBelongsToSystem());

                        startActivity(intent);
                    }
                break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                    builder.setMessage(getResources().getString(R.string.remove_category_alert)+" \""+cat.getName()+"\"?");
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { // Delete category from Database.
                            if(cat.getCategoryBelongsToSystem() == 1) {
                                AlertDialog.Builder cannotDeleteBuilder = new AlertDialog.Builder(ContactsActivity.this);
                                cannotDeleteBuilder.setMessage(R.string.cant_delete_category);
                                cannotDeleteBuilder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                AlertDialog cannotDeleteAlert = cannotDeleteBuilder.create();
                                cannotDeleteAlert.show();
                            }else{
                                try{
                                    DataBridge.getDatabaseHelper().deleteCategory(cat.getId());
                                    AlertDialog.Builder suc = new AlertDialog.Builder(ContactsActivity.this);
                                    suc.setTitle(R.string.delete_category_success_title);
                                    suc.setMessage(R.string.delete_category_success_msg);
                                    suc.setNeutralButton(R.string.ok,new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // do nothing
                                        }
                                    });
                                    AlertDialog sucAlert = suc.create();
                                    sucAlert.show();
                                    resetCategoryListView();
                                    fillCategoryListView();
                                }catch(Exception e){
                                    AlertDialog.Builder fail = new AlertDialog.Builder(ContactsActivity.this);
                                    fail.setTitle(R.string.delete_category_fail_title);
                                    fail.setMessage(R.string.delete_category_fail_msg);
                                    fail.setNeutralButton(R.string.ok,new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // do nothing
                                        }
                                    });
                                    AlertDialog failAlert = fail.create();
                                    failAlert.show();
                                }
                            }
                        }
                    });

                    builder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() { // Don't exclude category from database.
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                break;
                case 2:
                break;
            }
        }else if(item.getGroupId() == 1){ // Contact item selected
            switch (item.getItemId()){

                case 0:
                    int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                    int conId = ((Contact)contactListView.getChildAt(position).getTag()).getId();

                    Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
                    intent.putExtra("action",EditContactActivity.EDIT_CONTACT);
                    intent.putExtra("contactId",conId);
                    startActivity(intent);
                break;
                case 1:
                    // TO-DO
                break;
                case 2:
                    // TO-DO
                break;
                case 3:
                    // TO-DO
                break;

            }

        }
        return true;
    }

    /**
     * Event fired when Save Category button is clicked.
     * @param backButton Represents the Back button. It's received from the Android API.
     */
    public void onBackButtonClicked(View backButton){
        finish();
    }

    class ContactArrayAdapter extends ArrayAdapter<Contact>{

        private Context context;
        private int layout;
        private List list;

        /**
         * Represents the Contact Array Adapter. It's responsible for feeding up the Contact List View correctly.
         * @param context  Represents the current Context where the adapter is working.
         * @param resource Represents the layout resource. It's used for inflating each List View's row.
         * @param objects  Represents the object list containing the elements which will provide the information to be displayed by every List View's item.
         */
        public ContactArrayAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);

            this.context = context;
            this.layout  = resource;
            this.list    = objects;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Contact con = (Contact) list.get(position);
            View row = inflater.inflate(layout,null,true);

            row.setTag(con);

            TextView txView = (TextView) row.findViewById(R.id.contact_name);
            txView.setText(con.getName());

            try {
                InputStream input = new FileInputStream(con.getImagePath());
                ImageView imgView = (ImageView) row.findViewById(R.id.contact_photo_list);
                imgView.setImageBitmap(BitmapFactory.decodeStream(input));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return row;
        }
    }
}
