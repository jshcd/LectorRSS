package es.jshcd.lectorrss;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import es.jshcd.lectorrss.controller.DataBaseHandler;
import es.jshcd.lectorrss.controller.DownloadImageTask;
import es.jshcd.lectorrss.model.News;

/**
 * 
 * @author Javier Sanchez Hernandez
 * @version 1.0
 */
public class NewsDetailFragment extends Fragment {

	private TextView mTextViewDetailTitle;
	private ImageView mImageViewDetailImage;
	private TextView mTextViewDetailDescription;
	private Button mButtonDetailOpenInBrowser;

	List<News> newsList = new LinkedList<News>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.news_detail, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		DataBaseHandler dbHandler = new DataBaseHandler(getActivity());
		
		// Read database
		// Add items from database to news list
		// Open database
		dbHandler.openDataBase();
		Cursor c = dbHandler.getNews();
		if (c.moveToFirst()) {
			do { // We go over the cursor up to the end
				String title = c.getString(0);
				String description = c.getString(1);
				String content = c.getString(2);
				String image = c.getString(3);
				String link = c.getString(4);
				News news = new News(title, description, content, image, link);
				newsList.add(news);
			} while (c.moveToNext());
		}
		if (c != null) {
			c.close();
		}
		dbHandler.close();

	}

	public void updateFeedDisplay(int position) {
		// Initialize all views
		mTextViewDetailTitle = (TextView) getView().findViewById(
				R.id.textViewDetailTitle);

		mImageViewDetailImage = (ImageView) getView().findViewById(
				R.id.imageViewDetailImage);
		mTextViewDetailDescription = (TextView) getView().findViewById(
				R.id.textViewDetailDescription);
		mButtonDetailOpenInBrowser = (Button) getView().findViewById(
				R.id.buttonDetailOpenInBrowser);
		
		
		// Gets the selected news
		final News news = newsList.get(position);
		
		// Sets all the field values on the view
		mTextViewDetailTitle.setText(news.getTitle());
		mTextViewDetailDescription.setText(Html.fromHtml(news.getContent()));
		if (news.getImageUrl().length() > 0) {
			DownloadImageTask downloadImageTask = new DownloadImageTask(mImageViewDetailImage);
			downloadImageTask.execute(news.getImageUrl());
		}
		// Give functionality to the button
		mButtonDetailOpenInBrowser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Open the URL in browser
				Uri uri = Uri.parse(news.getLink());
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
		
	}

}
