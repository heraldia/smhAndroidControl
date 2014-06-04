package android.control.server.impl;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import smh.devices.control.Choices;

public class ControlServer {
	private final static int SHIFTF5 = 0;
	private final static int RIGHT = 1;
	private final static int LEFT = 2;
	private final static int ESC = 3;
	private final static int CTRLLEFT = 4;
	private final static int CTRLRIGHT = 5;
	private final static int SPACE = 6;
	private final static int UP = 7;
	
	
	private final static int DOWN = 8;
	public final static int ENTER = 9;

	private static int key;

	private static ObjectInputStream fromClient;

	private static ObjectOutputStream fromServer;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, AWTException, InterruptedException {

		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();// host IP¡¡
		while (true) {
			System.out.println("IP address: " + ip + " : 5000");
			ServerSocket sSocket = new ServerSocket(5000);
			System.out.println("Waiting a connection from the app client");

			Robot robot = new Robot();

			Socket sock = sSocket.accept();
			System.out.println("Recv a connection");

			fromClient = new ObjectInputStream(sock.getInputStream());
			fromServer = new ObjectOutputStream(sock.getOutputStream());

			try {

				do {
					Choices choice;
					choice = (Choices) fromClient.readObject();

					// System.out.println("the flag is " + choice.getKey());
					key = choice.getKey();
					switch (key) {

					case SHIFTF5:
						System.out.println("the flag is SHIFTF5");
						robot.keyPress(KeyEvent.VK_SHIFT);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_F5);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_F5);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						Thread.sleep(10);
						break;

					case LEFT:
						System.out.println("the flag is LEFT");
						robot.keyPress(KeyEvent.VK_LEFT);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_LEFT);
						Thread.sleep(10);
						break;

					case RIGHT:
						System.out.println("RIGHT");
						robot.keyPress(KeyEvent.VK_RIGHT);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_RIGHT);
						Thread.sleep(10);
						break;

					case ESC:
						System.out.println("ESC");
						robot.keyPress(KeyEvent.VK_ESCAPE);
						Thread.sleep(10);
						robot.keyPress(KeyEvent.VK_ESCAPE);
						Thread.sleep(10);
						break;

					case CTRLRIGHT:
						System.out.println("CTRLRIGHT");
						robot.keyPress(KeyEvent.VK_CONTROL);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_RIGHT);
						Thread.sleep(10);
						robot.keyPress(KeyEvent.VK_RIGHT);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						Thread.sleep(10);
						break;

					case CTRLLEFT:
						System.out.println("CTRLLEFT");
						robot.keyPress(KeyEvent.VK_CONTROL);
						Thread.sleep(20);
						robot.keyPress(KeyEvent.VK_LEFT);
						Thread.sleep(10);
						robot.keyPress(KeyEvent.VK_LEFT);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						Thread.sleep(10);
						break;
					case UP:
						System.out.println("UP");
						robot.keyPress(KeyEvent.VK_UP);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_UP);
						Thread.sleep(10);
						break;
					case DOWN:
						System.out.println("DOWN");
						robot.keyPress(KeyEvent.VK_DOWN);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_DOWN);
						Thread.sleep(10);
						break;
					case SPACE:
						System.out.println("SPACE");
						robot.keyPress(KeyEvent.VK_SPACE);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_SPACE);
						Thread.sleep(10);
						break;
					case ENTER:
						System.out.println("ENTER");
						robot.keyPress(KeyEvent.VK_ENTER);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_ENTER);
						Thread.sleep(10);
						break;
						
					default:
						break;
					}
				} while (key != -1);
			} catch (Exception e) {
				System.out.println(e);
			}
			System.out.println("exit the app");
			fromClient.close();
			fromServer.close();
			sock.close();
			sSocket.close();
		}

	}
}
