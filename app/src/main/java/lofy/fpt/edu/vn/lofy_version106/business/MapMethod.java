package lofy.fpt.edu.vn.lofy_version106.business;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import lofy.fpt.edu.vn.lofy_version106.R;
import lofy.fpt.edu.vn.lofy_version106.controller.MainActivity;

public class MapMethod {
    private Context mContext;
    private static final int REQUEST_LOCATION = 1111;
    private static final int PROXIMITY_RADIUS = 5000;
    private LocationManager locationManager;
    public MapMethod(Context mContext) {
    this.mContext=mContext;
    locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    // get location from address
    public LatLng getLocationFromAddress( String strAddress) {
        Geocoder coder = new Geocoder(mContext);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
              Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }
    // get distance A----B
    public double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);
        return distance;
    }

    // creat cirle around a locaion
    public Circle showCircleToGoogleMap(GoogleMap mMap,Circle mapCircle, LatLng position, float radius) {
        if (mapCircle != null) {
            mapCircle.remove();
        }
        if (position == null) {
            return null;
        }
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(position);
        //Radius in meters
        circleOptions.radius(radius * 1000);
        circleOptions.strokeColor(mContext.getResources().getColor(R.color.circleColorStroke));
        circleOptions.fillColor(0x220000FF);
        circleOptions.strokeWidth(3);
        if (mMap != null) {
            mapCircle = mMap.addCircle(circleOptions);
        }
        return mapCircle;
    }

    // get my current LatLng
    public LatLng getMyLocation2() {
        LatLng myLatLng = null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latti = location.getLatitude();
            double longi = location.getLongitude();
            myLatLng = new LatLng(latti, longi);
        }
        return myLatLng;
    }
    // get my current LatLng
    public LatLng getMyLocation() {
        LatLng myLatLng = null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                myLatLng = new LatLng(latti, longi);
            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                myLatLng = new LatLng(latti, longi);
            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                myLatLng = new LatLng(latti, longi);
            } else {
                return null;
            }
        }
        return myLatLng;
    }

    public String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBLEPBRfw7sMb73Mr88L91Jqh3tuE4mKsE");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }


}
