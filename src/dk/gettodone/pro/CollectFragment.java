package dk.gettodone.pro;

import dk.gettodone.pro.data.TasksDataSource;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CollectFragment extends Fragment {
	private TasksDataSource datasource;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View result = inflater.inflate(R.layout.collect, container, false);
		
		Button buttonCollect = (Button)result.findViewById(R.id.buttonCollect);

		buttonCollect.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				EditText textCollect = (EditText)getView().findViewById(R.id.textCollect);
				datasource.createTask(textCollect.getText().toString());
				textCollect.setText("");
			}
		});
		
		return result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new TasksDataSource(getActivity());
		datasource.open();

	}

	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}
}
