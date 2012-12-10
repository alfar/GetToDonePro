package dk.gettodone.pro;

import dk.gettodone.pro.data.TasksDataSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GetToDoneProCollectActivity extends Activity {
	private TasksDataSource datasource;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main);

		datasource = new TasksDataSource(this);
		datasource.open();

		Button buttonCollect = (Button)findViewById(R.id.buttonCollect);
		
		buttonCollect.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				EditText textCollect = (EditText)findViewById(R.id.textCollect);
				datasource.createTask(textCollect.getText().toString());
				textCollect.setText("");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.collect, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent; 
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent = new Intent(this, GetToDoneProCollectActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.item_do:
			intent = new Intent(this, GetToDoneProDoingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
}