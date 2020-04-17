package com.ajay.synccontacts

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ajay.synccontacts.network.ApiClient
import com.ajay.synccontacts.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName

    private val PERMISSIONS_REQUEST_CODE: Int = 1

    private lateinit var mAccount: Account
    private val SECONDS_IN_A_MINUTE = 60L
    private val NUMBER_OF_MINUTES = 15L
    private val SYNC_INTERVAL = NUMBER_OF_MINUTES * SECONDS_IN_A_MINUTE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                PERMISSIONS_REQUEST_CODE)
        } else {
            addAppAccount()
        }
    }

    /**
     * Method to check if app account is already added
     */
    private fun checkIfAppAccountExists(): Boolean {
        var accountExists = false
        for(account in AccountManager.get(this).accounts) {
            if(account.type == Constants.ACCOUNT_TYPE) {
                accountExists = true
                break
            }
        }

        return accountExists
    }

    /**
     * Method to add app account to device
     */
    private fun addAppAccount() {
        mAccount = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)

        if(!checkIfAppAccountExists()) {
            if(AccountManager.get(this).addAccountExplicitly(mAccount, null,null)) {
                ContentResolver.setSyncAutomatically(mAccount, ContactsContract.AUTHORITY, true)
                ContentResolver.addPeriodicSync(mAccount, ContactsContract.AUTHORITY, Bundle(), SYNC_INTERVAL)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        when(requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if(grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        addAppAccount()
                    } else {
                        showPermissionsAlert()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //region Receive Sync completed broadcast from SyncAdapter
    companion object {
        const val ACTION_SYNC_COMPLETED: String = "com.ajay.synccontacts.ACTION_FINISHED_SYNC"
    }

    private var mSyncBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            progress_bar.visibility = View.GONE
            Log.i(TAG, "Sync completed")
            Toast.makeText(this@MainActivity, "Refresh Completed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ACTION_SYNC_COMPLETED)
        registerReceiver(mSyncBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        unregisterReceiver(mSyncBroadcastReceiver)
        super.onPause()
    }
    //endregion

    /**
     * Method to show permissions alert
     */
    private fun showPermissionsAlert() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.permissions_alert_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                dialog.cancel()
                finish()
            }

        val alert = builder.create()
        alert.setTitle(getString(R.string.permissions_alert))
        alert.show()
    }

    /**
     * Method to handle button clicks
     */
    fun handleClick(view: View) {
        when(view.id) {
            R.id.register_number_btn -> {

                if(register_number.text.toString().isEmpty() ||
                    register_number.text.toString().length < 8 ||
                    register_number.text.toString().length > 15 ||
                    !register_number.text.toString().matches("[0-9]+".toRegex())){
                    Toast.makeText(this@MainActivity, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                    return
                }

                // register with server
                progress_bar.visibility = View.VISIBLE
                ApiClient.getClient()
                    .registerNumber(register_number.text.toString().trim()).enqueue(object: Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            progress_bar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, "Network Error",
                                Toast.LENGTH_SHORT).show()
                            Log.d(TAG, t.localizedMessage)
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            progress_bar.visibility = View.GONE
                            if(response.isSuccessful){
                                Toast.makeText(this@MainActivity, response.body()!!.string(),
                                    Toast.LENGTH_SHORT).show()
                                Log.d(TAG, response.body()!!.string())
                            } else {
                                Toast.makeText(this@MainActivity, response.errorBody()!!.string(),
                                    Toast.LENGTH_SHORT).show()
                                Log.d(TAG, response.errorBody()!!.string())
                            }
                        }
                    })
            }

            R.id.deregister_number_btn -> {

                if(deregister_number.text.toString().isEmpty() ||
                    deregister_number.text.toString().length < 8 ||
                    deregister_number.text.toString().length > 15 ||
                    !deregister_number.text.toString().matches("[0-9]+".toRegex())){
                    Toast.makeText(this@MainActivity, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                    return
                }

                // deregister with server
                progress_bar.visibility = View.VISIBLE
                ApiClient.getClient().deregisterNumber(deregister_number.text.toString().trim()).enqueue(object: Callback<ResponseBody>{
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Network Error",
                            Toast.LENGTH_SHORT).show()
                        Log.d(TAG, t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        progress_bar.visibility = View.GONE
                        if(response.isSuccessful){
                            Toast.makeText(this@MainActivity, response.body()!!.string(),
                                Toast.LENGTH_SHORT).show()
                            Log.d(TAG, response.body()!!.string())
                        } else {
                            Toast.makeText(this@MainActivity, response.errorBody()!!.string(),
                                Toast.LENGTH_SHORT).show()
                            Log.d(TAG, response.errorBody()!!.string())
                        }
                    }

                })
            }

            R.id.refresh_contacts -> {
                // refresh contacts by calling SyncAdapter
                progress_bar.visibility = View.VISIBLE
                val settingsBundle = Bundle().apply {
                    putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
                    putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
                }

                ContentResolver.requestSync(mAccount, ContactsContract.AUTHORITY, settingsBundle)
            }
        }
    }
}
