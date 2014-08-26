package net.majorkernelpanic.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.majorkernelpanic.streaming.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivitty extends Activity implements OnClickListener{
	
	private EditText pass,user;
	private Button login;
	private Thread sendThread, receiveThread;
	private Socket socket;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		initUI();
		initSocket();
		initThread();
	}
	
	private void initUI(){
		pass = (EditText)findViewById(R.id.password);
		user = (EditText)findViewById(R.id.username);
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(this);
	}
	
	private void initThread() {
		sendThread = new Thread(){
			public void run() {
				try {
					OutputStream os =  socket.getOutputStream();
					StringBuilder sb = new StringBuilder();
					sb.append(user.getText().toString());
					sb.append("\t");
					sb.append(pass.getText().toString());
					String s = sb.toString();
					byte[] buffer = s.getBytes();
					os.write(buffer);
					os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		receiveThread = new Thread(){
			public void run(){
				try {
					InputStream is = socket.getInputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line="";
					while((line = in.readLine())!=null) {
						sb.append(line);
					}
					if(sb.toString().equals("uer1")) {
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private void initSocket(){
		try {
			socket = new Socket("192.168.1.42",8000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if(v==login) {
			sendThread.start();
			login.setClickable(false);
		}
	}

}
