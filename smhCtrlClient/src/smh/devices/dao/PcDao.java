package smh.devices.dao;

import java.util.ArrayList;
import java.util.List;

import smh.devices.model.Pc;
import smh.devices.model.Service;


import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PcDao {
	private DBOpenHelper helper;
	private SQLiteDatabase db;

	public PcDao(Context context) {
		helper = new DBOpenHelper(context);
	}

	public void add(Pc pc) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"insert into pc (ipaddress, info) "
						+ "values (?,?)",
				new Object[] { pc.getIpAddress(), pc.getInfo()});
		db.close();
	}

	public void update(Pc pc, int id) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update pc set ipaddress = ?, info = ? where id = ?",
				new Object[] { pc.getIpAddress(), pc.getInfo() });
		db.close();
	}
	public void update(Pc pc) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update pc set ipaddress = ?, info = ? where id = ?",
				new Object[] { pc.getIpAddress(), pc.getInfo(), pc.getId() });
		db.close();
	}

	public List<Pc> findAll() {
		List<Pc> pcs = new ArrayList<Pc>();
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select id, ipaddress, info from pc",null);
		while (cursor.moveToNext()) {
			pcs.add(new Pc(cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("ipaddress")), cursor
							.getString(cursor.getColumnIndex("info"))));
		}
		cursor.close();
		db.close();
		return pcs;
	}
	

	public Pc findById(int id) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select id, ipaddress, info from pc "
						+ "where id = ?", new String[] { String.valueOf(id) });
		 
		if (cursor.moveToNext()) {
			Pc pc = new Pc(cursor.getInt(cursor
					.getColumnIndex("id")), cursor.getString(cursor
					.getColumnIndex("ipaddress")), cursor.getString(cursor
					.getColumnIndex("info")));
			cursor.close();
			db.close();
			return pc;
		}
		return null;
	}

	public void delete(int id) {
		db = helper.getWritableDatabase();
		db.execSQL("delete from pc where id = ? ",
				new String[] { String.valueOf(id) });
		db.close();
	}
	
	public Boolean emptyById(int id) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select info from pc where id = ?",
				new String[] { String.valueOf(id) });
		if (cursor.moveToNext()) {
			cursor.close();
			db.close();
			return false;
		}
		cursor.close();
		db.close();
		return true;
	}
	
	public Boolean emptyByIp(String ipaddString) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select info from pc where ipaddress = ?",
				new String[] { ipaddString });
		if (cursor.moveToNext()) {
			cursor.close();
			db.close();
			return false;
		}
		cursor.close();
		db.close();
		return true;
	}

}
