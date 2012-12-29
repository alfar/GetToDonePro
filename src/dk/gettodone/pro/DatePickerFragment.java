package dk.gettodone.pro;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	
	private EditText target; 
	
	public DatePickerFragment(EditText target)
	{
		this.target = target;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		try {
			Date dt = DateFormat.getDateFormat(getActivity()).parse(target.getText().toString());
			c.setTime(dt);
			
		} catch (ParseException e) {
		}

		// Use the current date as the default date in the picker
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);			
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		target.setText(DateFormat.getDateFormat(getActivity()).format(c.getTime()));
	}
}
