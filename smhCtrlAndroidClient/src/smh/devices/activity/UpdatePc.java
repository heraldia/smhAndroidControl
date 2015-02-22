package smh.devices.activity;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import smh.devices.control.R;
import smh.devices.dao.PcDao;
import smh.devices.model.Pc;

public class UpdatePc extends Activity{
	private EditText ipAddrEditText;
	private EditText infoEditText;
	private Button addButton;
	private PcDao pcDao;
	private Pc pc = null;
	private int id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatepc);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		this.id = bundle.getInt("id");
		
		ipAddrEditText = (EditText) findViewById(R.id.ipAddres);
		infoEditText = (EditText) findViewById(R.id.info);
		addButton = (Button) findViewById(R.id.update);
		addButton.setOnClickListener(new updateButtonListener());
		
		pcDao = new PcDao(getApplicationContext());
		pc = pcDao.findById(id);
//		Log.i("<1>", pc.getInfo());
		if(pc!=null){
			infoEditText.setText(pc.getInfo());
			ipAddrEditText.setText(pc.getIpAddress());
		}else {
			infoEditText.setText("Empty");
			ipAddrEditText.setText("Empty");
		}
	}
	
	class updateButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String ipAddrString = ipAddrEditText.getText().toString();
			String infoString = infoEditText.getText().toString();
			
			
			String ipaddString = ipAddrEditText.getText().toString().trim();
			if (ipaddString.equals("")) {
				Toast.makeText(UpdatePc.this, "Please input ip address",
						Toast.LENGTH_SHORT).show();
			}
			if (!ipaddString.equals("")) {
//				PcDao pcDao = new PcDao(getApplicationContext());
				Pc pc = new Pc(id, ipaddString, infoString);
//				pc.setInfo(infoString);
//				pc.setIpAddress(ipaddString);
				 Toast.makeText(UpdatePc.this, pc.getId()+" "+ pc.getIpAddress()+ "  " + pc.getInfo(),
				 Toast.LENGTH_SHORT).show();
				pcDao.update(pc);
				Toast.makeText(UpdatePc.this, "Updated a new pc",
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(UpdatePc.this, PcList.class));
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
