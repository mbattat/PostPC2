package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListDisplayAdapter extends ArrayAdapter<TodoListItem> {

	public TodoListDisplayAdapter(TodoListManagerActivity activity, List<TodoListItem> todos) {
		super(activity, android.R.layout.simple_list_item_1, todos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoListItem todos = getItem(position);
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView txtTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView txtDueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		
		
		txtTitle.setText(todos.getTitle());
		txtTitle.setTextColor(Color.BLACK);
		
		String dueDate = todos.dueDateToString();
		
		if (dueDate != null) {
			txtDueDate.setText(dueDate);
			
			Date now = new Date();
			if (todos.getDueDate().before(now)) {
				txtTitle.setTextColor(Color.RED);
				txtDueDate.setTextColor(Color.RED);
			}
			else{
				txtDueDate.setTextColor(Color.BLACK);
			}
		}
		
		return view;
	}
}
