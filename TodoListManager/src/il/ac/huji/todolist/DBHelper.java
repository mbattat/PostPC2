package il.ac.huji.todolist;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

	private static final int VERSION_ONE = 1;
	private static final String DEFAULT_NAME = "todo_db";

	public DBHelper(Context context) {
		super(context, DEFAULT_NAME, null, VERSION_ONE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table todo (_id integer primary key autoincrement, title text NOT NULL, due long);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) { }

}
