package com.example.matheus.phonebuk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class EditContactActivity extends Activity {

    public static final int NEW_CONTACT  = 0;
    public static final int EDIT_CONTACT = 1;

    public static final int PICK_PHOTO = 1;

    Bundle extras;
    TextView title;
    ContactDetail contact;

    int telId;
    int emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        extras = getIntent().getExtras();
        title  = (TextView) findViewById(R.id.edit_contact_ac);

        setActivityActionBarTitle();

        if(extras.getInt("action") == EDIT_CONTACT){
            contact = DataBridge.getDataBase().getContactDetail(extras.getInt("contactId"));

            telId   = contact.getTelId();
            emailId = contact.getEmailId();

            // Contact Name
            TextView  contactNameView      = (TextView) findViewById(R.id.contact_name);
            contactNameView.setText(contact.getName());

            // Contact Photo
            ImageView contactImage = (ImageView) findViewById(R.id.contact_photo);
            try {
                InputStream input = new FileInputStream(contact.getImagePath());
                contactImage.setImageBitmap(BitmapFactory.decodeStream(input));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Contact email
            TextView  contactEmailView     = (TextView) findViewById(R.id.contact_email);
            contactEmailView.setText(contact.getEmail());

            // Contact phone number
            TextView  contactPhoneView     = (TextView) findViewById(R.id.contact_phone);
            contactPhoneView.setText(contact.getPhoneNumber());

            // Contact Address
            TextView  contactAddressView   = (TextView) findViewById(R.id.address_input);
            contactAddressView.setText(contact.getAddress());

            // Contact Birth Date
            TextView  contactBirthDateView = (TextView) findViewById(R.id.birth_date_input);
            contactBirthDateView.setText(contact.getBirthDate());
        }
    }

    /**
     * Sets a new Image for the Contact by using the Image picked up from Gallery or Google Photos.
     * @param input An InputStream representing the referenced file got from Gallery or Google Photos.
     */
    private void setNewContactImage(InputStream input){
        ImageView imgV = (ImageView)findViewById(R.id.contact_photo);
        imgV.setImageBitmap(BitmapFactory.decodeStream(input));
    }

    /**
     * Sets a new Image for the Contact by using an Image took by the Camera.
     * @param bmp A Bitmap representing the image got by the Camera.
     */
    private void setNewContactImage(Bitmap bmp){
        ImageView imgV = (ImageView)findViewById(R.id.contact_photo);
        imgV.setImageBitmap(bmp);
    }

    /**
     * Starts the Image Picking Action by starting an Intent.
     */
    private void pickAnImage(){
        Intent photoPick = new Intent(Intent.ACTION_PICK);
        photoPick.setType("image/*");

        Intent photoTake = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String title = getResources().getString(R.string.get_a_photo);

        Intent chooser   = Intent.createChooser(photoPick,title);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{photoTake});
        startActivityForResult(chooser, PICK_PHOTO);
    }

    /**
     * Performed when the Contact's avatar photo is clicked.
     * @param v View corresponding to the RelativeLayout containing the contact photo.
     */
    public void onContactPhotoClicked(View v){
        pickAnImage();
    }

    /**
     * @deprecated
     * Performed when the Add new phone number next to the contact's phone number list is pressed.
     * @param v View corresponding to the ImageView that represents the add button.
     */
    public void onAddPhoneNumberClicked(View v){
        // TO-DO
    }

    /**
     * Event fired when Save Category button is clicked.
     * @param backButton Represents the view clicked. It's received from the Android API.
     */
    public void onBackButtonClicked(View backButton){
        finish();
    }

    /**
     * Sets the Activity Title in Activity's Action Bar based on the action (0 for adding a new
     * Contact and 1 for editing an existing one) passed by Extras.
     */
    private void setActivityActionBarTitle(){
        if(extras.getInt("action") == NEW_CONTACT){
            title.setText(R.string.new_contact_title);
        }else if(extras.getInt("action") == EDIT_CONTACT){
            title.setText(R.string.edit_contact_title);
        }
    }

    /**
     * Overridden event fired when the previous called activity finishes and it returns its results
     * to this activity.
     * @param requestCode Represents the request code passed from this activity to the called activity.
     * @param resultCode  Result code passed from the called activity to this one. Indicates the request's success/failure.
     * @param data        The result itself, an Intent containing the resulting data from the request made.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case PICK_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    if(data == null){
                        AlertDialog.Builder cannotGetImage = new AlertDialog.Builder(EditContactActivity.this);
                        cannotGetImage.setMessage(R.string.cant_get_image);
                        cannotGetImage.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog cannotDeleteAlert = cannotGetImage.create();
                        cannotDeleteAlert.show();
                    }else{
                        try {
                            InputStream input = getBaseContext().getContentResolver().openInputStream(data.getData());
                            setNewContactImage(input);
                        } catch (Exception e) {
                            setNewContactImage((Bitmap) data.getExtras().get("data"));
                            e.printStackTrace();
                        }
                    }
                }else{
                    AlertDialog.Builder cannotGetImage = new AlertDialog.Builder(EditContactActivity.this);
                    cannotGetImage.setMessage(R.string.cant_get_image);
                    cannotGetImage.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog cannotDeleteAlert = cannotGetImage.create();
                    cannotDeleteAlert.show();
                }
            break;
        }
    }

    /**
     * Saves the current selected Contact's picture to the storage and returns its path.
     * Performs some adjustments on the bitmap image file before saving the file to the storage.
     * @return A string representing the image's path returned to be stored into the SQLite database.
     */
    public String saveContactPicture(){
        Calendar c = Calendar.getInstance();
        String fileName = (System.currentTimeMillis()/1000L)+"";
        String p = getBaseContext().getFilesDir().getAbsolutePath()+"/"+fileName+".png";

        ImageView imgVw = (ImageView) findViewById(R.id.contact_photo);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgVw.getDrawable();
        Bitmap image = bitmapDrawable.getBitmap();

        float newDim = 128.0f;

        if(image.getWidth()>image.getHeight()){ // Image is rectangular and landscape

            float newWidth  = newDim;
            float newHeight = newDim/image.getWidth()*image.getHeight();
            image = Bitmap.createScaledBitmap(image,(int)newWidth,(int)newHeight,true);

        }else if(image.getWidth()<image.getHeight()){ // Image is rectangular and portrait

            float newWidth  = newDim/image.getHeight()*image.getWidth();
            float newHeight = newDim;
            image = Bitmap.createScaledBitmap(image,(int)newWidth,(int)newHeight,true);

        }else{ // Image is square

            float newWidth  = newDim, newHeight = 512.0f;
            image = Bitmap.createScaledBitmap(image,(int)newWidth,(int)newHeight,true);
        }

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            //FileOutputStream fos = openFileOutput(p, Context.MODE_PRIVATE);
            FileOutputStream fos = new FileOutputStream(new File(p));

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();


            return p;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Event fired when the Save button is clicked.
     * It performs either inserting or updating into the database based on the action
     * passed by the Extras.
     * @param v Represents the view clicked. It's received from the Android API.
     */
    public void onSaveButtonClicked(View v){
        if(extras.getInt("action") == NEW_CONTACT){

            String name       = ((TextView) findViewById(R.id.contact_name)).getText().toString();
            String phone      = ((TextView) findViewById(R.id.contact_phone)).getText().toString();
            String email      = ((TextView) findViewById(R.id.contact_email)).getText().toString();
            String address    = ((TextView) findViewById(R.id.address_input)).getText().toString();
            String birth_date = ((TextView) findViewById(R.id.birth_date_input)).getText().toString();
            String imgPath    = saveContactPicture();

            DataBridge.getDatabaseHelper().addNewContact(name, phone, email, address, birth_date, imgPath);
        }else if(extras.getInt("action") == EDIT_CONTACT){

            String name       = ((TextView) findViewById(R.id.contact_name)).getText().toString();
            String phone      = ((TextView) findViewById(R.id.contact_phone)).getText().toString();
            String email      = ((TextView) findViewById(R.id.contact_email)).getText().toString();
            String address    = ((TextView) findViewById(R.id.address_input)).getText().toString();
            String birth_date = ((TextView) findViewById(R.id.birth_date_input)).getText().toString();
            String imgPath    = saveContactPicture();

            DataBridge.getDatabaseHelper().updateExistingContact(name, address, birth_date, imgPath, phone, telId, email, emailId, extras.getInt("contactId"));

        }

        finish();
    }
}
