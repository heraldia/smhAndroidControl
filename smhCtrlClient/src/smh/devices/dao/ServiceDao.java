package smh.devices.dao;

import java.util.ArrayList;
import java.util.List;

import smh.devices.model.Service;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ServiceDao {
	private DBOpenHelper helper;
	private SQLiteDatabase db;

	public ServiceDao(Context context) {
		helper = new DBOpenHelper(context);
	}

	public void add(Service service) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"insert into service (ipaddress, name) "
						+ "values (?,?)",
				new Object[] { service.getIpAddress(), service.getName()});
		db.close();
	}

	public void update(Service service, int id) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update service set ipaddress = ?, name = ? where id = ?",
				new Object[] { service.getIpAddress(), service.getName(),String.valueOf(id)});
		db.close();
	}

	public void update(String ipString, int id) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update service set ipaddress = ? where id = ?",
				new Object[] { ipString, String.valueOf(id)});
		db.close();
	}
	
	public List<Service> findAll() {
		List<Service> services = new ArrayList<Service>();
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select id, ipaddress, name from service",null);
		while (cursor.moveToNext()) {
			services.add(new Service(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("ipaddress")), cursor
							.getString(cursor.getColumnIndex("name"))));
		}
		cursor.close();
		db.close();
		return services;
	}

	public Service findById(int id) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select id, ipaddress, name from service "
						+ "where id = ?", new String[] { String.valueOf(id) });
		 
		if (cursor.moveToNext()) {
			Service service = new Service(cursor.getInt(cursor
					.getColumnIndex("id")), cursor.getString(cursor
					.getColumnIndex("name")), cursor.getString(cursor
					.getColumnIndex("ipaddress")));
			cursor.close();
			db.close();
			return service;
		}
		return null;
	}

	public void delete(int id) {
		db = helper.getWritableDatabase();
		db.execSQL("delete from service where id = ? ",
				new String[] { String.valueOf(id) });
		db.close();
	}
	
	 public int getCount()
	 {
	 db = helper.getWritableDatabase();
	 Cursor cursor = db.rawQuery("select count(id) from service", null);
	 if (cursor.moveToNext())
	 {
	 return cursor.getInt(0);
	 }
	 return 0;
	 }

}
