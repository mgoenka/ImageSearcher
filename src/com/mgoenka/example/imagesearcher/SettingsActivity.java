package com.mgoenka.example.imagesearcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
	int filterSize = 0;
	int filterColor = 0;
	int filterType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras();
		
		filterSize = extras.getInt("size");
		filterColor = extras.getInt("color");
		filterType = extras.getInt("type");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		Spinner sSize = (Spinner) findViewById(R.id.sSize);
		Spinner sColor = (Spinner) findViewById(R.id.sColor);
		Spinner sType = (Spinner) findViewById(R.id.sType);
		
		sSize.setOnItemSelectedListener(this);
		sColor.setOnItemSelectedListener(this);
		sType.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> aaSize = ArrayAdapter.createFromResource(this,
		        R.array.array_size, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> aaColor = ArrayAdapter.createFromResource(this,
		        R.array.array_color, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> aaType = ArrayAdapter.createFromResource(this,
		        R.array.array_type, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		aaSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aaColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aaType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		sSize.setAdapter(aaSize);
		sColor.setAdapter(aaColor);
		sType.setAdapter(aaType);
		
		sSize.setSelection(filterSize);
		sColor.setSelection(filterColor);
		sType.setSelection(filterType);
		
		EditText etSite = (EditText) findViewById(R.id.etSite);
		etSite.setText(getIntent().getStringExtra("site"));
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		int spinnerId = parent.getId();
		
		switch (spinnerId) {
		case R.id.sSize:
			filterSize = pos;
			break;
		case R.id.sColor:
			filterColor = pos;
			break;
		case R.id.sType:
			filterType = pos;
			break;
		default:
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		
		i.putExtra("size", filterSize);
		i.putExtra("color", filterColor);
		i.putExtra("type", filterType);
		i.putExtra("site", ((EditText) findViewById(R.id.etSite)).getText().toString());
		
		setResult(RESULT_OK, i);
		finish();
	}
}
