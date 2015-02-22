package smh.devices.activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import smh.devices.control.R;
import smh.devices.dao.PcDao;
import smh.devices.dao.ServiceDao;
import smh.devices.model.Pc;
import smh.devices.model.Pc;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class PcList extends Activity {
//	private EditText ipAddrEditText;
//	private Button submitButton;
	private ListView listView;
	private List<Pc> pcs;
	private PcDao pcDao;
    public int MID; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pclist);
		listView = (ListView)findViewById(R.id.listView);
		show();
		/*
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				idt = (TextView) view.findViewById(R.id.idto);
				String ids = idt.getText().toString().trim();
				int id = Integer.parseInt(ids);
				datePicker = (DatePicker) findViewById(R.id.datePicker1);
				year = datePicker.getYear();
				month = datePicker.getMonth() + 1;
				day = datePicker.getDayOfMonth();
				Intent intent = new Intent();
				intent.setClass(day.this, update.class);
				intent.putExtra("id", id);
				intent.putExtra("dateType", dateType);
				intent.putExtra("year", year);
				intent.putExtra("month", month);
				intent.putExtra("day", day);
				startActivity(intent);
			}
		});*/
        ItemOnLongClick1(); 

	}
	
	 private void ItemOnLongClick1() { 
	        //注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的 
	       listView 
	            .setOnCreateContextMenuListener(new OnCreateContextMenuListener() { 
	                public void onCreateContextMenu(ContextMenu menu, View v, 
	                    ContextMenuInfo menuInfo) { 
	                menu.add(0, 0, 0, "Update"); 
	                menu.add(0, 1, 0, "Delete"); 
	                menu.add(0, 2, 0, "Add"); 
	               }});

	    } 
	    // 长按菜单响应函数 
	    public boolean onContextItemSelected(MenuItem item) { 
	        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item 
	            .getMenuInfo(); 
//	        MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值 
	        MID = Integer.parseInt(((TextView) info.targetView.findViewById(R.id.id)).getText().toString().trim());
	        Log.i("<1>",  String.valueOf(MID));
	        
	        switch (item.getItemId()) { 
	        case 0: 
	            Toast.makeText(PcList.this, 
	                "Update", Toast.LENGTH_SHORT).show(); 
	            Intent intent = new Intent(this, UpdatePc.class);
			    intent.putExtra("id", MID);
	            startActivity(intent);
			    finish();
	            break; 
	        case 1: 
	        	 PcDao pcDao = new PcDao(getApplicationContext());
	        		if (!pcDao.emptyById(MID))
	        		{
	        			pcDao.delete(MID);
	        		    Toast.makeText(PcList.this,"Delete successfully",
	        			    Toast.LENGTH_SHORT).show();
	        		} else {
	        		    Toast.makeText(PcList.this, "This item has been deleted",
	        			    Toast.LENGTH_SHORT).show();
	        		}
	    		    finish();
	            break; 
	        case 2: 
	        	 startActivity(new Intent(this, AddPc.class));
	 		    finish();
	            break; 
	        default: 
	            break; 
	        } 
	        return super.onContextItemSelected(item); 
	    } 
	    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(this, Menu.class));
		}
		return super.onKeyDown(keyCode, event);
	}
	private void show() {
		pcDao = new PcDao(getApplicationContext());
		pcs = pcDao.findAll();
		int sum = pcs.size();
		if (sum == 0) {
			Toast.makeText(PcList.this, "There is no corresponding pcs",
					Toast.LENGTH_SHORT).show();
		}
		else {
			show(pcs);
		}
	}
	public SimpleAdapter buildListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.pclist, new String[] { "id", "ipaddress", "info" }, new int[] {
				R.id.id, R.id.ipaddress, R.id.info });
		return adapter;
	}
	public void show(List<Pc> list) {
		Object[] o = list.toArray();
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
//			String idString = String.valueOf(((Pc) o[i]).getId());
			String ipAddString = ((Pc) o[i]).getIpAddress();
			String infoString = ((Pc) o[i]).getInfo();
			HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
			tempHashMap.put("ipaddress", ipAddString);
			tempHashMap.put("info", infoString);
			tempHashMap.put("id", ((Pc) o[i]).getId());
			data.add(tempHashMap);
		}
		SimpleAdapter adapter = buildListAdapter(this, data);
		listView.setAdapter(adapter);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		menu.add(0, 1, 1, "Add a new pc");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 if(item.getItemId() == 1){    
			 startActivity(new Intent(this, AddPc.class));
			 }
		return super.onOptionsItemSelected(item);
	}
}
