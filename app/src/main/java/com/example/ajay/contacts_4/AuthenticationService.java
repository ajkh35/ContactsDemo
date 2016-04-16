package com.example.ajay.contacts_4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ajay on 28/9/15.
 */
public class AuthenticationService extends Service {

    private static final String TAG = "AuthenticationService";
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        if (android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(TAG, "SyncAdapter Authentication Service started.");
        }
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        if (android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE)) {
            Log.v(TAG, "SyncAdapter Authentication Service stopped.");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "getBinder()...  returning the AccountAuthenticator binder for intent "
                    + intent);
        }
        return mAuthenticator.getIBinder();
    }
}