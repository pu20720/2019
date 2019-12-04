package andbas.Ch11TabHost2;

import java.io.File;


import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xml.LoginXMLStruct;
import com.xml.OnLineListHandler;
import com.xml.OnlineStruct;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationMAP extends FragmentActivity{
	
	private static final int MSG_UPDATE = 1;
		
	//MapView mapView;
	GoogleMap mapView = null;
	
	TextView textView1;
	SupportMapFragment fragment;
	
	LatLng Pune;
	Marker pune;
	
	EditText fileedit;
	
	int start=0;
	int press=0;

    private Timer timer;

	Marker lastOpenned = null;

	String IPAddress;
    
	ArrayList<OnlineStruct> slatrec;

	public LocationManager locationManager1;
	public Location location1 = null;
	public String locationProvider1;
	
	int gindex;
	String groupname[];
	
	String user;
	
	LoginXMLStruct data;
	
	String id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview1);
	    
        Bundle bData = this.getIntent().getExtras();
        
        if (bData != null)
        {
        	id = bData.getString( "id" );
        }		
		
		setMainThread();
	
	    
	    if(location1 !=null)
	    {
	    	locationManager1.requestLocationUpdates(locationProvider1, 
	    			100, 100, locationListener1);
	    }
	    
	    timer = new Timer();
	    timer.schedule(new DateTask(), 0, 5000);
	}
	
	void setMainThread()
	{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        IPAddress = (String) this.getResources().getText(R.string.url);
        
        if (mapView == null)
        {
        	mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview)).getMap();
            
            if (mapView != null)
            	 mapView.setMyLocationEnabled(true);
        }
        
        locationManager1 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		String provider = LocationManager.GPS_PROVIDER;
		location1 = getMyLocationProvider(locationManager1);
		location1 = locationManager1.getLastKnownLocation(provider);

	}
	
    private void MP()
	{
			PolylineOptions rectOptions = new PolylineOptions();
			rectOptions.width(10);
			rectOptions.color(0xff659d32);
			
			if (mapView == null) return;
			
			for (int i=0; i<slatrec.size(); i++)
			{
				double lat = Double.valueOf(slatrec.get(i).lat);
				double lng = Double.valueOf(slatrec.get(i).lng);
				LatLng newlanlng = new LatLng(lat, lng);
				mapView.addMarker(new MarkerOptions().position(newlanlng).title(slatrec.get(i).name + " 在這裡哦").visible(true)).showInfoWindow();
			}

			mapView.setOnMarkerClickListener(new OnMarkerClickListener() {
			    public boolean onMarkerClick(Marker marker) {
			        // Check if there is an open info window
			        if (lastOpenned != null) {
			            // Close the info window
			            lastOpenned.hideInfoWindow();
			            
			            // Is the marker the same marker that was already open
			            if (lastOpenned.equals(marker)) {
			                // Nullify the lastOpenned object
			                lastOpenned = null;
			                // Return so that the info window isn't openned again
			                return true;
			            } 
			        }

			        // Open the info window for the marker
			        marker.showInfoWindow();
			        // Re-assign the last openned such that we can close it later
			        lastOpenned = marker;

			        // Event was handled by our code do not launch default behaviour.
			        return true;
			    }
			});
			
			//當點開時，就可看到資訊
			mapView.setInfoWindowAdapter(new InfoWindowAdapter() 
			{
				   private final View contents = getLayoutInflater().inflate(R.layout.content, null);

				   
				   public View getInfoWindow(Marker marker) 
				   {
					   return null;
				   }

				   
				  
				   public View getInfoContents(Marker marker) 
				   {
					   
					   //int index = Integer.valueOf(marker.getTitle());
					   TextView tvTitle = ((TextView)contents.findViewById(R.id.title));
					   tvTitle.setText(marker.getSnippet());
					  
					   return contents;
				   }

			});
			
	}
	  
	  
	public class DateTask extends TimerTask {
		    public void run() 
		    {
				String uriAPI = IPAddress + "getgpslist.php?user=" + id;
				Log.i("TAG", uriAPI);
				URL url = null;
				try {
					url = new URL(uriAPI);

					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					OnLineListHandler myHandler = new OnLineListHandler();
					xr.setContentHandler(myHandler);
					// open connection
					xr.parse(new InputSource(url.openStream()));
					slatrec = myHandler.getContainer().getListItems();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				if (slatrec != null) 
				{
					//GPS更新
					Message msg = new Message();
					msg.what = MSG_UPDATE;
					myHandler.sendMessage(msg);				
				}
		    }
	 }
	 
	 
	public Handler myHandler = new Handler(){
		    public void handleMessage(Message msg) {
		        switch(msg.what)
		        {
		          case MSG_UPDATE:
		        	  MP();        	  
		        	  break;
		        }
		    }
	};
	  	  
	public void setMapCenter(Location location1)
	{
	   
		
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
					
					if (location1 != null)					
						mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(Pune, 17));
				}
	};
	
	
	   public boolean onKeyDown(int keyCode, KeyEvent event) 
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{  
	    		if (timer != null)
	    			timer.cancel();
	    		
	        	finish();
	    		return true;
	    	}
			
			return super.onKeyDown(keyCode, event);  
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
	
	
	 
}
