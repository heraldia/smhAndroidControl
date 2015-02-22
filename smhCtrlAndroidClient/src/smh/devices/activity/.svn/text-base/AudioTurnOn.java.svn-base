package smh.devices.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import smh.devices.control.Choices;
import smh.devices.control.Constant;
import smh.devices.control.R;
import smh.devices.model.Pc;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class AudioTurnOn extends Activity {

    //Ah Pah sensitivity
    public static int mAh = 1000;
    public static int mPah = 8000;
    public static float mMaxUp = 0.0f;
    public static boolean ahh = false;
    public static boolean pahh = false;

	private boolean isConnecting = false;
	private Socket sock = null;
	private ObjectOutputStream fromClient;
	private ObjectInputStream fromServer;
	private String ipString;
	private Button toggleButton1;
	private boolean start=false;
	
    //Recoder
    private Recorder mRecorder;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.audioturnon);
            ipString = getIntent().getStringExtra("ipAddress");
    		sockInit(ipString);
          
		toggleButton1 = (Button) this.findViewById(R.id.toggleButton1);
		toggleButton1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (start==false) {
					start=true;
					audioControl();
				} else {
					start=false;
				}
			}
		});
        }
    
    private void audioControl(){
    	mRecorder = new Recorder();
		while (start) {
			if (mRecorder != null)
				mRecorder.update();
			if (pahh == true) {
				Log.d("Turn on", "" + AudioTurnOn.pahh);
				// Toast.makeText(MainActivity.this, " ahh == true ",
				// Toast.LENGTH_SHORT).show();
				try {
//					Thread.sleep(2000);
					fromClient.writeObject(new Choices(Constant.ENTER));
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				pahh = false;
			}
		}
    }
  
	private void sockInit(String ipAddress) {
	    if(sock == null){
			try {
	        	sock = new Socket(InetAddress.getByName(ipAddress),5000);
	        	fromClient = new ObjectOutputStream(sock.getOutputStream());
				fromServer = new ObjectInputStream(sock.getInputStream());
				} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else{
			Toast.makeText(AudioTurnOn.this, "Connected!", Toast.LENGTH_SHORT).show();
		}
		}
	
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
		}
		else
		{				
			isConnecting = true;
	        try {
				sock = new Socket(InetAddress.getByName(ipString),5000);
			 	fromClient = new ObjectOutputStream(sock.getOutputStream());
				fromServer = new ObjectInputStream(sock.getInputStream());
				Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) { 
				e.printStackTrace();
			}
	       
				}
		
	}
	

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mRecorder.release();
            mRecorder = null;
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }	
}
