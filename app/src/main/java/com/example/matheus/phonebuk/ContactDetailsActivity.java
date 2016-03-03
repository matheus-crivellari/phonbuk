package com.example.matheus.phonebuk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContactDetailsActivity extends Activity {

    ContactDetail contact;
    int contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Bundle extras = getIntent().getExtras();
        contactId = extras.getInt("contactId");

        fillContactInfo();
    }

    /**
     * Retrieves all Contact's data from the database by its id and fills up the screen with its information.
     */
    private void fillContactInfo(){
        contact = DataBridge.getDatabaseHelper().getContactDetail(contactId);

        // Contact's Name
        TextView tv = (TextView) findViewById(R.id.contact_name);
        tv.setText(contact.getName());

        // Contact's Photo
        ImageView iv = (ImageView) findViewById(R.id.contact_photo);
        try {
            InputStream input = new FileInputStream(contact.getImagePath());
            iv.setImageBitmap(BitmapFactory.decodeStream(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Contact's phone number
        TextView telephoneView = (TextView) findViewById(R.id.telephone_view);
        telephoneView.setText(contact.getPhoneNumber());

        // Contact's e-mail
        TextView emailView = (TextView) findViewById(R.id.email_view);
        emailView.setText(contact.getEmail());

        // Contact's Address
        TextView addressView = (TextView) findViewById(R.id.address_view);
        addressView.setText(contact.getAddress());

        // Contact's Birth Date
        TextView birthDateView = (TextView) findViewById(R.id.birth_date_view);
        birthDateView.setText(contact.getBirthDate());
    }

    /**
     * Event fired when the phone icon closer to Contact's phone number is clicked.
     * It calls an Intent in order to let the user perform a calling to the current Contact.
     * @param v    Represents the view clicked. It's received from the Android API.
     */
    public void onCallButtonClicked(View v){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
        startActivity(callIntent);
    }

    /**
     * Event fired when the blob icon closer to Contact's phone number is clicked.
     * It calls an Intent in order to let the user send a message (SMS) to the current Contact.
     * @param v    Represents the view clicked. It's received from the Android API.
     */
    public void onSendSmSButtonClicked(View v){
        Intent textMessageIntent = new Intent(Intent.ACTION_VIEW);
        textMessageIntent.setData(Uri.parse("sms:"+contact.getPhoneNumber()));
        startActivity(textMessageIntent);
    }

    /**
     * Event fired when the mail icon in closer to Contact's e-mail address is clicked.
     * It calls an Intent in order to let the user send an e-mail message to the current Contact.
     * @param v    Represents the view clicked. It's received from the Android API.
     */
    public void onSendMailButtonClicked(View v){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+contact.getEmail()));
        startActivity(emailIntent);
    }

    /**
     * Event fired when the back button is clicked.
     * It drives the user back to the previous activity.
     * @param v    Represents the view clicked. It's received from the Android API.
     */
    public void onBackButtonClicked(View v){
        finish();
    }

    /**
     * Event fired when the edit icon on action bar is clicked.
     * It drives the user to the Edit Contact Activity where he/she can edit the current Contact.
     * @param v    Represents the view clicked. It's received from the Android API.
     */
    public void onEditContactButtonClicked(View v){
        Intent intent = new Intent(getBaseContext(), EditContactActivity.class);
        intent.putExtra("action",EditContactActivity.EDIT_CONTACT);
        intent.putExtra("contactId",contact.getId());
        startActivity(intent);
    }

    /**
     * Event fired when the previous called activity finishes and the app comes back to this activity.
     * It refreshes the Contact's info in order to display correctly any information updated by the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        fillContactInfo();
    }
}
