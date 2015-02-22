package smh.devices.model;

import android.R.integer;

public class Pc {
	private int id;
	private String ipAddress;
	private String info;

	public Pc(int id, String ipAddress, String info) {
		super();
		this.id=id;
		this.ipAddress=ipAddress;
		this.info=info;
	}

	public Pc(String ipAddress) {
		super();
		this.ipAddress=ipAddress;
		this.info="Empty";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
