package dk.gettodone.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import dk.gettodone.pro.data.ContentHelper;
import dk.gettodone.pro.data.Task;

public class UIHelper {
	public static void finishTask(final Activity activity, final Task task) {
		AlertDialog.Builder newContext = new AlertDialog.Builder(
				activity);
		newContext.setTitle("What's the next action?");
		final EditText input = new EditText(activity);
		input.setText(task.getTitle());
		newContext.setView(input);
		newContext.setPositiveButton("Create",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						ContentHelper.processTaskToDone(
								activity.getContentResolver(),
								task.getId(), input.getText()
										.toString());

						Toast.makeText(activity,
								"Done!", 1000).show();
					}
				});
		newContext.setNeutralButton("All done",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						ContentHelper.finishTask(
								activity.getContentResolver(),
								task.getId());

						Toast.makeText(activity,
								"Done!", 1000).show();
					}
				});
		newContext.setNegativeButton("Cancel", null);
		newContext.show();
	}
	
}
