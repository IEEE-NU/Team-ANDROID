package com.example.ieee_ae;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

public class MainActivity extends ActionBarActivity implements OnClickListener {
	SharedPreferences sharedPref;
	EditText goCats;
	TextView ageText;
	Button buttonGoCats;
	Button plus;
	Button minus;
	int counter;
	int age;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		goCats = (EditText) findViewById(R.id.editText1);
		buttonGoCats = (Button) findViewById(R.id.buttonGoCats);
		
		buttonGoCats.setOnClickListener(this);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		age = Integer.parseInt(sharedPref.getString("stringAge","0"));
		ageText = (TextView) findViewById(R.id.textView2);
		ageText.setText(Integer.toString(age));
		minus = (Button) findViewById(R.id.button1);
		plus = (Button) findViewById(R.id.button2);
		minus.setOnClickListener(this);
		plus.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		age = Integer.parseInt(sharedPref.getString("stringAge","0"));
		ageText.setText(Integer.toString(age));
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
			startActivity(new Intent(this, SettingsActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(buttonGoCats)) {
			counter++;
			goCats.setText(goCats.getText().toString() + Integer.toString(counter));
		} else if (v.equals(plus)) {
			age++;
			Editor editor = sharedPref.edit();
			editor.putString("stringAge", Integer.toString(age));
			editor.apply();
			ageText.setText(Integer.toString(age));
		} else if (v.equals(minus)) {
			age--;
			Editor editor = sharedPref.edit();
			editor.putString("stringAge", Integer.toString(age));
			editor.apply();
			ageText.setText(Integer.toString(age));
		}
	}
}
