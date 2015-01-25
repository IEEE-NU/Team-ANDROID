package com.example.ieee_ae;

import android.app.Activity;
import android.os.Bundle;
//import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Settings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
    
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
        
        @Override
        public void onDestroy() {
        	// save the changed settings?
        }
    }
}
/*
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

public class Settings extends DialogFragment{
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	     // Use the Builder class for convenient dialog construction
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		 
	     builder.setView(inflater.inflate(R.layout.dialog_settings,null))
	    		.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int id) {
	    				// FIRE ZE MISSILES!
	                }
	            })
	            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int id) {
	            		// User cancelled the dialog
	                }
	            })
	            .setTitle(R.string.settings_title_change_num);
	     // Create the AlertDialog object and return it
	     return builder.create();  
	 }
}
*/
