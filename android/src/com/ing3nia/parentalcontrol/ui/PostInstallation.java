package com.ing3nia.parentalcontrol.ui;

import com.ing3nia.parentalcontrol.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PostInstallation extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_installation);
	}

	/**
	 * TODO hay que guardar el tipo de usuario para
	 * saber que es lo proximo que hay que hacer. 
	 */
	public void onParentClick(View view) {
		Intent i = new Intent(PostInstallation.this, Login.class);
		startActivity(i);
	}

	public void onChildClick(View view) {
		Intent i = new Intent(PostInstallation.this, RegisterSmartphone.class);
		startActivity(i);		
	}
}
