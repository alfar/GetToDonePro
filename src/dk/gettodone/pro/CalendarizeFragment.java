package dk.gettodone.pro;

import java.util.Calendar;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CalendarizeFragment extends DialogFragment implements
		OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View result = inflater.inflate(R.layout.calendarize, container, false);

		date = (EditText) result.findViewById(R.id.calendarize_date);
		time = (EditText) result.findViewById(R.id.calendarize_time);

		date.setText(DateFormat.getDateFormat(getActivity()).format(Calendar.getInstance().getTime()));
		
		Button dateButton = (Button) result
				.findViewById(R.id.calendarize_date_button);
		dateButton.setOnClickListener(this);

		timeButton = (Button) result
				.findViewById(R.id.calendarize_time_button);
		timeButton.setOnClickListener(this);

		CheckBox allDay = (CheckBox) result
				.findViewById(R.id.calendarize_allday);

		allDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				time.setEnabled(!isChecked);
				timeButton.setEnabled(!isChecked);
			}
		});
		
		Button cancelButton = (Button)result.findViewById(R.id.calendarize_cancel_button);
		cancelButton.setOnClickListener(this);

		return result;
	}

	private EditText date;
	private EditText time;
	private Button timeButton;

	public void showDatePicker(View v) {
		DialogFragment newFragment = new DatePickerFragment(date);
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void showTimePicker(View v) {
		DialogFragment newFragment = new TimePickerFragment(time);
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calendarize_date_button:
			showDatePicker(v);
			break;
		case R.id.calendarize_time_button:
			showTimePicker(v);
			break;
		case R.id.calendarize_cancel_button:
			this.dismiss();
		}
	}
}
