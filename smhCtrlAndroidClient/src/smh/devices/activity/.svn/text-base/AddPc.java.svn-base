package smh.devices.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import smh.devices.control.R;
import smh.devices.dao.PcDao;
import smh.devices.model.Pc;

public class AddPc extends Activity{
	private EditText ipAddrEditText;
	private EditText infoEditText;
	private Button addButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addpc);
		
		ipAddrEditText = (EditText) findViewById(R.id.ipAddres);
		infoEditText = (EditText) findViewById(R.id.info);
		addButton = (Button) findViewById(R.id.add);
		addButton.setOnClickListener(new addButtonListener());
		
	}
	
	class addButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String ipAddrString = ipAddrEditText.getText().toString();
			String infoString = infoEditText.getText().toString();
			
			PcDao pcDao = new PcDao(getApplicationContext());
			String ipaddString = ipAddrEditText.getText().toString().trim();
			if (ipaddString.equals("")) {
				Toast.makeText(AddPc.this, "please input ip address",
						Toast.LENGTH_SHORT).show();
			}
			if (!ipaddString.equals("")) {
//				PcDao pcDao = new PcDao(getApplicationContext());
				Pc pc = new Pc(0, ipaddString, infoString);
				// Toast.makeText(newa.this, categoryvalue+"type",
				// Toast.LENGTH_SHORT).show();
				pcDao.add(pc);
				Toast.makeText(AddPc.this, "Added a new pc",
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(AddPc.this, PcList.class));
			}
			
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(this, PcList.class));
		}
		return super.onKeyDown(keyCode, event);
	}
}
