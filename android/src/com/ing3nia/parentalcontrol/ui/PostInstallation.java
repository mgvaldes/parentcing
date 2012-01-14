package com.ing3nia.parentalcontrol.ui;

import com.ing3nia.parentalcontrol.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PostInstallation extends Activity {

	/**
	 * Represents the RegisterSmartphone screen
	 */
	private RegisterSmartphone registerSmartphone;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_installation);
	}

	public void onParentClick(View view) {
		Intent i = new Intent(this, RegisterSmartphone.class);
		i.putExtra("userType", 0);
		startActivity(i);
	}

	public void onChildClick(View view) {
		Intent i = new Intent(this, RegisterSmartphone.class);
		i.putExtra("userType", 1);
		startActivity(i);
	}
}
