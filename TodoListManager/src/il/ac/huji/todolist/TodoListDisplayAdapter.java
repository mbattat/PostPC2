package il.ac.huji.todolist;

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
		int color = ((position % 2) == 0) ? Color.RED : Color.BLUE;
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView txtName = (TextView)view.findViewById(R.id.todoItem);
		
		txtName.setText(todos.getTask());
		txtName.setTextColor(color);
		return view;
	}
}
