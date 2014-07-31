package es.jshcd.lectorrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Javier Sanchez Hernandez
 * @version 1.0
 */
public class SettingsActivity extends Activity {
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	private TextView textViewDataSource;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		prefs = getSharedPreferences("rss", 0);
		editor = prefs.edit();
		
		setupSimplePreferencesScreen();
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	private void setupSimplePreferencesScreen() {
		textViewDataSource = (TextView) findViewById(R.id.textViewDataSource);

		final String dataSource = prefs.getString("data_source",
				getString(R.string.default_feed_url));
		textViewDataSource.setText(dataSource);

		textViewDataSource.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				showDialog(getApplicationContext().getString(R.string.pref_title_data_source), prefs.getString("data_source",
						getString(R.string.default_feed_url)));
			}
		});
		
	}
	
	/**
	 * Shows an <code>AlertDialog</code> with an <code>EditText</code> to change
	 * the value of the indicated parameter.
	 * 
	 * @param message
	 * @param oldValue
	 */
	public void showDialog(String message, String oldValue) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(
				R.string.introduce_the_desired_value));
		
		alert.setMessage(message);
		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setText(oldValue);
		alert.setView(input);
		alert.setPositiveButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable value = input.getText();
						editor.putString("data_source", value.toString())
									.commit();
						textViewDataSource.setText(value.toString());
					}
				});
		alert.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
		alert.show();
	}
}
