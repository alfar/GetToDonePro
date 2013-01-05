package dk.gettodone.pro;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	
	private TextView target; 
	
	public TimePickerFragment(TextView target)
	{
		this.target = target;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		try {
			Date dt = DateFormat.getTimeFormat(getActivity()).parse(target.getText().toString());
			c.setTime(dt);
			
		} catch (ParseException e) {
		}

		// Use the current date as the default date in the picker
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of DatePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));			
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		target.setText(DateFormat.getTimeFormat(getActivity()).format(c.getTime()));		
	}
}
