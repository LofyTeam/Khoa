package lofy.fpt.edu.vn.lofy_version106.controller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import lofy.fpt.edu.vn.lofy_version106.R;
import lofy.fpt.edu.vn.lofy_version106.adapter.PlacesAutoCompleteAdapter;
import lofy.fpt.edu.vn.lofy_version106.business.MapMethod;
import lofy.fpt.edu.vn.lofy_version106.entities.IsHost;
import lofy.fpt.edu.vn.lofy_version106.entities.IsMember;
import lofy.fpt.edu.vn.lofy_version106.entities.Notification;
import lofy.fpt.edu.vn.lofy_version106.entities.Route;
import lofy.fpt.edu.vn.lofy_version106.entities.User;
import lofy.fpt.edu.vn.lofy_version106.entities.UserLocation;
import lofy.fpt.edu.vn.lofy_version106.modules.DirectionFinder;
import lofy.fpt.edu.vn.lofy_version106.modules.DirectionFinderListener;
import lofy.fpt.edu.vn.lofy_version106.modules.GetNearbyPlacesData;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener, GoogleMap.OnMyLocationChangeListener, AdapterView.OnItemClickListener {

    private AutoCompleteTextView atcplSearch;
    private Button btnFindPath;
    private TextView tvDistance;
    private TextView tvDuration;
    private Button btnNearbyHospital;
    private Button btnNearbyGastation;
    private Button btnNearbyRestaurant;
    private Switch alertSOS;
    private String tempColor;
    private RadioButton rdCyan;
    private RadioButton rdYellow;
    private RadioButton rdGreen;
    private RadioButton rdBlue;
    private Marker myMarker;
    private List<Address> addresses = new ArrayList<>();


    private static final int REQUEST_LOCATION = 1111;
    public static final String INTENT_RESULT_SEARCH = "RESULT";
    private MapMethod mapMethod;
    private String keyId = "";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private DatabaseReference myRef;

    private double mLatitude;
    private double mLongtitude;

    private String isSOS;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Circle mapCircle = null;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        initView();


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    }

    private void initView() {
        atcplSearch = (AutoCompleteTextView) findViewById(R.id.ac_mainActivity_search);
        atcplSearch.setAdapter(new PlacesAutoCompleteAdapter(getApplication(), R.layout.item_autocomplete_list));
        atcplSearch.setOnItemClickListener(this);
        btnFindPath = (Button) findViewById(R.id.btn_main_findPath);
        tvDistance = (TextView) findViewById(R.id.tv_main_distance);
        tvDuration = (TextView) findViewById(R.id.tv_main_duration);
        btnNearbyHospital = (Button) findViewById(R.id.btn_main_nearby_hopistals);
        btnNearbyGastation = (Button) findViewById(R.id.btn_main_nearby_gasStation);
        btnNearbyRestaurant = (Button) findViewById(R.id.btn_main_nearby_restaurants);
        alertSOS = (Switch) findViewById(R.id.btn_main_alert);
        rdCyan = (RadioButton) findViewById(R.id.rdCyan);
        rdYellow = (RadioButton) findViewById(R.id.rdYellow);
        rdGreen = (RadioButton) findViewById(R.id.rdGreen);
        rdBlue = (RadioButton) findViewById(R.id.rdBlue);
        btnNearbyGastation.setOnClickListener(this);
        btnNearbyHospital.setOnClickListener(this);
        btnNearbyRestaurant.setOnClickListener(this);
        alertSOS.setOnClickListener(this);
        rdCyan.setOnClickListener(this);
        rdYellow.setOnClickListener(this);
        rdGreen.setOnClickListener(this);
        rdBlue.setOnClickListener(this);
        btnFindPath.setOnClickListener(this);
        btnFindPath.setEnabled(false);
        mapMethod = new MapMethod(this);


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
//        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(MainActivity.this, "Login ngon lanh", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        // get LatLng
        getLatLngToMark();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(MainActivity.this, mapMethod.getMyLocation() + "", Toast.LENGTH_LONG).show();
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
        mMap.setOnMyLocationChangeListener(this);
        pushLatLngToFirebase();
        onLongClick();
    }

    public void onLongClick() {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                // First check if myMarker is null
                if (myMarker == null) {
                    // Marker was not set yet. Add marker:
                    myMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Your marker title")
                            .snippet("Your marker snippet"));

                } else {
                    // Marker already exists, just update it's position
                    myMarker.setPosition(latLng);

                }
            }
        });
    }

    public void onMarkerClick() {

    }

    @Override
    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        String url;
        String staion;
        switch (v.getId()) {
            case R.id.btn_main_findPath:
                if (atcplSearch.getText().toString().equals("") || atcplSearch.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Xin nhập địa điểm cần tìm !", Toast.LENGTH_LONG).show();
                } else {
                    Intent in = new Intent(this, Main2Activity.class);
                    in.putExtra(INTENT_RESULT_SEARCH, atcplSearch.getText().toString());
                    startActivity(in);
                    btnFindPath.setEnabled(false);
                }


                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                break;
            case R.id.btn_main_nearby_gasStation:
                mMap.clear();
                staion = "gas_station";
                url = mapMethod.getUrl(mLatitude, mLongtitude, staion);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(getApplicationContext(), "Showing Nearby Gas Station", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_nearby_hopistals:
                mMap.clear();
                staion = "hospital";
                url = mapMethod.getUrl(mLatitude, mLongtitude, staion);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(getApplicationContext(), "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_nearby_restaurants:
                mMap.clear();
                staion = "restaurant";
                url = mapMethod.getUrl(mLatitude, mLongtitude, staion);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(getApplicationContext(), "Showing Nearby Restaurant", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_main_alert:
                if (alertSOS.isChecked()) {
                    Toast.makeText(this, "Danger!", Toast.LENGTH_SHORT).show();
                    tempColor = isSOS;
                    isSOS = "RED";
                    break;
                } else if (!alertSOS.isChecked()) {
                    Toast.makeText(this, tempColor, Toast.LENGTH_SHORT).show();
                    isSOS = tempColor;
                    break;
                }

            case R.id.rdCyan:
                Toast.makeText(this, "Cyan!", Toast.LENGTH_SHORT).show();
                isSOS = "CYAN";
                break;
            case R.id.rdYellow:
                Toast.makeText(this, "Yellow!", Toast.LENGTH_SHORT).show();
                isSOS = "YELLOW";
                break;
            case R.id.rdGreen:
                Toast.makeText(this, "Green!", Toast.LENGTH_SHORT).show();
                isSOS = "GREEN";
                break;
            case R.id.rdBlue:
                Toast.makeText(this, "Blue!", Toast.LENGTH_SHORT).show();
                isSOS = "BLUE";
                break;
            default:
                break;
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        mLatitude = location.getLatitude();
        mLongtitude = location.getLongitude();
        updateLatLngToFirebase(location);
        mapCircle = mapMethod.showCircleToGoogleMap(mMap, mapCircle, mapMethod.getMyLocation(), 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LatLng la = mapMethod.getLocationFromAddress(atcplSearch.getText().toString());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la.latitude, la.longitude), 12.0f));
        mMap.addMarker(new MarkerOptions().position(new LatLng(la.latitude, la.longitude)));
        btnFindPath.setEnabled(true);
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

    public void pushLatLngToFirebase() {
        // Write a message to the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");
        keyId = myRef.push().getKey();
        UserLocation userLocation = new UserLocation(keyId, mLatitude + "", mLongtitude + "");
        User user = new User(keyId, "userName", "nickName", null,
                null, userLocation, null,
                null, null, 0);
        DatabaseReference newRef = myRef.child(keyId);
        newRef.setValue(user);
    }

    // update LatLng to firebase
    public void updateLatLngToFirebase(Location location) {
        // Write a message to the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");
        UserLocation userLocation = new UserLocation(keyId, mLatitude + "", mLongtitude + "");
        User user = new User(keyId, "userName", "nickName", null,
                null, userLocation, null,
                null, null, 0);
        try {
            if (!isSOS.toString().equals("")) {
                user.setuColor(Color.parseColor(isSOS.toString()));
            }
        } catch (Exception e) {

        }


        DatabaseReference newRef = myRef.child(keyId);
        newRef.setValue(user);
    }


    private ArrayList<User> usersList = new ArrayList<>();
    private Marker mMaker = null;
    private ArrayList<Marker> poolMarker = new ArrayList<>();
    private MarkerOptions markerOptions;

    // get lat & long to mark
    public void getLatLngToMark() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //usersList =null;
        mFirebaseDatabase.getReference("user").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usersList.clear();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            User value = childSnapshot.getValue(User.class);
                            if (!value.getUserId().trim().equals(keyId))
                                usersList.add(value);
                        }
                        markerOptions = new MarkerOptions();
                        for (Marker marker : poolMarker) {
                            marker.remove();
                        }
                        poolMarker.clear();
                        for (User us : usersList) {
                            markerOptions.position(new LatLng(Double.parseDouble(us.getUserLocation().getLati()),
                                    Double.parseDouble(us.getUserLocation().getLongti())));
                            markerOptions.title(us.getUserId());
                            Marker mMaker = mMap.addMarker(markerOptions);
                            poolMarker.add(mMaker);
                            if (us.getuColor() == Color.BLUE) {
                                mMaker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE));
                            } else if (us.getuColor() == Color.CYAN) {
                                mMaker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_CYAN));
                            } else if (us.getuColor() == Color.GREEN) {
                                mMaker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN));
                            } else if (us.getuColor() == Color.YELLOW) {
                                mMaker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_YELLOW));
                            } else if (us.getuColor() == Color.RED) {
                                mMaker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED));
                            } else {
                                mMaker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_VIOLET));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

}
