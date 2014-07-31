package es.jshcd.lectorrss.view;

import java.util.List;

import es.jshcd.lectorrss.R;
import es.jshcd.lectorrss.controller.DownloadImageTask;
import es.jshcd.lectorrss.model.News;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Javier Sanchez Hernandez
 * @version 1.0
 */
public class NewsListItemAdapter extends BaseAdapter {
	/**
	 * The list containing the news.
	 */
	private List<News> newsList;
	/**
	 * The object used to create each grid item.
	 */
	private static LayoutInflater inflater = null;
	
	/**
	 * Constructor of the class.
	 * 
	 * @param context
	 * @param newsList
	 * @param res
	 */
	public NewsListItemAdapter(Context context,
			List<News> newsList) {
		this.newsList = newsList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return newsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		News news = newsList.get(position);

		if (convertView == null)
			view = inflater.inflate(R.layout.news_list_item, null);

		TextView title = (TextView) view.findViewById(R.id.textViewTitle);
		TextView description = (TextView) view.findViewById(R.id.textViewDescription);
		ImageView image = (ImageView) view.findViewById(R.id.imageViewImage);
		
		if (news != null) {
			title.setText(news.getTitle());
			description.setText(news.getDescription());
			if (news.getImageUrl().length() > 0) {
				DownloadImageTask downloadImageTask = new DownloadImageTask(image);
				downloadImageTask.execute(news.getImageUrl());
			}
		}
		return view;
	}

}
