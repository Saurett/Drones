package texium.mx.drones.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by texiumuser on 16/03/2016.
 */
public class BDTasksManager extends SQLiteOpenHelper {

    String sqlCreateTaskTable = "CREATE TABLE \"Tasks\" ( \t`task_dni`\tINTEGER PRIMARY KEY AUTOINCREMENT, \t`task_title`\tTEXT, \t`task_content`\tTEXT, \t`task_priority`\tINTEGER, \t`task_begin_date`\tTEXT, \t`task_end_date`\tTEXT, \t`task_id`\tINTEGER, \t`task_latitude`\tREAL, \t`task_longitude`\tREAL, \t`task_status`\tINTEGER, \t`task_user_id`\tINTEGER )";
    String sqlCreateUserTable = "CREATE TABLE `Users` ( \t`cve_user`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, \t`idUser`\tINTEGER, \t`userName`\tTEXT, \t`idActor`\tINTEGER, \t`actorName`\tTEXT, \t`actorType`\tINTEGER, \t`actorTypeName`\tTEXT, \t`idTeam`\tINTEGER, \t`teamName`\tTEXT, \t`latitude`\tREAL, \t`longitude`\tREAL, \t`lastTeamConnection`\tTEXT )";

    public BDTasksManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTaskTable);
        db.execSQL(sqlCreateUserTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS Users");

        db.execSQL(sqlCreateTaskTable);
        db.execSQL(sqlCreateUserTable);
    }
}
