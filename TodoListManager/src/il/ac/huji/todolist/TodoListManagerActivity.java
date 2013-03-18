package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class TodoListManagerActivity extends Activity {

	private ListView todoList;
	private ArrayAdapter<TodoListItem> todoListAdapter;
	private List<TodoListItem> todos = new ArrayList<TodoListItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		todoList = (ListView) findViewById(R.id.lstTodoItems);
		todoListAdapter = new TodoListDisplayAdapter(this, todos);

		todoList.setAdapter(todoListAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {

		case R.id.menuItemAdd:

			EditText todoItem = (EditText) findViewById(R.id.edtNewItem);
			todoListAdapter.add(new TodoListItem(todoItem.getText().toString()));
			todoItem.setText("");
			return true;

		case R.id.menuItemDelete:

			todoListAdapter.remove((TodoListItem) todoList.getSelectedItem());

			return true;

		}


		return super.onOptionsItemSelected(item);
	}

}


