package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class TodoListManagerActivity extends Activity {

	private static final int ADD_NEW_TODO_ITEM_REQ_CODE = 3333;

	private static final String CALL_STRING = "Call ";

	private static final CharSequence TELL_STRING = "tel:";

	private ListView todoList;
	private ArrayAdapter<ITodoItem> todoListAdapter;
	private List<ITodoItem> todos = new ArrayList<ITodoItem>();
	
	private TodoDAL todoDAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		//Initialize the DB access class and populate items from previous sessions. 
		todoDAL = new TodoDAL(this);
		todos.addAll(todoDAL.all());
		
		todoList = (ListView) findViewById(R.id.lstTodoItems);
		todoListAdapter = new TodoListDisplayAdapter(this, todos);

		todoList.setAdapter(todoListAdapter);

		registerForContextMenu(todoList);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.contex_menu, menu);
		
		AdapterContextMenuInfo ctxMenuInfo = (AdapterContextMenuInfo) menuInfo;
		
		TextView titleView = (TextView) ctxMenuInfo.targetView.findViewById(R.id.txtTodoTitle);
		String title = titleView.getText().toString();
		
		MenuItem call = menu.findItem(R.id.menuItemCall);
		
		menu.setHeaderTitle(title);
		
		if (title.startsWith(CALL_STRING)) {
	        call.setTitle(title);
			return;
		}
		
		menu.removeItem(R.id.menuItemCall);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		TodoListItem listItem = (TodoListItem) todoList.getItemAtPosition(info.position);
		switch(item.getItemId()) {

		case R.id.menuItemDelete:
			todoDAL.delete(listItem);
			todoListAdapter.remove(listItem);
			break;

		case R.id.menuItemCall:
			
			String phoneNumber = listItem.getTitle().replace(CALL_STRING, TELL_STRING);
			
			Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
			startActivity(dialIntent);
			break;
			
		}

		return true;
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

			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, ADD_NEW_TODO_ITEM_REQ_CODE);

			return true;

		}


		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		final Resources res = getResources();

		switch(requestCode) {

		case ADD_NEW_TODO_ITEM_REQ_CODE:
			//You said that it is possible to get no date..
			if (resultCode == RESULT_OK && data.hasExtra(res.getString(R.string.title))) {
				Date dueDate = null;
				if (data.getSerializableExtra(res.getString(R.string.dueDate)) != null) {
					dueDate = (Date) data.getSerializableExtra(res.getString(R.string.dueDate));
				}
				TodoListItem newTodo = new TodoListItem(data.getStringExtra(res.getString(R.string.title)),	dueDate);
				if (todoDAL.insert(newTodo)) { todoListAdapter.add(newTodo); }
			}
			break;

		}
	}
}


