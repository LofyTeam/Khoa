package lofy.fpt.edu.vn.mycapstoneprojectver5.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lofy.fpt.edu.vn.mycapstoneprojectver5.Entities.Route;
import lofy.fpt.edu.vn.mycapstoneprojectver5.Entities.User;
import lofy.fpt.edu.vn.mycapstoneprojectver5.Modules.DirectionFinder;
import lofy.fpt.edu.vn.mycapstoneprojectver5.Modules.DirectionFinderListener;
import lofy.fpt.edu.vn.mycapstoneprojectver5.R;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,

        LocationListener, DirectionFinderListener, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap;
    private TextView tvSeach;
    private Button btnFindPath;
    public static final int REQUEST_CODE_SEARCH = 1111;
    private boolean isBtnFindPathVisible = false;
    private TextView tvDistance;
    private TextView tvDuration;
    private ProgressDialog progressDialog;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_NETWORK = 11;
    private LocationManager locationManager;
    private String keyId = "";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ArrayList<User> usersList = new ArrayList<>();
    private Marker mMaker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initView();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(MainActivity.this, getMyLocation() + "", Toast.LENGTH_LONG).show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        pushLatLngToFirebase();
        mMap.setOnMyLocationChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(SearchActivity.INTENT_RETURN_MAIN);
                tvSeach.setText(result);
                isBtnFindPathVisible = true;
                btnFindPath.setEnabled(isBtnFindPathVisible);

                // Add a marker in Sydney and move the camera
                LatLng la = getLocationFromAddress(getApplicationContext(), result);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la.latitude, la.longitude), 12.0f));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // no result
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_search:
                Intent i = new Intent(this, SearchActivity.class);
                startActivityForResult(i, REQUEST_CODE_SEARCH);
                break;
            case R.id.btn_main_findPath:
                String ori = getMyLocation().latitude + "," + getMyLocation().longitude;
                String des = tvSeach.getText().toString();
                try {
                    findPath(ori, des);
                } catch (Exception e) {
                    Toast.makeText(this, "ERROR: ", Toast.LENGTH_LONG).show();
                }
                String gDis = "" + (new DecimalFormat("#.##").format((getDistance(getLocationFromAddress
                        (this, ori), getLocationFromAddress(this, des)) / 1000))) + " Km";
                Toast.makeText(this, "Distance: " + ori, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }
        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            tvDuration.setText(route.duration.text);
            tvDistance.setText(route.distance.text);
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    // get LatLng from an address
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
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

    // find path of 2 location
    private void findPath(String ori, String des) {
        try {
            new DirectionFinder(this, ori, des).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // initial application
    private void initView() {
        tvSeach = (TextView) findViewById(R.id.tv_main_search);
        btnFindPath = (Button) findViewById(R.id.btn_main_findPath);
        tvSeach.setOnClickListener(this);
        tvDistance = (TextView) findViewById(R.id.tv_main_distance);
        tvDuration = (TextView) findViewById(R.id.tv_main_duration);
        btnFindPath.setOnClickListener(this);
        btnFindPath.setEnabled(isBtnFindPathVisible);
    }

    // get distance
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

    // get my current LatLng
    private LatLng getMyLocation() {
        LatLng myLatLng = null;
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
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

    // alert request if not gps
    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // creat cirle around a locaion
    public void showCircleToGoogleMap(LatLng position, float radius) {
        if (position == null) {
            return;
        }
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(position);
        //Radius in meters
        circleOptions.radius(radius * 1000);
        circleOptions.strokeColor(getResources().getColor(R.color.circleColorStroke));
        circleOptions.fillColor(0x220000FF);
        circleOptions.strokeWidth(3);
        if (mMap != null) {
            mMap.addCircle(circleOptions);
        }
    }

    public void pushLatLngToFirebase() {
        // Write a message to the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");
        //String keyG=myRef.push().getKey();
        //keyId = myRef.child(keyG).push().getKey();

        keyId = myRef.push().getKey();
        User user = new User("Tie", keyId, String.valueOf(getMyLocation().latitude), String.valueOf(getMyLocation().longitude));
        // DatabaseReference newRef = myRef.child(keyG).child(keyId);
        DatabaseReference newRef = myRef.child(keyId);
        newRef.setValue(user);
    }

    public void updateLatLngToFirebase(Location location) {
        // Write a message to the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");
        User user = new User("Tie", keyId, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        DatabaseReference newRef = myRef.child(keyId);
        newRef.setValue(user);
    }

    public void getLatLngToMark() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //usersList =null;
        mFirebaseDatabase.getReference("user").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            User value = childSnapshot.getValue(User.class);
                            if (!value.getKeyId().trim().equals(keyId))
                                usersList.add(value);
                        }
                        MarkerOptions markerOptions = new MarkerOptions();
                        // markerOptions.visible(false);
                        for (User us : usersList) {
                            try {
                                mMaker.remove();

                            } catch (Exception e) {
                                e.getMessage();
                            }
                            markerOptions.position(new LatLng(Double.parseDouble(us.getLati()), Double.parseDouble(us.getLongti())));
                            mMaker = mMap.addMarker(markerOptions);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public void onMyLocationChange(Location location) {
//        showCircleToGoogleMap(getMyLocation(), 3);

        updateLatLngToFirebase(location);
        getLatLngToMark();

    }
}
