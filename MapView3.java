package andbas.Ch11TabHost2;

import java.io.File;


import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MapView3 extends locationDetection implements OnMapClickListener, OnMarkerClickListener, OnMarkerDragListener {
	
	//MapView mapView;
	GoogleMap mapView = null;
	
	TextView textView1;
	SupportMapFragment fragment;
	LatLng Pune;
	Marker pune;
	
	LatLng dpoint;
	
	EditText fileedit;
	
	ArrayList<LatLng> latrec;
	
	int start=0;
	int press=0;
	
    public String[] allFiles;
	private String SCAN_PATH ;
	private static final String FILE_TYPE="image/*";
	
	private MediaScannerConnection conn;

	//Uri uri;
	
	Marker lastOpenned = null;
	
	int path;
    int diff;
    int tr;
    int drt;
    
    String[] paths = null;
    String[] path_content = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview1);
		
		latrec = new ArrayList<LatLng>();
		locationManager1 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		setUpMapIfNeeded();
		
		if(location1 !=null){
			Pune = new LatLng(location1.getLatitude(), 
					location1.getLongitude());
			mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(Pune, 17));
		}
		else
		{
			Pune = new LatLng(25.03926, 121.394291);
			mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(Pune, 17));
		}
		
		locationProvider1 = LocationManager.GPS_PROVIDER;
		locationManager1.requestLocationUpdates(locationProvider1, 
				100, 100, locationListener1);
		
		
		Toast.makeText(this, "請點選任一點", Toast.LENGTH_LONG).show();
	}
	
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mapView == null)
        {
            // Try to obtain the map from the SupportMapFragment.
        	//FragmentManager fm = getChildFragmentManager();
        	mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview)).getMap();
        	//mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview)).getMap();
            // Check if we were successful in obtaining the map.
        	
        	mapView.setOnMapClickListener(this);
        	mapView.setOnMarkerClickListener(this);
        	mapView.setOnMarkerDragListener(this);

            
            if (mapView != null)
            {
                setUpMap();
            }
 
            //This is how you register the LocationSource
            //mapView.setLocationSource(locationListener1);
        }
    }
	
	private void setUpMap()
	{
		 mapView.setMyLocationEnabled(true);
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
					{
						Pune = new LatLng(location1.getLatitude(), 
								location1.getLongitude());
						mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(Pune, 17));
					}
				}
	};

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) 
	{
		// TODO Auto-generated method stub
		
		dpoint = new LatLng(arg0.latitude, arg0.longitude);
		
		String address = getAddressFromGPSData(arg0.latitude, arg0.longitude);
		String str;
		
		if (address.equals(""))
			str = "你要標示的地點為:" + arg0.latitude + "," + arg0.longitude;
		else
			str = "你要標示的地點為:" + address;
		
		openOptionsDialog(str);
		//finish();
	}
	
	public String getAddressFromGPSData(double lat, double lng) {
        HttpRetriever agent = new HttpRetriever();
        String request = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                + lng + "&language=zh-TW&sensor=true";
        // Log.d("GeoCoder", request);
        String response = agent.retrieve(request);
        String formattedAddress = "";
        if (response != null) {
            Log.d("GeoCoder", response);
            try {
                JSONObject parentObject = new JSONObject(response);
                JSONArray arrayOfAddressResults = parentObject
                        .getJSONArray("results");
                JSONObject addressItem = arrayOfAddressResults.getJSONObject(0);
                formattedAddress = addressItem.getString("formatted_address");
            } catch (JSONException e) {
 
                e.printStackTrace();
            }
 
        }
 
        // Log.d("GeoCoder", response);
        return formattedAddress;
    }
	
	 private void openOptionsDialog(String str) {
	      
	      new AlertDialog.Builder(this)
	        .setTitle("標示的地點")
	        .setMessage(str)
	        .setNegativeButton("No",
	            new DialogInterface.OnClickListener() {
	            
	              public void onClick(DialogInterface dialoginterface, int i) 
	              {
	            	  
	              }
	        }
	        )
	     
	        .setPositiveButton("Yes",
	            new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialoginterface, int i) 
	            {
	            	Content.rContent.npos = dpoint;
	            	finish();
	            }
	            
	        }
	        )
	        
	        .show();
	    }       
		
	 
}
