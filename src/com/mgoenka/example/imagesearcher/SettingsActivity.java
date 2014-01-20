package com.mgoenka.example.imagesearcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		Spinner sSize = (Spinner) findViewById(R.id.sSize);
		Spinner sColor = (Spinner) findViewById(R.id.sColor);
		Spinner sType = (Spinner) findViewById(R.id.sType);

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
		
		//TextView tvLabel = (TextView) findViewById(R.id.tvLabel);
		//tvLabel.setText(getIntent().getStringExtra("label"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
