package com.ing3nia.parentalcontrol.ui;

import com.ing3nia.parentalcontrol.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class RegisterSmartphone extends Activity {
	
	private int userType;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.register_smartphone);
		Bundle extras = getIntent().getExtras();
		
		if (extras == null) {
			return;
		}
		
		this.userType = extras.getInt("userType");
		String user = "";
		
		if (this.userType == 0) {
			user = "Parent!";
		}
		else if (this.userType == 1) {
			user = "Child!";
		}
		
		Toast.makeText(this, "You chose: " + user + " in previous Activity", Toast.LENGTH_SHORT).show();
	}
}
