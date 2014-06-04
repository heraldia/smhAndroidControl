package smh.devices.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import smh.devices.control.Choices;
import smh.devices.control.Constant;
import smh.devices.control.R;
import smh.devices.control.R.id;
import smh.devices.control.R.layout;
import smh.devices.dao.PcDao;
import smh.devices.dao.ServiceDao;
import smh.devices.model.Pc;
import smh.devices.model.Service;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;

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

public class PPT extends Activity {
	private int serviceid = Constant.PPTID;
	
	private Button start;
	private Button back;
	private Button forward;
	private Button escape;
	private Button connectButton;
	private EditText IPText;
	private ImageButton ibtn_dropDown;
	private PopupWindow pop;
	private PopupAdapter adapter;
	private ListView listView;
	private boolean isShow = false;
	private List<Pc> pcs;
	ServiceDao serviceDao = null;
    Service service = null;
    PcDao pcDao = null;
	private String ipString;
	
	private boolean isConnecting = false;
	private Context mContext;
	
	private Socket sock = null;
	private ObjectOutputStream fromClient;
	private ObjectInputStream fromServer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppt);
        mContext=this;
        

		ibtn_dropDown = (ImageButton) findViewById(R.id.ibtn_dropDown);
	
		setUpIbtnListeners();
	
		pcDao = new PcDao(getApplicationContext());
	
		pcs = pcDao.findAll();

		serviceDao = new ServiceDao(getApplicationContext());
		service = serviceDao.findById(serviceid);

        IPText= (EditText)findViewById(R.id.ipText);
        //IPText.setText("10.0.2.15:");

		if(service!=null){
		ipString = service.getIpAddress();
		}else {
			ipString="192.168.1.9";	
		}

        IPText= (EditText)findViewById(R.id.ipText);
        IPText.setText(ipString);
        
        connectButton= (Button)findViewById(R.id.connectButton);
        connectButton.setOnClickListener(ConnectClickListener);
      
       /*  if(sock==null){
        try {
//        	Log.e("<1> + ipString",IPText.getText().toString());
//        	Log.e("<1> + ipString",ipString);
        	sock = new Socket(InetAddress.getByName(IPText.getText().toString()),5000);
        	fromClient = new ObjectOutputStream(sock.getOutputStream());
			fromServer = new ObjectInputStream(sock.getInputStream());
			} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        }
        */
        
        start = (Button)this.findViewById(R.id.start);
        escape = (Button)this.findViewById(R.id.escape);
        forward = (Button)this.findViewById(R.id.forward);
        back = (Button)this.findViewById(R.id.back);
      
		 back.setEnabled(false);
	      forward.setEnabled(false);
	      start.setEnabled(false);
	      escape.setEnabled(false);
	  
        start.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Choices choice = new Choices(Constant.SHIFTF5);
				try {
					fromClient.writeObject(choice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        	
        });
        
        escape.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Choices  choice = new Choices(Constant.ESC);
				try {
					fromClient.writeObject(choice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        });
        forward.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Choices choice = new Choices(Constant.RIGHT);
				try {
					fromClient.writeObject(choice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        });
        back.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Choices choice = new Choices(Constant.LEFT);
				try {
					fromClient.writeObject(choice);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        });
       
        
    }
    
    private OnClickListener ConnectClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
		connInit();
		}
	};
	
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
	        Log.i("<2> + ipString",ipString);
	        try {
				sock = new Socket(InetAddress.getByName(ipString),5000);
			 	fromClient = new ObjectOutputStream(sock.getOutputStream());
				fromServer = new ObjectInputStream(sock.getInputStream());
				 serviceDao.update(ipString, serviceid);
			        if(pcDao.emptyByIp(ipString)){
			        	pcDao.add(new Pc(ipString));
			        }
			        Log.i("<3> + ipString",ipString);
				Toast.makeText(mContext, "Connected!", Toast.LENGTH_SHORT).show();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) { 
				e.printStackTrace();
			}
	        
	        serviceDao.update(ipString, serviceid);
	        if(pcDao.emptyByIp(ipString)){
	        	pcDao.add(new Pc(ipString));
	        }
	        
			IPText.setEnabled(false);
			 back.setEnabled(true);
		      forward.setEnabled(true);
		      start.setEnabled(true);
		      escape.setEnabled(true);
		     
		}
		
	}

	class ShakeDo implements OnClickListener {
		@Override
		public void onClick(View v) {
			ipString=IPText.getText().toString();

				try {
					if(sock!=null)
					{
						sock.close();
						sock = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

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
						adapter = new PopupAdapter(PPT.this);
						listView = new ListView(PPT.this);
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
