import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

class t0606 {

	static int angle = 270; // 馬達轉動的角度

	private static boolean OutServer = false;
	private static ServerSocket server;
	private final static int ServerPort = 1234; // 要監控的port

	public static void main(String[] args) {

		//按下ESCAPE鍵離開程式
		Button.ESCAPE.addKeyListener(new KeyListener() {
			public void keyReleased(Key k) {
				System.exit(0);
			}
			public void keyPressed(Key k) {}
		});

		print("Start");
		
		Thread socketServer_thread = new Thread(socketServer_runnable);
		socketServer_thread.start();

		while (true);
	}

	//建立SocketServer
	public static void SocketServer() {
		try {
			server = new ServerSocket(ServerPort);

		} catch (java.io.IOException e) {
			print("Socket Error!");
			print("IOException :" + e.toString());
		}
	}

	static Runnable socketServer_runnable = new Runnable() {
		public void run() {
			SocketServer();

			Socket socket;

			print("Socket Server OK!");

			while (!OutServer) {
				socket = null;
				try {
					synchronized (server) {
						socket = server.accept();
					}
					print(socket.getInetAddress().toString()); //印出目前連線設備的ip

					//Socket接收
					BufferedReader inbound = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					//Socket發送
					PrintWriter outbound = new PrintWriter(
							socket.getOutputStream(), true);

					while (true) {
						String data = "";

						outbound.println("Success");

						while ((data = inbound.readLine()) == null)
							;

						print("getData:" + data);

						if (data.equals("FWD")) {
							go();
							outbound.print(data + " OK");
						} else if (data.equals("BACK")) {
							back();
							outbound.print(data + " OK");
						} else if (data.equals("LEFT")) {
							left(angle);
							outbound.print(data + " OK");
						} else if (data.equals("RIGHT")) {
							right(angle);
							outbound.print(data + " OK");
						} else if (data.equals("STOP")) {
							stop();
							outbound.print(data + " OK");
						}else if (data.equals("CA")) {
							ca();
							outbound.print(data + " OK");
						}else if (data.equals("CL")) {
							cl();
							outbound.print(data + " OK");
						}
					}

				} catch (java.io.IOException e) {
					print("Socket Error");
					print("IOException :" + e.toString());
					Thread socketServer_thread = new Thread(socketServer_runnable);
					socketServer_thread.start();
				}
			}
		}
	};
	
	//前進
	private static void go() {
		Motor.A.forward();
		Motor.B.forward();
	}

	//後退
	private static void back() {
		Motor.A.backward();
		Motor.B.backward();
	}

	//左轉
	private static void left(int angle) {		
		Motor.A.rotate(-angle, true);
		Motor.B.rotate(angle, true);
	}

	//右轉
	private static void right(int angle) {
		Motor.A.rotate(angle, true);
		Motor.B.rotate(-angle, true);
	}
	//停止
	private static void stop() {
		Motor.A.stop();
		Motor.B.stop();
	}
	private static void ca() {
		Motor.C.rotate(-100, true);
	}
	private static void cl() {
		Motor.C.rotate(150, true);	
	}
	
	//在EV3螢幕印出字串
	private static void print(String str) {
		LCD.clear();
		LCD.drawString(str, 0, 3);
	}
}