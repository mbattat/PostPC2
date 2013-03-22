package il.ac.huji.todolist;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoListItem {
	
	private static final String FORMAT = "dd/MM/yyyy";
	private String _txtTodoTitle;
	private Date _txtTodoDueDate;
	
	public TodoListItem(String txtTodoTitle, Date date) {
		this._txtTodoTitle = txtTodoTitle;
		this._txtTodoDueDate = date;
	}

	public String getTitle() {
		return _txtTodoTitle;
	}
	
	public Date getDueDate() {
		return _txtTodoDueDate;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String dueDateToString() {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
		if((_txtTodoDueDate != null) || (_txtTodoDueDate instanceof Date)) {
			return sdf.format(_txtTodoDueDate);
		}
		else {
			return null;
		}
	}
	
}
