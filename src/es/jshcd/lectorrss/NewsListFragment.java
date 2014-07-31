package es.jshcd.lectorrss;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import es.jshcd.lectorrss.controller.DataBaseHandler;
import es.jshcd.lectorrss.controller.NewsDownloader;
import es.jshcd.lectorrss.model.News;
import es.jshcd.lectorrss.view.NewsListItemAdapter;

/**
 * 
 * @author Javier Sanchez Hernandez
 * @version 1.0
 */
public class NewsListFragment extends ListFragment {
	private SelectionListener mCallback;
	private SharedPreferences prefs;
	private List<News> newsList;
	private DataBaseHandler dbHandler;

	public interface SelectionListener {
		public void onItemSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		newsList = new ArrayList<News>();
		dbHandler = new DataBaseHandler(getActivity());

		prefs = getActivity().getSharedPreferences("rss", 0);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (SelectionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SelectionListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// When using two-pane layout, configure the ListView to highlight the
		// selected list item
		if (isInTwoPaneMode()) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id) {
		// Notify the hosting Activity that a selection has been made.
		mCallback.onItemSelected(position);
	}

	/**
	 * Indicates if the layout is two-pane.
	 * 
	 * @return true if there is a NewsDetailFragment and false otherwise.
	 */
	private boolean isInTwoPaneMode() {
		return getFragmentManager().findFragmentById(R.id.news_detail_frag) != null;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	/**
	 * Updates the data to show on the ListView
	 */
	private void loadData() {
		// Empty the ArrayList
		newsList.removeAll(newsList);

		if (isNetworkAvailable()) {
			// Download data from RSS Feed
			String feedURL = prefs.getString("data_source",
					getString(R.string.default_feed_url));
			// System.out.println("Feed: " + feedURL);

			NewsDownloader downloader = new NewsDownloader();
			String result;
			downloader.execute(feedURL);

			try {

				result = downloader.get();
				// System.out.println(result);
				// TODO Check if data correctly retrieved
				parse(result, dbHandler);

			} catch (InterruptedException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			} catch (ExecutionException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(), "No hay conexión de red",
					Toast.LENGTH_LONG).show();
		}

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

		// Set the list adapter for this NewsListFragment
		setListAdapter(new NewsListItemAdapter(getActivity(), newsList));
	}

	private void parse(String xml, DataBaseHandler dbHandler) {
		StringReader sr = new StringReader(xml);
		InputSource is = new InputSource(sr);
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			Document d = builder.parse(is);
			NodeList nodeList = d.getElementsByTagName("item");
			// System.out.println("The feed has " + nodeList.getLength() +
			// " items");
			
			dbHandler.createDataBase();
			// Open database
			dbHandler.openDataBase();
			// Empty database
			dbHandler.emptyDatabase();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				News news = new News();
				NodeList childNodes = nNode.getChildNodes();
				// System.out.println(childNodes.getLength());
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node mNode = childNodes.item(j);
					if (mNode.getNodeName().compareTo("title") == 0) {
						news.setTitle(mNode.getTextContent());
					} else if (mNode.getNodeName().compareTo("description") == 0) {
						news.setDescription(mNode.getTextContent());
					} else if (mNode.getNodeName().compareTo("link") == 0) {
						news.setLink(mNode.getTextContent());
					} else if (mNode.getNodeName().compareTo("content:encoded") == 0) {
						news.setContent(mNode.getTextContent());
					} else if (mNode.getNodeName().compareTo("enclosure") == 0) {
						NamedNodeMap attributes = mNode.getAttributes();
						Node url = attributes.getNamedItem("url");
						news.setImageUrl(url.getTextContent());
						// System.out.println(url.getTextContent());
					}
				}
				// Put data into database
				dbHandler.insertNews(news);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			dbHandler.close();
		}
	}

	/**
	 * Indicates if the device has a network connection or not.
	 * 
	 * @return true if the device has a connection to network and false
	 *         otherwise.
	 */
	private boolean isNetworkAvailable() {
		getActivity();
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
