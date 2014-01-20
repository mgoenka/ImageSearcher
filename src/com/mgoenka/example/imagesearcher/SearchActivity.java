package com.mgoenka.example.imagesearcher;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	int filterSize = 0;
	int filterColor = 0;
	int filterType = 0;
	String filterSite = null;
	private final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	            // Triggered only when new data needs to be appended to the list
	            // Add whatever code is needed to append new items to your AdapterView
		        customLoadMoreDataFromApi(page); 
	            // or customLoadMoreDataFromApi(totalItemsCount); 
		    }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_searcher, menu);
		return true;
	}
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		
		watcher(etQuery, btnSearch);
	}
	public void onImageSearch(View v) {
		sendSearchQuery();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
	
	public void sendSearchQuery() {
		String query = Uri.encode(etQuery.getText().toString());
		
		if (filterSize > 0) {
			query += "&imgsz=";
			switch (filterSize) {
			case 1:
				query += "icon";
				break;
			case 2:
				query += "medium";
				break;
			case 3:
				query += "xxlarge";
				break;
			default:
				query += "huge";
			}
		}

		if (filterColor > 0) {
			query += "&imgcolor=";
			switch (filterColor) {
			case 1:
				query += "black";
				break;
			case 2:
				query += "blue";
				break;
			case 3:
				query += "brown";
				break;
			case 4:
				query += "gray";
				break;
			case 5:
				query += "green";
				break;
			case 6:
				query += "orange";
				break;
			case 7:
				query += "pink";
				break;
			case 8:
				query += "purple";
				break;
			case 9:
				query += "red";
				break;
			case 10:
				query += "teal";
				break;
			case 11:
				query += "white";
				break;
			default:
				query += "yellow";
			}
		}
		
		if (filterType > 0) {
			query += "&imgtype=";
			switch (filterType) {
			case 1:
				query += "face";
				break;
			case 2:
				query += "photo";
				break;
			case 3:
				query += "clipart";
				break;
			default:
				query += "lineart";
			}
		}
		
		if (filterSite != null) {
			query += "&as_sitesearch=" + filterSite;
		}
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.get("https://ajax.googleapis.com/ajax/services/search/images?"
				+ "rsz=8&start=" + 0 + "&v=1.0&q=" + query,
				new JsonHttpResponseHandler() {
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject(
									"responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult
									.fromJSONArray(imageJsonResults));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	public void onSettings(MenuItem mi) {
		Intent i = new Intent(SearchActivity.this, SettingsActivity.class);
		i.putExtra("size", filterSize);
		i.putExtra("color", filterColor);
		i.putExtra("type", filterType);
		i.putExtra("site", filterSite);
		startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // REQUEST_CODE is defined above
	    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	    	Bundle extras = data.getExtras();
	        filterSize = extras.getInt("size");
	        filterColor = extras.getInt("color");
	        filterType = extras.getInt("type");
	        filterSite = data.getStringExtra("site");
	    }
	    
	    sendSearchQuery();
	}
	
	protected void watcher(final EditText etQuery,final Button btnSearch)
	{
	    etQuery.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable s) { 
	            if (etQuery.length() == 0) {
	            	btnSearch.setEnabled(false);
	            } else {
	            	btnSearch.setEnabled(true);
	            	sendSearchQuery();
	            }
	        }

	        public void beforeTextChanged(CharSequence s, int start, int count, int after){
	        }
	        
	        public void onTextChanged(CharSequence s, int start, int before, int count){
	        }
	    }); 
	    
	    if (etQuery.length() == 0) {
	    	btnSearch.setEnabled(false);
	    }
	}
	
	public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter. 
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
    }
}
