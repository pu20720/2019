package andbas.Ch11TabHost2;

import android.app.Activity;

import android.app.AlertDialog;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

public class locationDetection extends FragmentActivity {
	
	public LocationManager locationManager1;
	public Location location1 = null;
	public String locationProvider1;

	public void locateMyPosition(){
		locationManager1 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		String provider = LocationManager.GPS_PROVIDER;
		//location1 = getMyLocationProvider(locationManager1);
		location1 = locationManager1.getLastKnownLocation(provider);
		
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
