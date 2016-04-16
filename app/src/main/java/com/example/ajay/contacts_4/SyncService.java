package com.example.ajay.contacts_4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ajay on 28/9/15.
 */
public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter mSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.I("Sync Service created.");
        synchronized (sSyncAdapterLock){
            if(mSyncAdapter == null){
                mSyncAdapter = new SyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.I("Sync Service binded.");
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
