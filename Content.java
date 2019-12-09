package andbas.Ch11TabHost2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.android.gms.maps.model.LatLng;
import com.xml.LoginXMLHandler;
import com.xml.LoginXMLStruct;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Content extends Activity 
{
	String TAG ="Content";

	private static final int MSG_LIST_OK = 1;

	static public Content rContent;
	
	private Spinner spinner_event;
	//private Spinner spinner_limit;
	EditText stitle, scontent, sphone;
	private Button pic,area,send;
	String[] action_name = new String[] { "日式","中式","台式","義式","美式","其他" };
	
	String[] add_limit = new String[] { "不限制","1", "2", "3", "4", "5", "6" ,"7","8","9","10","11","12","13","14","15"
			,"16","17","18","19","20"};
	
	String picfilename[]= new String[3];
	
	String IPAddress="";
	
	ProgressDialog  myDialog;
	
	LatLng npos;
	
	LoginXMLStruct data;
	ByteArrayOutputStream baos;
	
	int counter = 0;
	
	int from; //This must be declared as global !
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		rContent = this;
		
        IPAddress = (String) this.getResources().getText(R.string.url);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		spinner_event = (Spinner) findViewById(R.id.spinner_event);
		//spinner_limit = (Spinner) findViewById(R.id.spinner_limit);
		pic = (Button) findViewById(R.id.addpicture);
		area = (Button) findViewById(R.id.getarea);
		send = (Button) findViewById(R.id.Contentsend);
		
		spinner_event.setEnabled(true);
		//spinner_limit.setEnabled(true);
		pic.setEnabled(true);
		area.setEnabled(true);
		send.setEnabled(true);	
		
		pic.setOnClickListener(listener);
		area.setOnClickListener(listener);
		send.setOnClickListener(listener);

		npos = new LatLng(121.3631593, 25.0564237);
		//picfilename ="pic.jpg";
		
		buildViews();
		
		
	}
	
	private Button.OnClickListener listener  = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.addpicture:
				//Toast.makeText(Content.this, "asd",Toast.LENGTH_SHORT).show();
				
				final CharSequence[] choice = {"Choose from Gallery","Capture a photo"};

				

				AlertDialog.Builder alert = new AlertDialog.Builder(Content.this);
				alert.setTitle("上傳選擇");
				alert.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        if (choice[which] == "Choose from Gallery") {
				            from = 1;
				        } else if (choice[which] == "Capture a photo") {
				            from = 2;
				        }
				    }
				});
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        if (from == 0) {
				            Toast.makeText(Content.this, "Select One Choice", 
				                        Toast.LENGTH_SHORT).show();
				        } else if (from == 1) {
				            // Your Code
				        	Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				          	photoPickerIntent.setType("image/*");
				          	startActivityForResult(photoPickerIntent, 1);
				        } else if (from == 2) {
				            // Your Code
				        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			                startActivityForResult(intent, 2);
				        }
				    }
				});
				alert.show();
				
				
				
	          	
	          	
	          	
	          	
				break;
			case R.id.getarea:
				//Toast.makeText(Content.this, "QWE", Toast.LENGTH_SHORT).show();
				Intent maps = new Intent(Content.this, MapView3.class);
	          	startActivity(maps);				
				break;
			case R.id.Contentsend:
				
        	  	senddata();
				Toast.makeText(Content.this, "加入成功", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	


	private void buildViews() 
	{
		// TODO 自動產生的方法 Stub
		stitle = (EditText) findViewById(R.id.editTitle);
		scontent = (EditText) findViewById(R.id.editContent);
		sphone = (EditText) findViewById(R.id.editPhone);
		
		spinner_event = (Spinner) findViewById(R.id.spinner_event);
		//spinner_limit = (Spinner) findViewById(R.id.spinner_limit);
		
		ArrayAdapter<String> adapterAction = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, action_name);
		adapterAction
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_event.setAdapter(adapterAction);
		
		ArrayAdapter<String> adapterlimt = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, add_limit);
		adapterAction
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//spinner_limit.setAdapter(adapterlimt);
	}

	void senddata()
	{
    	  try
    	  {
	    	  String subject = URLEncoder.encode((stitle.getText().toString().trim()), "UTF-8");
	    	  String content = URLEncoder.encode((scontent.getText().toString()), "UTF-8");
	    	  String pnum="";
	    	  //if (spinner_limit.getSelectedItemPosition() == 0)
	    		  pnum = "0";
	          //else
	        	  //pnum = add_limit[spinner_limit.getSelectedItemPosition()];
	    	  
	    	  String type = Integer.toString(spinner_event.getSelectedItemPosition());
	    	  
	    	  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);	    	  
	    	  String user = telephonyManager.getDeviceId();
	    	  
			  String area = getCityFromGPSData(npos.latitude, npos.longitude);
			  String sarea = URLEncoder.encode(area, "UTF-8");
	    	  String uriAPI = IPAddress + "sendgroup.php?title=" + subject + "&content=" + content + "&pic=" + picfilename[0]  + "&address=" + npos.latitude + "," + npos.longitude + "&type=" + type + "&pnum=" + pnum + "&user=" + user + "&area=" + sphone.getText().toString();
	    	  
	    	  Log.i("TAG", uriAPI);
	
				URL url = null;
				try{
					url = new URL(uriAPI);
					
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					//Using login handler for xml
					LoginXMLHandler myHandler = new LoginXMLHandler();
					xr.setContentHandler(myHandler);
					//open connection
					xr.parse(new InputSource(url.openStream()));
					//verify OK
					data = myHandler.getParsedData();
					
					stitle.setText("");
					scontent.setText("");
				}
				catch(Exception e){
					e.printStackTrace();
					return;
				}				
    	  }
    	  catch (Exception e)
    	  {
    		  e.printStackTrace();
    	  }
		
	}
	
	
	public static String getRealPathFromUri(Activity activity, Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	  public void upload(byte [] ba)
	  {
		  String ba1 = Base64.encodeBytes(ba);
		  ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

		  Calendar c = Calendar.getInstance();
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		  picfilename[counter] = sdf.format(c.getTime()) + ".jpg";
		  
		  nameValuePairs.add(new BasicNameValuePair("image",ba1));
		  nameValuePairs.add(new BasicNameValuePair("filename",picfilename[counter]));
		  
		  Log.i("TAG", picfilename[counter]);

		  try{
				  HttpClient httpclient = new DefaultHttpClient();

				  String url = IPAddress + "upload.php";
				  Log.i("TAG", url);
				  HttpPost httppost = new HttpPost(url);
				  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
				  HttpResponse response = httpclient.execute(httppost);

			    /* http response 200 = ok */
			    Log.v(TAG, "http response status: "+response.getStatusLine().getStatusCode());
			    if (response.getStatusLine().getStatusCode() == 200) {
		
			    /* get data from server url */
			    String strResult = EntityUtils.toString(response.getEntity());
			    Log.d(TAG, "get Result:"+strResult);
		    } else {
		     //tools.showInfo("upload file error: "+httpResponse.getStatusLine().getStatusCode());
		    }

		  }catch(Exception e){
			  Log.e("log_tag", "Error in http connection "+e.toString());
	      }

	  }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    Bitmap mBitmap = null;
	    
	    if (resultCode == RESULT_OK)
	    {
	    	if (requestCode == 1) 
	    	{
		        Uri chosenImageUri = data.getData();
	
		        try {
					
					mBitmap = Media.getBitmap(this.getContentResolver(), chosenImageUri);
					
					baos = new ByteArrayOutputStream();    
					mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
					
		        }
		        catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
	    	}
	    	else
	    	{
	    		mBitmap = (Bitmap) data.getExtras().get("data");
	    		
	    		Log.i("TAG", mBitmap.getHeight()+"");
	    		
	    		baos = new ByteArrayOutputStream();    
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
	    		
	    	}
	    	
	    	//Progress
		     myDialog = ProgressDialog.show
		     (
		        	    Content.this,
		        		"傳送中",
		        		"等待",
		            true
		     );
		        
		     new Thread()
		     {
		          public void run()
		          {
						upload(baos.toByteArray());				
		                Message msg = new Message();
		                msg.what = MSG_LIST_OK;
		                myHandler.sendMessage(msg);
		           }
		     }.start();  
	    }
	}
	
    public Handler myHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
              case MSG_LIST_OK:
           	   	  myDialog.dismiss();
           	   	  
            	  break;
            }
        }
    };
	
	public String getCityFromGPSData(double lat, double lng) 
	{
		HttpRetriever agent = new HttpRetriever();
        String request = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&language=zh-TW&sensor=true";
			
        String Address1 = "";
		String Address2 = "";
		String City = "";
		String State = "";
		String Country = "";
		String County = "";
		String PIN = "";

        try {
        	
        	String response = agent.retrieve(request);
            JSONObject jsonObj = new JSONObject(response);
					
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            // Address2 = Address2 + long_name + ", ";
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }
                    }

                    // JSONArray mtypes = zero2.getJSONArray("types");
                    // String Type = mtypes.getString(0);
                    // Log.e(Type,long_name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	 
	        // Log.d("GeoCoder", response);
	        return City;
	}
	
	
	
	
}
