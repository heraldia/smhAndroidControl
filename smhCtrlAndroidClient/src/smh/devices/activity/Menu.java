package smh.devices.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smh.devices.control.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Menu extends Activity implements OnItemClickListener {

	private String texts[] = null;
	private Integer images[] = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		images = new Integer[] {
				R.drawable.blinds,
				R.drawable.lamp,
				R.drawable.radio,
				R.drawable.ppt,
				R.drawable.player, 
				R.drawable.lock, 
				R.drawable.service,
				R.drawable.pc
		};
		texts = new String[] { "Blind","Lamp","Radio", "PPT", "Player",
				"Lock","Service","PC" };

		// fill out GridView
		GridView gridView = (GridView) findViewById(R.id.homeGrid);

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, fillMap(),
				R.layout.layout_gridview_item, new String[] { "imageView",
						"imageTitle" }, new int[] { R.id.imageView,
						R.id.imageTitle });
		gridView.setAdapter(simpleAdapter);
		// listen onItemClick event
		gridView.setOnItemClickListener((OnItemClickListener) this); // ?
	}

	public List<Map<String, Object>> fillMap() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0, j = texts.length; i < j; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imageView", images[i]);
			map.put("imageTitle", texts[i]);
			list.add(map);
		}
		return list;
	}

	/*
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}
*/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int idx, long arg3) {
		// TODO Auto-generated method stub
		switch (idx) {
		case 0:
			startActivity(new Intent(this, Blind.class));
			break;
		case 1:
			Intent intent1 = new Intent();
			intent1.setClass(this, Lamp.class);
			startActivity(intent1);
			break;
			
		case 2:
			startActivity(new Intent(this, Radio.class));
			break;
			
		case 3:
			startActivity(new Intent(this, PPT.class));
			break;

		case 4:
			startActivity(new Intent(this, AudioPlayer.class));
			break;
			
		case 5:
			openWebURL("http://cycurity.cs.iastate.edu");
			break;
			
		case 6:
			startActivity(new Intent(this, ServiceList.class));
			break;
			
		case 7:
			startActivity(new Intent(this, PcList.class));
			break;
			
		default:
			break;
		}
	}
	
	 /**
     * Back 
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {	
		if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Exit app");
			builder.setMessage("You will exit the app...");
			//builder.setIcon(R.drawable.stat_sys_warning);
			builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME); 
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity(startMain);
					System.exit(0);
				}
			});
			builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}
    private void openWebURL(String inURL){
    	Intent browseIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(inURL));
    	startActivity(browseIntent);
    }

}
