package com.hochan.sqlite.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.Buffer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

//利用socket访问网络 ,废弃

public class ClientThread implements Runnable{

	private Socket mSocket;
	private Handler mHandler;
	public Handler mRevHandler;
	private BufferedReader mBr = null;
	private OutputStream mOs = null;
	
	public ClientThread(Handler handler) {
		// TODO Auto-generated constructor stub
		this.mHandler = handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			mSocket = new Socket("192.168.55.1", 49856);
			
			mBr = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			mOs = mSocket.getOutputStream();
			
			new Thread(){
				public void run() {
					String content = null;
					try{
						while((content = mBr.readLine()) != null){
							Message message = new Message();
							message.what = 0x0123;
							message.obj = content;
							mHandler.sendMessage(message);
						}
					}
					catch(IOException e){
						e.printStackTrace();
					}
				};
			}.start();
			
			Looper.prepare();
			
			mRevHandler = new Handler(){
				public void handleMessage(Message msg) {
					if(msg.what == 0x345){
						try {
							mOs.write((msg.obj.toString()+"\r\n").getBytes("utf-8"));
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				};
			};
			Looper.loop();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
