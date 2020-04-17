package com.ajay.synccontacts.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ajay.synccontacts.syncing.SyncAdapter

class SyncService : Service() {

    private val TAG: String = javaClass.simpleName
    private var mSyncAdapter: SyncAdapter? = null

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "Sync service created")
        synchronized(this) {
            if(mSyncAdapter == null) {
                mSyncAdapter = SyncAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "Sync service bound")
        return mSyncAdapter!!.syncAdapterBinder
    }
}
