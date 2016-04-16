package com.example.ajay.contacts_4;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract.*;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay on 28/9/15.
 */
public class ContactsManager {

    private static String MIMETYPE = "vnd.android.cursor.item/com.example.ajay.contacts_4";

    public static void addContact(Context context,MyContact contact){

        ContentResolver resolver = context.getContentResolver();
        boolean mHasAccount = isAlreadyRegistered(resolver, contact.Id);

        if(mHasAccount){
            Log.I(context.getString(R.string.account_exists));
        } else {

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            // insert account name and account type
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
                    .withValue(ContactsContract.RawContacts.AGGREGATION_MODE,
                            ContactsContract.RawContacts.AGGREGATION_MODE_DEFAULT)
                    .build());

            // insert contact number
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, contact.number)
                    .build());

            // insert contact name
//            ops.add(ContentProviderOperation
//                    .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
//                    .withValue(CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
//                    .build());

            // insert mime-type data
            ops.add(ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE)
                    .withValue(ContactsContract.Data.DATA1, 12345)
                    .withValue(ContactsContract.Data.DATA2, "user")
                    .withValue(ContactsContract.Data.DATA3, "MyData")
                    .build());

            try {
                resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Check if contact is already registered with app
     * @param resolver
     * @param id
     * @return
     */
    private static boolean isAlreadyRegistered(ContentResolver resolver, String id){

        boolean isRegistered = false;
        List<String> str = new ArrayList<>();

        //query raw contact id's from the contact id
        Cursor c = resolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts._ID},
                RawContacts.CONTACT_ID + "=?",
                new String[]{id}, null);

        //fetch all raw contact id's and save them in a list of string
        if(c != null && c.moveToFirst()) {
            do{
                str.add(c.getString(c.getColumnIndexOrThrow(RawContacts._ID)));
            }while (c.moveToNext());
            c.close();
        }

        //query account types and check the account type for each raw contact id
        for(int i=0; i<str.size();i++) {
            Cursor c1 = resolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts.ACCOUNT_TYPE},
                    RawContacts._ID + "=?",
                    new String[]{str.get(i)}, null);

            if(c1 != null){
                c1.moveToFirst();
                String accType = c1.getString(c1.getColumnIndexOrThrow(RawContacts.ACCOUNT_TYPE));
                if(accType != null && accType.equals("com.example.ajay.contacts_4")) {
                    isRegistered = true;
                    break;
                }
                c1.close();
            }
        }

        return isRegistered;
    }

    /**
     * Check for sync call
     * @param uri
     * @param isSyncOperation
     * @return
     */
    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build();
        }
        return uri;
    }

//    public static void updateMyContact(Context context,String id){
//
//        Cursor ids = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
//                ContactsContract.Contacts._ID + " = ?",
//                new String[]{id}, null);
//
////        int rawContactId = ids.getColumnIndexOrThrow(ContactsContract.RawContacts.);
//
//        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
//        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
//                .withValue(ContactsContract.Contacts._ID, id)
//                .withValue(ContactsContract.Data.DATA1, "Data1")
//                .withValue(ContactsContract.Data.DATA2, "Data2")
//                .withValue(ContactsContract.Data.DATA3, "MyData")
//                .build());
//
//        ids.close();
//
//        try{
//            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }
//    }
}