package es.jshcd.lectorrss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * @author Javier Sanchez Hernandez
 * @version 1.0
 */
public class MainActivity extends FragmentActivity implements
NewsListFragment.SelectionListener {
	private NewsListFragment mNewsListFragment;
	private NewsDetailFragment mNewsDetailFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// If the layout is single-pane, create the FriendsFragment
		// and add it to the Activity
		if (!isInTwoPaneMode()) {
			mNewsListFragment = new NewsListFragment();

			// Add the NewsListFragment to the fragment_container
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.add(R.id.fragment_container, mNewsListFragment);
			fragmentTransaction.commit();

		} else {
			// Otherwise, save a reference to the NewsDetailFragment for later
			// use
			mNewsDetailFragment = (NewsDetailFragment) getSupportFragmentManager()
					.findFragmentById(R.id.news_detail_frag);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        //case R.id.help:
	        //    showHelp();
	        //    return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * Indicates if the application is running in Two Pane Mode.
	 * 
	 * @return true if there is not fragment_container ID and false otherwise.
	 */
	private boolean isInTwoPaneMode() {
		return findViewById(R.id.fragment_container) == null;
	}

	/**
	 * Display selected News Detail.
	 * @param position
	 */
	public void onItemSelected(int position) {
		// If there is no NewsDetailFragment instance, then create one
		if (mNewsDetailFragment == null) {
			mNewsDetailFragment = new NewsDetailFragment();
		}
		// If in single-pane mode, replace single visible Fragment
		if (!isInTwoPaneMode()) {
			// Replace the fragment_container with the NewsDetailFragment
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, mNewsDetailFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

			// execute transaction now
			getSupportFragmentManager().executePendingTransactions();

		}

		// Update News Detail display on NewsDetailFragment
		mNewsDetailFragment.updateFeedDisplay(position);

	}
	
	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
}
