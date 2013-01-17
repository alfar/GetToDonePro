package dk.gettodone.pro;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AccountAuthenticatorActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.login_email);
		password = (EditText) findViewById(R.id.login_password);
		final Button login = (Button) findViewById(R.id.login_button_login);
		login.setOnClickListener(this);
	}

	private EditText username;
	private EditText password;

	public void onClick(View v) {
		Account account = new Account(username.getText().toString(),
				getString(R.string.ACCOUNT_TYPE));
		AccountManager am = AccountManager.get(this);
		boolean accountCreated = am.addAccountExplicitly(account, password
				.getText().toString(), null);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (accountCreated) { // Pass the new account back to the account
									// manager
				AccountAuthenticatorResponse response = extras
						.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
				Bundle result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, username
						.getText().toString());
				result.putString(AccountManager.KEY_ACCOUNT_TYPE,
						getString(R.string.ACCOUNT_TYPE));
				response.onResult(result);
			}
			finish();
		}
	}
}
