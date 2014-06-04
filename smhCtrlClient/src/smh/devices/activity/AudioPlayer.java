package smh.devices.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import android.view.MotionEvent;
import smh.devices.control.Choices;
import smh.devices.control.Constant;
import smh.devices.control.R;
import smh.devices.dao.PcDao;
import smh.devices.dao.ServiceDao;
import smh.devices.model.Pc;
import smh.devices.model.Service;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPlayer extends Activity {
	private int serviceid = Constant.AUDIOPLAYERID;
	private Button forward;
	private Button back;
	private Button cleft;
	private Button cright;
	private Button space;
	private Button up;
	private Button down;
	private Button connectButton;	
	private EditText IPText;
	private Context mContext;
	private boolean isConnecting = false;
	
	private Socket sock = null;
	private ObjectOutputStream fromClient;
	private ObjectInputStream fromServer;
	private String ipString;
	

	private ImageButton ibtn_dropDown;
	private PopupWindow pop;
	private PopupAdapter adapter;
	private ListView listView;
	private boolean isShow = false;
	private List<Pc> pcs;
	ServiceDao serviceDao = null;
    Service service = null;
    PcDao pcDao = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.audioplayer);
	        mContext=this;
	        
		  back = (Button)this.findViewById(R.id.back);
		  forward = (Button)this.findViewById(R.id.forward);
		  cleft = (Button)this.findViewById(R.id.cleft);
	      cright = (Button)this.findViewById(R.id.cright);
	      space = (Button)this.findViewById(R.id.space);
	      up = (Button)this.findViewById(R.id.up);
	      down = (Button)this.findViewById(R.id.down);
	      back.setEnabled(false);
	      forward.setEnabled(false);
	      cleft.setEnabled(false);
	      cright.setEnabled(false);
	      space.setEnabled(false);
	      up.setEnabled(false);
	      down.setEnabled(false);
	      

		ibtn_dropDown = (ImageButton) findViewById(R.id.ibtn_dropDown);
		setUpIbtnListeners();
		pcDao = new PcDao(getApplicationContext());
		pcs = pcDao.findAll();
		serviceDao = new ServiceDao(getApplicationContext());
		service = serviceDao.findById(serviceid);
		
		if(service!=null){
		ipString = service.getIpAddress();
		}else {
			ipString="192.168.1.9";	
		}

        IPText= (EditText)findViewById(R.id.ipText);
        IPText.setText(ipString);

        connectButton= (Button)findViewById(R.id.connectButton);
        connectButton.setOnClickListener(ConnectClickListener);

//			ipString = "192.168.1.9";
//			sockInit(ipString);
	      
	      cleft.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					choiceMode(Constant.LEFT);
				}
	        });
	        cright.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					choiceMode(Constant.RIGHT);
				}
	        });
	        back.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					choiceMode(Constant.CTRLLEFT);
				}
	        });
	        forward.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					choiceMode(Constant.CTRLRIGHT);
				}
	        });
	        up.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					choiceMode(Constant.UP);
				}
	        });
	        down.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					choiceMode(Constant.DOWN);
				}
	        });
	        space.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					choiceMode(Constant.SPACE);
				}});
	        

	        
	}
	private void choiceMode(int arg) {
		Choices choice = new Choices(arg);
		try {
			fromClient.writeObject(choice);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private OnClickListener ConnectClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
		connInit();
		}
	};
    /*
	private void sockInit(String ipString) {
	    if(sock == null){
			try {
	        	sock = new Socket(InetAddress.getByName(ipString),5000);
	        	fromClient = new ObjectOutputStream(sock.getOutputStream());
				fromServer = new ObjectInputStream(sock.getInputStream());
				} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else{
			Toast.makeText(AudioPlayer.this, "Connected!", Toast.LENGTH_SHORT).show();
		}
		
		}
        */

	private void connInit() {
		if (isConnecting) 
		{				
			isConnecting = false;
			try {
				if(sock!=null)
				{
					sock.close();
					sock = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			connectButton.setText("Connecting");					
			IPText.setEnabled(true);		
		}
		else
		{				
			isConnecting = true;
			connectButton.setText("Stop connecting");
	        ipString=IPText.getText().toString();
	        Log.e("<2> + ipString",ipString);
	        try {
				sock = new Socket(InetAddress.getByName(ipString),5000);
			 	fromClient = new ObjectOutputStream(sock.getOutputStream());
				fromServer = new ObjectInputStream(sock.getInputStream());
				   serviceDao.update(ipString, serviceid);
			        if(pcDao.emptyByIp(ipString)){
			        	pcDao.add(new Pc(ipString));
			        }
				Toast.makeText(mContext, "Connected!", Toast.LENGTH_SHORT).show();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) { 
				e.printStackTrace();
			}
	     
	        
			IPText.setEnabled(false);
		      back.setEnabled(true);
		      forward.setEnabled(true);
		      cleft.setEnabled(true);
		      cright.setEnabled(true);
		      space.setEnabled(true);
		      up.setEnabled(true);
		      down.setEnabled(true);
		}
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				if(sock!=null)
				{
					sock.close();
					sock = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}


	public void setUpIbtnListeners() {
		ibtn_dropDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pop == null) {
					if (adapter == null) {
						adapter = new PopupAdapter(AudioPlayer.this);
						listView = new ListView(AudioPlayer.this);
						pop = new PopupWindow(listView, IPText.getWidth(), LayoutParams.WRAP_CONTENT);
						listView.setAdapter(adapter);
						pop.showAsDropDown(IPText);
						isShow = true;
					}
				} else if (isShow) {
					pop.dismiss();
					isShow = false;
				} else if (!isShow) {
					pop.showAsDropDown(IPText);
					isShow = true;
				}
			}

		});
	}
	
	class PopupAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;
		private Context context;

		public PopupAdapter() {

		}

		public PopupAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return pcs.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			final String pcString = pcs.get(position).getIpAddress();
			if (convertView == null) {
				layoutInflater = LayoutInflater.from(context);
				convertView = layoutInflater.inflate(R.layout.popup, null);
				holder = new Holder();
				holder.tv = (TextView) convertView.findViewById(R.id.tv_account);
				holder.ibtn = (ImageButton) convertView.findViewById(R.id.ibtn_delete);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if (holder != null) {
				convertView.setId(position);
				holder.setId(position);
				holder.tv.setText(pcString);
				holder.tv.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						pop.dismiss();
						isShow = false;
						IPText.setText(pcString);
						return true;
					}
				});

				holder.ibtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pcs.remove(position);
						adapter.notifyDataSetChanged();
					}
				});
			}
			return convertView;
		}

		class Holder {
			TextView tv;
			ImageButton ibtn;

			void setId(int position) {
				tv.setId(position);
				ibtn.setId(position);
			}
		}

	}
}
