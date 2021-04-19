package xyz.dean.practice.devartexplore.ipc

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class UserDataProvider : ContentProvider() {
    private lateinit var usersDB: SQLiteDatabase

    override fun onCreate(): Boolean {
        usersDB = UserDBHelper(context!!).writableDatabase
        usersDB.execSQL("insert into users values('zhangsan', 25)")
        usersDB.execSQL("insert into users values('lisi', 26)")
        return true
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        usersDB.insert("users", null, values).also {
            if (it != -1L) {
                context!!.contentResolver.notifyChange(uri, null)
            }
        }
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return usersDB.delete("users", selection, selectionArgs).also {
            if (it > 0) {
                context!!.contentResolver.notifyChange(uri, null)
            }
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return usersDB.query("users", projection, selection, selectionArgs, null, null, sortOrder, null)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return usersDB.update("users", values, selection, selectionArgs).also {
            if (it > 0) {
                context!!.contentResolver.notifyChange(uri, null)
            }
        }
    }

    companion object {
        const val AUTHORITY = "xyz.dean.practice.devartexplore.ipc.provider"
    }
}

private class UserDBHelper(context: Context)
    : SQLiteOpenHelper(context, "userdb", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table if not exists users(_username text primary key, _age integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}