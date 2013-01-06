package dk.gettodone.pro;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import dk.gettodone.pro.data.ContentHelper;
import dk.gettodone.pro.data.Task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

public class CalendarizeFragment extends DialogFragment implements
		OnClickListener, DialogInterface.OnClickListener {

	public CalendarizeFragment() {
	}

	public static CalendarizeFragment newInstance(Task task) {
		CalendarizeFragment result = new CalendarizeFragment();
		Bundle args = new Bundle();
		args.putLong("Id", task.getId());
		args.putString("Title", task.getTitle());
		result.setArguments(args);

		return result;
	}

	private long taskId;
	private String taskTitle;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity()).setTitle(taskTitle)
				.setView(createFragmentView())
				.setPositiveButton(R.string.calendarize_send, this)
				.setNegativeButton(R.string.cancel, this).create();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskId = getArguments().getLong("Id");
		taskTitle = getArguments().getString("Title");
	}

	private View createFragmentView() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		// Inflate the layout for this fragment
		View result = inflater.inflate(R.layout.calendarize, null);

		date = registerAsOnClickListener(result, R.id.calendarize_date_button);
		time = registerAsOnClickListener(result, R.id.calendarize_time_button);

		date.setText(DateFormat.getDateFormat(getActivity()).format(
				Calendar.getInstance().getTime()));

		time.setText(DateFormat.getTimeFormat(getActivity()).format(
				Calendar.getInstance().getTime()));

		allDay = (CheckBox) result.findViewById(R.id.calendarize_allday);

		allDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				time.setEnabled(!isChecked);
			}
		});

		calendarSpinner = (Spinner) result
				.findViewById(R.id.calendarize_calendar_spinner);
		populateCalendarPicker(calendarSpinner);

		return result;
	}

	private Button registerAsOnClickListener(View view, int id) {
		Button btn = (Button) view.findViewById(id);
		btn.setOnClickListener(this);
		return btn;
	}

	private Spinner calendarSpinner;
	private Button date;
	private Button time;
	private CheckBox allDay;

	public void populateCalendarPicker(Spinner spinner) {
		List<dk.gettodone.pro.data.Calendar> items = new ArrayList<dk.gettodone.pro.data.Calendar>();
		String[] projection = new String[] { Calendars._ID, Calendars.NAME,
				Calendars.CALENDAR_TIME_ZONE, Calendars.ACCOUNT_NAME,
				Calendars.ACCOUNT_TYPE };
		Cursor calCursor = getActivity().getContentResolver().query(
				Calendars.CONTENT_URI, projection, Calendars.VISIBLE + " = 1",
				null, Calendars._ID + " ASC");
		if (calCursor.moveToFirst()) {
			do {
				long id = calCursor.getLong(0);
				String displayName = calCursor.getString(1);
				String timezone = calCursor.getString(2);
				items.add(new dk.gettodone.pro.data.Calendar(id, displayName,
						timezone));
			} while (calCursor.moveToNext());
		}

		ArrayAdapter<dk.gettodone.pro.data.Calendar> adapter = new ArrayAdapter<dk.gettodone.pro.data.Calendar>(
				getActivity(), android.R.layout.simple_list_item_1, items);
		spinner.setAdapter(adapter);
	}

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
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			long calendarId = ((dk.gettodone.pro.data.Calendar) calendarSpinner
					.getSelectedItem()).getId();
			String timezone = ((dk.gettodone.pro.data.Calendar) calendarSpinner
					.getSelectedItem()).getTimezone();
			Calendar startDate = Calendar.getInstance();
			startDate.setTimeZone(TimeZone.getTimeZone(timezone));
			try {
				startDate.setTime(DateFormat.getDateFormat(getActivity())
						.parse(date.getText().toString()));
				Calendar startTime = Calendar.getInstance();
				startTime.setTime(DateFormat.getTimeFormat(getActivity())
						.parse(time.getText().toString()));
				startDate.set(Calendar.HOUR_OF_DAY,
						startTime.get(Calendar.HOUR_OF_DAY));
				startDate.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ContentHelper.processTaskToCalendar(getActivity()
					.getContentResolver(), taskId, taskTitle, calendarId,
					startDate.getTime(), timezone, allDay.isChecked());
			this.dismiss();
		}
	}
}
