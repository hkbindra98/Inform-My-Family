package com.example.sagv2;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareMyContacts extends MainActivity implements Serializable
{

    public String android_contact_Name = "";
    public String android_contact_TelefonNr = "";
    public int android_contact_ID=0;

    ShareMyContacts() {
        ArrayList<MainActivity> arrayList_Android_Contacts = new ArrayList<MainActivity>();
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }

        if (cursor_Android_Contacts.getCount() > 0) {
            while (cursor_Android_Contacts.moveToNext()) {
                ShareMyContacts android_contact = new ShareMyContacts();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                android_contact.android_contact_Name = contact_display_name;
                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);
                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        android_contact.android_contact_TelefonNr = phoneNumber;
                    }
                    phoneCursor.close();
                }
                arrayList_Android_Contacts.add(android_contact);
            }
            sendIt(arrayList_Android_Contacts);
        }
    }

    public void sendIt(ArrayList<MainActivity> details)
    {
        String msg="";
        for(MainActivity obj : details)
        {
            msg += obj.android_contact_Name + "    " + obj.android_contact_TelefonNr + " /n ";
        }

       /* String sendTo = sendid.getText().toString();

        TextView add = findViewById(R.id.addText);
        add.setText(msg); */
        String sendTo = "fasa@gmail.com";
        String subject = "My Contact List";
        Intent intent = new Intent(Intent.ACTION_SEND);

        // add three fiels to intent using putExtra function
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { sendTo });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, details);

        // set type of intent
        intent.setType("message/rfc822");

        // startActivity with intent with chooser
        // as Email client using createChooser function
        startActivity(
                Intent
                        .createChooser(intent,
                                "Choose an Email client :"));
    }
}



