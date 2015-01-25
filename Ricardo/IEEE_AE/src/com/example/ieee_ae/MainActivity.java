package com.example.ieee_ae;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ieee_ae.Settings;	

public class MainActivity extends ActionBarActivity implements OnClickListener {
	EditText goCats;
	Button buttonGoCats;
	TextView numberDisplay;
	Button buttonSubtract;
	Button buttonAdd;
	int counter;
	Settings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		goCats = (EditText) findViewById(R.id.editText1);
		buttonGoCats = (Button) findViewById(R.id.buttonGoCats);
		numberDisplay = (TextView) findViewById(R.id.numberDisplay);
		buttonSubtract = (Button) findViewById(R.id.buttonSubtract);
		buttonAdd = (Button) findViewById(R.id.buttonAdd);
		settings = new Settings();
		
		buttonGoCats.setOnClickListener(this);
		buttonSubtract.setOnClickListener(this);
		buttonAdd.setOnClickListener(this);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		numberDisplay.setText(preferences.getInt("settings_change_number_display", 0));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//settings.show(this.getFragmentManager(), "Settings test");
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// If a view is clicked, find which view and respond appropriately?
		if (v.equals(buttonGoCats)) {
			counter++;
			goCats.setText(goCats.getText().toString() + Integer.toString(counter));
		}
		if (v.equals(buttonAdd)) {
			
		}
		if (v.equals(buttonSubtract)) {
			
		}
	}
}
