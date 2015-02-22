package smh.devices.model;

import android.R.integer;

public class Service {
	private int id;
	private String name;
	private String ipAddress;

	public Service(int id, String name, String ipAddress) {
		super();
		this.id=id;
		this.name=name;
		this.ipAddress=ipAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
