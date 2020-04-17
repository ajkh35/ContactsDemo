package com.ajay.synccontacts

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.ajay.synccontacts.utils.Constants

class AuthenticatorActivity : AccountAuthenticatorActivity() {

    private val SECONDS_IN_A_MINUTE = 60L
    private val NUMBER_OF_MINUTES = 15L
    private val SYNC_INTERVAL = NUMBER_OF_MINUTES * SECONDS_IN_A_MINUTE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup the result intent
        val result = Intent()
        result.putExtra(AccountManager.KEY_ACCOUNT_NAME, Constants.ACCOUNT_NAME)
        result.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)

        // Add account via AccountManager and setup sync
        val account = Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE)
        if(!checkIfAppAccountExists()) {
            if(AccountManager.get(this).addAccountExplicitly(account, null,null)) {
                ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true)
                ContentResolver.addPeriodicSync(account, ContactsContract.AUTHORITY,Bundle(), SYNC_INTERVAL)
            }
        }

        // Set result and finish
        setAccountAuthenticatorResult(result.extras)
        setResult(Activity.RESULT_OK, result)
        finish()
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
}
