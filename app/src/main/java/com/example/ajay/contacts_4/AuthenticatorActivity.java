package com.example.ajay.contacts_4;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        com.example.ajay.contacts_4.Log.I("AuthenticatorActivity");
        Intent res = new Intent();
        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, Constants.ACCOUNT_NAME);
        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        res.putExtra(AccountManager.KEY_AUTHTOKEN, Constants.ACCOUNT_TOKEN);
        Account account = new Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE);
        mAccountManager = AccountManager.get(this);
        mAccountManager.addAccountExplicitly(account, null, null);
//      mAccountManager.setAuthToken(account, Constants.AUTHTOKEN_TYPE_FULL_ACCESS, Constants.ACCOUNT_TOKEN);
        ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        setAccountAuthenticatorResult(res.getExtras());
        setResult(RESULT_OK, res);
        finish();
    }
}