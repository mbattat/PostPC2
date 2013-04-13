package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TodoDAL {

	private static final String TITLE = "title";
	private static final String DUE = "due";
	private static final String TODO = "todo";
	private static final long NOT_INSERTED = -1;


	private SQLiteDatabase _db;
	private DBHelper _dbHelper;

	/*
	 * Constructor.
	 * @context - the context.
	 */
	public TodoDAL (Context context) {

		//DB initialization.
		_dbHelper = new DBHelper(context);

		//Parse initialization.
		Resources res = context.getResources();
		Parse.initialize(context, res .getString(R.string.parseApplication), res.getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();
	}


	/*
	 * Stores a todo item in the SQLite DB and parse it.
	 * @todoItem - the item to process.
	 * @return true on success.
	 */
	public boolean insert(ITodoItem todoItem) {  

		final String title = todoItem.getTitle();
		final Date dueDate = todoItem.getDueDate();
		boolean inserted = false;

		_db = _dbHelper.getWritableDatabase();

		if (_db != null) {

			//SQLite DB insertion.
			ContentValues rowCV = new ContentValues();
			rowCV.put(TITLE, title);

			if (dueDate != null) 	{ rowCV.put(DUE, dueDate.getTime()); }
			else 				  	{ rowCV.putNull(DUE); }
			if (_db.insert(TODO, null, rowCV) != NOT_INSERTED ){
				inserted = true;
			}
			_db.close();

			//Parse cloud insertion.
			ParseObject rowParse = new ParseObject("todo");

			rowParse.put(TITLE, title);
			if (dueDate != null) 	{ rowParse.put(DUE, dueDate.getTime()); }
			else 	                { rowParse.put(DUE, JSONObject.NULL); }

			rowParse.saveInBackground();

		}	 

		return inserted;
	}


	/*
	 * Updates a given todoItem due date.
	 * @todoItem - the item to update.
	 * @return true on success.
	 */
	public boolean update(ITodoItem todoItem) { 

		final String title = todoItem.getTitle();
		final Date dueDate = todoItem.getDueDate();
		boolean updated = false;

		_db = _dbHelper.getWritableDatabase();

		if (_db != null) {

			//SQLite DB update.
			ContentValues rowCV = new ContentValues();			 
			if (dueDate != null) 	{ rowCV.put(DUE, dueDate.getTime()); }
			else 				  	{ rowCV.putNull(DUE); }

			if (_db.update(TODO, rowCV, TITLE + " = '" + title + "'", null) != 0) {
				updated = true;
			}

			_db.close();


			// update parse cloud
			ParseQuery query = new ParseQuery("todo");
			query.whereEqualTo(TITLE, title);

			query.findInBackground(new FindCallback() {			

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if(e == null){
						for(ParseObject parseObject : objects){

							if(dueDate != null)	{ parseObject.put(DUE, dueDate.getTime()); }
							else                { parseObject.put(DUE, JSONObject.NULL); }

							parseObject.saveInBackground();
						}
					}
				}
			});
		}

		return updated;
	}

	/*
	 * Deletes a given todoItem.
	 * @todoItem - the item to delete.
	 * @return true on success.
	 */
	public boolean delete(ITodoItem todoItem) { 

		final String title = todoItem.getTitle();
		final Date dueDate = todoItem.getDueDate();
		boolean deleted = false;

		_db = _dbHelper.getWritableDatabase();

		if (_db != null) {

			//SQLite DB deletion.
			ContentValues rowCV = new ContentValues();
			rowCV.put(TITLE, title);

			if (dueDate != null) 	{ rowCV.put(DUE, dueDate.getTime()); }
			else 				  	{ rowCV.putNull(DUE); }
			if (_db.delete(TODO, TITLE + " = '" + title + "'", null) != 0) {
				deleted = true;
			}
			_db.close();


			// delete from parse cloud
			ParseQuery query = new ParseQuery("todo");
			query.whereEqualTo(TITLE, title);
			//query.whereEqualTo(DUE, dueDate); TODO: can date be changed and then deleted or can be two identical titles 

			query.findInBackground(new FindCallback() {			

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if(e == null){
						for(ParseObject parseObject : objects){

							parseObject.deleteInBackground();

						}
					}
				}
			});
		}

		return deleted;
	}

	/*
	 * Returns a list of all stored items.
	 * @return a list of all stored items.
	 */
	public List<ITodoItem> all() {

		List<ITodoItem> todos = new ArrayList<ITodoItem>();

		_db = _dbHelper.getReadableDatabase();
		if (_db != null) {

			Cursor cursor = _db.query(TODO, new String[] { TITLE, DUE }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) {
				
				do {
					todos.add(new TodoListItem(cursor.getString(cursor.getColumnIndex(TITLE)), new Date(cursor.getLong(cursor.getColumnIndex(DUE)))));
				} while (cursor.moveToNext());
				
			}

			_db.close();

		}

		return todos;
	}

}
