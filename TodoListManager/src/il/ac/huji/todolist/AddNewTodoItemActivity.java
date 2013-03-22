package il.ac.huji.todolist;

import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	protected static final int YEARS_SUBSTRACTION = 1900;

	public void onCreate(Bundle unused) {
		super.onCreate(unused);
		setContentView(R.layout.activity_add_new_todo_item);
		
		final Resources res = getResources();
		final Intent intent = new Intent();
		
		Button btnOK = (Button) findViewById(R.id.btnOK);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		
		btnOK.setOnClickListener(
				new View.OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {

						EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
						intent.putExtra(res.getString(R.string.title), edtNewItem.getText().toString());
						

						DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
						intent.putExtra(res.getString(R.string.dueDate), new Date(datePicker.getYear() - YEARS_SUBSTRACTION, datePicker.getMonth(), datePicker.getDayOfMonth()));
						
						setResult(RESULT_OK, intent);
						finish();
						
					}
				});

		btnCancel.setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						setResult(RESULT_CANCELED, intent);
						finish();
						
					}
				});

	}
	
}
