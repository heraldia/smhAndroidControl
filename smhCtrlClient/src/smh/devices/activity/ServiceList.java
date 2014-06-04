package smh.devices.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Text;

import smh.devices.control.R;
import smh.devices.dao.ServiceDao;
import smh.devices.model.Service;

import android.R.integer;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ServiceList extends Activity {
	private ListView listView;
	private TextView ids;
	private List<Service> services;
	private ServiceDao serviceDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.servicelist);

		listView = (ListView)findViewById(R.id.listView);
		
        show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(this, Menu.class));
		}
		return super.onKeyDown(keyCode, event);
	}

	public String stringFormat(String s, int l) {
		String result = s;
		int supplyment = l - s.length();
		for (int i = 0; i < supplyment; i++) {
			s = s + "  ";
		}
		return s;
	}

	public String dataFormat(int i) {
		String s = "";

		if (i > 999) {
			s = i + 1 + "";
		} else if (i > 99) {
			s = i + 1 + "  ";
		} else if (i > 9) {
			s = i + 1 + "    ";
		} else {
			s = i + 1 + "      ";
		}
		return s;
	}
	
	private void show() {

		serviceDao = new ServiceDao(getApplicationContext());
		services = serviceDao.findAll();
		int sum = services.size();
		if (sum == 0) {
			Toast.makeText(ServiceList.this, "There is no corresponding services",
					Toast.LENGTH_SHORT).show();
		}
		else {
			show(services);
		}
	}

	public SimpleAdapter buildListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.servicelist, new String[] { "id", "ipAddress", "name" }, new int[] {
				R.id.ids, R.id.ipadd, R.id.serviceName, });
		return adapter;
	}

	public void show(List<Service> list) {
		Object[] o = list.toArray();

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.size(); i++) {

//			String idString = String.valueOf(((Service) o[i]).getId());
			String ipAddString = ((Service) o[i]).getIpAddress();
			String nameString = ((Service) o[i]).getName();


			HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
			
			tempHashMap.put("ipAddress", ipAddString);
			tempHashMap.put("name", nameString);
			tempHashMap.put("id", ((Service) o[i]).getId());
			
			data.add(tempHashMap);
		}
		SimpleAdapter adapter = buildListAdapter(this, data);
		listView.setAdapter(adapter);
	}
}
