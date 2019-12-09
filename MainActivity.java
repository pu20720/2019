package andbas.Ch11TabHost2;

import java.io.IOException;
import java.net.URL;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.xml.LoginXMLHandler;
import com.xml.LoginXMLStruct;
import com.xml.MessageHandler;
import com.xml.MessageStruct;
import com.xml.UserdataHandler;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.app.Activity;


public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	private static final int MSG_UPDATE = 1;
	private static final int MSG_GCM_UPDATE = 2;
	
	public TextView Title;
	
	public LocationManager locationManager1;
	public Location location1 = null;
	public String locationProvider1;
	
	String IPAddress;
	
	LoginXMLStruct data;
	
	String id;
	String user;
	
	EditText s1;
	
	private ArrayList<MessageStruct> ms;
	
	private Timer timer;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		locationManager1 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		String provider = LocationManager.NETWORK_PROVIDER;
		location1 = getMyLocationProvider(locationManager1);
		location1 = locationManager1.getLastKnownLocation(provider);
		
		IPAddress = (String) this.getResources().getText(R.string.url);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);	    	  
   	    user = telephonyManager.getDeviceId();
		
		//Title = (TextView)findViewById(R.id.Title);
        buildViews();  //user define
        
        String uriAPI = IPAddress + "getid.php?user=" + user;
  	  
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
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		finally
		{
			if (data == null)
			{
				Toast.makeText(MainActivity.this, "NO SERVER", Toast.LENGTH_LONG).show();
				finish();
				
			}
			
			if (data.h_chilid != null)
			{
				if (data.h_chilid.equals("-1"))
				{
					addf();
				}
				else
				{
					id = data.h_chilid;
					toweb(IPAddress + "sendlist.php?user=" + id +  "&group=0&status=1&rdate=1");
			    	locationManager1.requestLocationUpdates(locationProvider1, 
			    			0, 0, locationListener1);

				}
			}
			else
			{
				Toast.makeText(MainActivity.this, "NO SERVER", Toast.LENGTH_LONG).show();
				finish();
				
			}
		}
		
    }
	
	private void buildViews() {
		TabHost tabHost = getTabHost();

		Resources res = getResources(); 
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, Dynamic.class);
		TabSpec tabspec=tabHost.newTabSpec("tab1");
		tabspec.setContent(intent);
		tabspec.setIndicator("美食列表",
				res.getDrawable(R.drawable.icon8));
		tabHost.addTab(tabspec);

		intent = new Intent();
		intent.setClass(MainActivity.this, Action.class);
		tabspec=tabHost.newTabSpec("tab2");
		tabspec.setContent(intent);
		tabspec.setIndicator("我的收藏",
				res.getDrawable(R.drawable.ic_tab_artists));
		tabspec.setContent(intent);
		tabHost.addTab(tabspec);
		
		//if (id != null && id.equals("admin"))
		{
			intent = new Intent();
			intent.setClass(MainActivity.this, Content.class);
			tabspec=tabHost.newTabSpec("tab2");
			tabspec.setContent(intent);
			tabspec.setIndicator("新增",
					res.getDrawable(R.drawable.ic_tab_artists));
			tabspec.setContent(intent);
			tabHost.addTab(tabspec);	
		}
	
		tabHost.setCurrentTab(0);
	}
	
	public Location getMyLocationProvider(LocationManager locationManager1)
	{
		Location currentLocation = null;
		try
		{
			Criteria Criteria1 = new Criteria();
			Criteria1.setAccuracy(Criteria.ACCURACY_FINE);
			Criteria1.setPowerRequirement(Criteria.POWER_LOW);
			Criteria1.setAltitudeRequired(false);
			Criteria1.setBearingRequired(false);
			Criteria1.setCostAllowed(true);
			locationProvider1 = locationManager1.getBestProvider(Criteria1, true);
			currentLocation = locationManager1.getLastKnownLocation(locationProvider1);
		}
		catch (Exception e) {
			{
				e.printStackTrace();
			}
			return currentLocation;
		}
		return currentLocation;
		}
	
	public LocationListener locationListener1 =
			new LocationListener() 
			{
				
				//@Override
				public void onStatusChanged(String provider, int status, Bundle extras)
				{
				
				}
				
				//@Override
				public void onProviderEnabled(String provider)
				{
					
				}
				
				//@Override
				public void onProviderDisabled(String provider)
				{
					location1 = null;	
				}
				
				//@Override
				public void onLocationChanged(Location location)
				{
					location1 = location;
					//send
					if (location1 != null)
					{
						Message msg = new Message();
						msg.what = MSG_UPDATE;
						myHandler.sendMessage(msg);						
					}
				}
	};
	
	public Handler myHandler = new Handler(){
	    public void handleMessage(Message msg) {
	        switch(msg.what)
	        {
	          case MSG_UPDATE:
	        	  sendgps();        	  
	        	  break;
	          case MSG_GCM_UPDATE:
	        	  
	        	 
	        	  
	        	  break;
	        }
	    }
	};
	
	void sendgps()
	{
   	    if (location1 == null) return;
   	    
		String uriAPI = IPAddress + "sendgps.php?user=" + id + "&lat=" + location1.getLatitude() + "&lng=" + location1.getLongitude();
		Log.i("TAG", uriAPI);
		URL url = null;
		try {
			url = new URL(uriAPI);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			LoginXMLHandler myHandler = new LoginXMLHandler();
			xr.setContentHandler(myHandler);
			// open connection
			xr.parse(new InputSource(url.openStream()));
			data = myHandler.getParsedData();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	void addf()
	{
	      AlertDialog.Builder alert = new AlertDialog.Builder(this);
	      alert.setCancelable(false);
	      alert.setTitle("ID?");     
	      
	      ScrollView sv = new ScrollView(this);
	      LinearLayout ll = new LinearLayout(this);
	      ll.setOrientation(LinearLayout.VERTICAL);
	      sv.addView(ll);
	      
	      TextView tlogin = new TextView(this);
	      tlogin.setTextColor(Color.WHITE);
	      tlogin.setText(user + ": ");
	      s1 = new EditText(this);
	      s1.setText("");
	      ll.addView(tlogin);
	      ll.addView(s1);

	      // Set an EditText view to get user input
	      alert.setView(sv);
	      
	      alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	      	  try
	      	  {
	  	    	  String content = URLEncoder.encode((s1.getText().toString()), "UTF-8");
	  	    	  
	  	    	  String uriAPI = IPAddress + "sendid.php?user=" + user  + "&id=" + content 
	  	    			+ "&img=0" + "&user=" + user;
	  	    	  
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
	  				}
	  				catch(Exception e){
	  					e.printStackTrace();
	  					return;
	  				}
	  				finally
	  				{
	  					id = content;
	  					
						toweb(IPAddress + "sendlist.php?user=" + id +  "&group=0&status=1&rdate=1");
				    	locationManager1.requestLocationUpdates(locationProvider1, 
				    			0, 0, locationListener1);

	  				}
	      	  }
	      	  catch (Exception e)
	      	  {
	      		  e.printStackTrace();
	      	  }

	        		
	        }
	      });
	      
	      alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton)
	          {
	            

	          }
	        });
	        
	      
	      alert.show();    
	}
	
	public int toweb(String uriAPI) {
		int error = 0;
		HttpGet httpRequest = new HttpGet(uriAPI);

		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} else {
				// mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString());
			}
		} catch (ClientProtocolException e) {
			// mTextView1.setText(e.getMessage().toString());
			e.printStackTrace();
			error = 1;
		} catch (IOException e) {
			// mTextView1.setText(e.getMessage().toString());
			e.printStackTrace();
			error = 1;
		} catch (Exception e) {
			// mTextView1.setText(e.getMessage().toString());
			e.printStackTrace();
			error = 1;
		}

		return error;
	}
	
	public class DateTask extends TimerTask {
	    public void run() 
	    {
	    	try {
				
				String uriAPI = IPAddress + "getgcm.php";
				
				Log.i("TAG", uriAPI);
		        
		        URL url = null;
		        try{
		          url = new URL(uriAPI);
		          
		          SAXParserFactory spf = SAXParserFactory.newInstance();
		          SAXParser sp = spf.newSAXParser();
		          XMLReader xr = sp.getXMLReader();
		          MessageHandler myHandler = new MessageHandler();
		          xr.setContentHandler(myHandler);
		          //open connection
		          xr.parse(new InputSource(url.openStream()));
		          ms = myHandler.getContainer().getListItems();
		        }
		        catch(Exception e){
		          e.printStackTrace();
		          return;
		        }
		        
		        if (ms != null)
		        {
		        	Message msg = new Message();
		 		    msg.what = MSG_GCM_UPDATE;
		 		    myHandler.sendMessage(msg);
		        }
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}		
	    }
}

}