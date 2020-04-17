package com.ajay.synccontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        if(intent != null && intent.data != null) {
            Log.e(TAG, intent.data.toString())

            var contactName = ""
            val cursor = contentResolver.query(intent.data!!,
                arrayOf(ContactsContract.Data.DATA1,ContactsContract.Data.DATA2,ContactsContract.Data.DATA3),
                null,null,null)

            if(cursor != null && cursor.moveToFirst()) {
                do{
                    Log.e(TAG, cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Data.DATA1)))
                    contactName = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Data.DATA2))
                    Log.e(TAG, contactName)
                    Log.e(TAG, cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Data.DATA3)))
                }while (cursor.moveToNext())
                cursor.close()
            }

            messaging_text.text = getString(R.string.messaging) + " $contactName"
        }
    }
}
