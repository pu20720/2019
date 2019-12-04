package com.example.crazy_0703;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Device 1
    Socket clientSocket1;
    BufferedReader inbound, yoloInbound;
    PrintWriter outbound, yoloOutbund;

    String sendStr = "";

    Button Connect, FWD, BACK, LEFT, RIGHT, STOP;
    EditText deviceIP, devicePort;
    private TextView text;
    private Button btFWD, btLEFT, btBACK, btRIGHT, btSTOP, btCA, btCL, btYolo;
    private EditText textView;


    //Device 2
    Socket clientSocket2;
    BufferedReader inbound2;
    PrintWriter outbound2;

    String sendStr2 = "";

    Button Connect2, FWD2, BACK2, LEFT2, RIGHT2, STOP2;
    EditText deviceIP2, devicePort2;

    //初始化
    void init() {
        //FWD = (Button) findViewById(R.id.FWD);
        //BACK = (Button) findViewById(R.id.BACK);
        //RIGHT = (Button) findViewById(R.id.RIGHT);
        //LEFT = (Button) findViewById(R.id.LEFT);
        //STOP = (Button) findViewById(R.id.STOP);
/*
        /FWD.setOnClickListener(this);
        BACK.setOnClickListener(this);
        LEFT.setOnClickListener(this);
        RIGHT.setOnClickListener(this);
        STOP.setOnClickListener(this);
   */
        setContentView(R.layout.activity_main);
        deviceIP = (EditText) findViewById(R.id.device1_ip);
        devicePort = (EditText) findViewById(R.id.device1_port);
        Connect = (Button) findViewById(R.id.Connect);

        textView = (EditText) this.findViewById(R.id.voice_txt);
        // text = (TextView) findViewById(R.id.textview);
        btFWD = (Button) findViewById(R.id.FWD);
        btYolo = findViewById(R.id.btYolo);


        btFWD.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定按鈕觸發時所執行的動作
                // text.setText("衝阿衝阿向前衝!!!");
                sendStr = "FWD";
                sendCommand();
            }
        });
        btLEFT = (Button) findViewById(R.id.LEFT);
        btLEFT.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定按鈕觸發時所執行的動作
                // text.setText("這是左邊");
                sendStr = "LEFT";
                sendCommand();
            }
        });
        btBACK = (Button) findViewById(R.id.BACK);
        btBACK.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定按鈕觸發時所執行的動作
                // text.setText("你按到後退囉~");
                sendStr = "BACK";
                sendCommand();
            }
        });
        btRIGHT = (Button) findViewById(R.id.RIGHT);
        btRIGHT.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定按鈕觸發時所執行的動作
                // text.setText("你按到右邊惹!");
                sendStr = "RIGHT";
                sendCommand();
            }
        });
        btSTOP = (Button) findViewById(R.id.STOP);
        btSTOP.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定按鈕觸發時所執行的動作
                // text.setText("停下來啦");
                sendStr = "STOP";
                sendCommand();
            }
        });

        btCA = findViewById(R.id.btCA);
        btCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // text.setText("抓");
                sendStr = "CA";
                sendCommand();
            }
        });
        btCL = findViewById(R.id.btCL);
        btCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // text.setText("關");
                sendStr = "CL";
                sendCommand();
            }
        });
        ImageButton speak = (ImageButton) findViewById(R.id.voice_btn);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話");
                try {
                    startActivityForResult(intent, 200);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Intent problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*FWD2 = (Button) findViewById(R.id.FWD2);
                BACK2 = (Button) findViewById(R.id.BACK2);
                RIGHT2 = (Button) findViewById(R.id.RIGHT2);
                LEFT2 = (Button) findViewById(R.id.LEFT2);
                STOP2 = (Button) findViewById(R.id.STOP2);

                FWD2.setOnClickListener(this);
                BACK2.setOnClickListener(this);
                LEFT2.setOnClickListener(this);
                RIGHT2.setOnClickListener(this);
                STOP2.setOnClickListener(this);

                deviceIP2 = (EditText) findViewById(R.id.device2_ip);
                devicePort2 = (EditText) findViewById(R.id.device2_port);
                Connect2 = (Button) findViewById(R.id.Connect2);
                */
    }


    Thread t2;
    //Thread t3;
    Socket yoloSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();        //初始化

        //設定前進、後退、左轉、右轉按鈕Enabled為Flase 在未連線時無法點選
        setDeviceButton(false);
        //setDevice2Button(false);

        //按下Connect使Device1連線
        Connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Device 1 Socket Client連線
                if (Connect.getText().equals("Connect")) {

                    //建立Socket連線
                    Thread t1 = new Thread(runnable);
                    t1.start();

                }

                //Device 1 Socket Client斷線
                else if (clientSocket1.isConnected()) {
                    try {
                        Log.w("AndyDebug", "ClientSocket1 Close");
                        clientSocket1.close();
                        Connect.setText("Connect");
                        setDeviceButton(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btYolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yoloSocket == null) {
                    t2 = new Thread(yoloRunnable);
                    t2.start();
                }
            }
        });

        //按下Connect使Device2連線
        /*Connect2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                //Device 1 Socket Client連線
                if (Connect2.getText().equals("Connect")) {
                    Thread t1 = new Thread(divice2runnable);
                    t1.start();
                }

                //Device 2 Socket Client斷線
                else if (clientSocket2.isConnected()) {
                    try {
                        Log.w("AndyDebug", "ClientSocket1 Close");
                        clientSocket2.close();
                        Connect2.setText("Connect");
                        //setDevice2Button(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

    }


    //yolo連線
    Runnable yoloRunnable = new Runnable() {
        public void run() {
            try {


                String ip = "192.168.0.101";
                int port = 11111;
                String yoloInput = "";
                char buf[] = new char[100];

                yoloSocket = new Socket(ip, port);    //建立Socket連線


                Log.d("AndyDebug", "is?" + yoloSocket.isConnected());

                //Socket 接收
                yoloInbound = new BufferedReader(new InputStreamReader(
                        yoloSocket.getInputStream()));

                //Socket 發送
                yoloOutbund = new PrintWriter(yoloSocket.getOutputStream(),
                        true);

                //sendYolo("Success");

                while (true) {
                    int len = yoloInbound.read(buf);
                    char inBuf[] = new char[len];
                    for (int i=0; i<len; i++) {
                        inBuf[i] = buf[i];
                    }
                    // inBuf[len] = '\0';
                    String inString = new String(inBuf);
                    Log.v("cclo", inString);
                    if (inString.equals("turnleft")) {
                        sendStr = "LEFT";
                        sendCommand();
                        //System.out.println("123");
                    } else if (inString.equals("turnright")) {
                        sendStr = "RIGHT";
                        sendCommand();
                    } else if (inString.equals("turnstraight")) {
                        sendStr = "FWD";
                        sendCommand();
                    } else if (inString.equals("turnback")) {
                        sendStr = "BACK";
                        sendCommand();
                    }

                }


            } catch (
                    IOException ioe) {
                System.err.println("IOException: " + ioe);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Connect.setText("Connect");
                        setDeviceButton(false);
                    }
                });
            }
        }
    };


    Runnable yolorunnable2 = new Runnable() {
        public void run() {
            try {
                String sss = sendStr2;

                char send[] = sss.toCharArray();

                Log.d("AndyDebug", "送出: " + sss);

                yoloOutbund.println(send);   //送出資料

            } catch (Exception e) {
                System.err.println("IOException: " + e);
            }
        }
    };


    //Device 1 建立Socket連線
    Runnable runnable = new Runnable() {
        public void run() {
            try {

                String ip = deviceIP.getText().toString();
                int port = Integer.parseInt(devicePort.getText().toString());

                clientSocket1 = new Socket(ip, port);  //建立Socket連線

                if (clientSocket1.isConnected()) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Connect.setText("Close");
                            setDeviceButton(true);
                        }
                    });
                }

                Log.d("AndyDebug", "is?" + clientSocket1.isConnected());

                //Socket 接收
                inbound = new BufferedReader(new InputStreamReader(
                        clientSocket1.getInputStream()));

                //Socket 發送
                outbound = new PrintWriter(clientSocket1.getOutputStream(),
                        true);

            } catch (IOException ioe) {
                System.err.println("IOException: " + ioe);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Connect.setText("Connect");
                        setDeviceButton(false);
                    }
                });
            }
        }
    };

    //Device 1 透過Socket收發資料
    Runnable runnable2 = new Runnable() {
        public void run() {
            try {

                String sss = sendStr + "\r\n";

                char send[] = sss.toCharArray();

                Log.d("AndyDebug", "送出: " + sss);

                outbound.println(send);        //送出資料

                String inputLine = "";

                //接收資料
                while ((inputLine = inbound.readLine()) == null) {
                }
                Log.d("AndyDebug", "收到：" + inputLine);

            } catch (Exception e) {
                System.err.println("IOException: " + e);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.FWD:
                sendStr = "FWD";
                sendCommand();
                break;

            case R.id.BACK:
                sendStr = "BACK";
                sendCommand();
                break;

            case R.id.LEFT:
                sendStr = "LEFT";
                sendCommand();
                break;

            case R.id.RIGHT:
                sendStr = "RIGHT";
                sendCommand();
                break;

            case R.id.STOP:
                sendStr = "STOP";
                sendCommand();
                break;
             /*case R.id.FWD2:
                            s endStr2 = "FWD";
                            sendCommand2();
                            break;

                          case R.id.BACK2:
                        sendStr2 = "BACK";
                        sendCommand2();
                        break;

                         case R.id.LEFT2:
                        sendStr2 = "LEFT";
                        sendCommand2();
                        break;

                         case R.id.RIGHT2:
                        sendStr2 = "RIGHT";
                        sendCommand2();
                        break;

                         case R.id.STOP2:
                        sendStr2 = "STOP";
                        sendCommand2();
                        break;
                         default:
                        break;*/
        }
    }

    private void sendCommand() {
        Thread t2 = new Thread(runnable2);
        t2.start();
    }

    private void sendCommand2() {
        Thread t2 = new Thread(yolorunnable2);
        t2.start();
    }

    private void setDeviceButton(boolean b) {
        btFWD.setEnabled(b);
        btBACK.setEnabled(b);
        btLEFT.setEnabled(b);
        btRIGHT.setEnabled(b);
        btSTOP.setEnabled(b);
    }

            /*private void setDevice2Button(boolean b) {
                        FWD2.setEnabled(b);
                        BACK2.setEnabled(b);
                        LEFT2.setEnabled(b);
                        RIGHT2.setEnabled(b);
                        STOP2.setEnabled(b);
            }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String str = result.get(0);
                textView.setText(result.get(0));
                switch (str) {
                    case "前進":
                        sendStr = "FWD";
                        sendCommand();
                        break;
                    case "前景":
                        sendStr = "FWD";
                        sendCommand();
                        break;
                    case "乾淨":
                        sendStr = "FWD";
                        sendCommand();
                        break;
                    case "向佐":
                        sendStr = "LEFT";
                        sendCommand();
                        break;
                    case "左轉":
                        sendStr = "LEFT";
                        sendCommand();
                        break;
                    case "右轉":
                        sendStr = "RIGHT";
                        sendCommand();
                        break;
                    case "後退":
                        sendStr = "BACK";
                        sendCommand();
                        break;
                    case "向左":
                        sendStr = "LEFT";
                        sendCommand();
                        break;
                    case "向右":
                        sendStr = "RIGHT";
                        sendCommand();
                        break;
                    case "停止":
                        sendStr = "STOP";
                        sendCommand();
                        break;
                    case "停":
                        sendStr = "STOP";
                        sendCommand();
                        break;
                    case "保特瓶":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                    case "保 特 瓶":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                    case "瓶子":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                    case "停 滯":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                    case "平 日":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                    case "平日":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                    case "屏子":
                        sendStr2 = "bottle";
                        sendCommand2();
                        break;
                }
            }
        }
    }
}
