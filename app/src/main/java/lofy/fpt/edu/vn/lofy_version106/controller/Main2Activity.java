package lofy.fpt.edu.vn.lofy_version106.controller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lofy.fpt.edu.vn.lofy_version106.R;
import lofy.fpt.edu.vn.lofy_version106.business.MapMethod;
import lofy.fpt.edu.vn.lofy_version106.entities.Route;
import lofy.fpt.edu.vn.lofy_version106.modules.DirectionFinder;
import lofy.fpt.edu.vn.lofy_version106.modules.DirectionFinderListener;

public class Main2Activity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener,
        View.OnClickListener, DirectionFinderListener {

    private static final String TAG = "TAG";
    private GoogleMap mMap;
    private Circle mapCircle;
    private LocationManager locationManager;
    private Button btnFind;
    private TextView tvOris;
    private TextView tvDes;
    private TextView tvDuration;
    private TextView tvDistance;
    private ProgressDialog progressDialog;

    private MapMethod mapMethod;

    private static final int REQUEST_LOCATION = 1112;
    public static final int REQUEST_CODE_SEARCH_ORI = 1113;
    public static final int REQUEST_CODE_SEARCH_DES = 1114;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMain2);
        mapFragment.getMapAsync(this);
        initView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
    }

    private void initView() {
        btnFind = (Button) findViewById(R.id.btn_main2_findPath);
        tvOris = (TextView) findViewById(R.id.tv_main2_ori);
        tvDes = (TextView) findViewById(R.id.tv_main2_des);
        tvDistance = (TextView) findViewById(R.id.tv_main2_distance);
        tvDuration = (TextView) findViewById(R.id.tv_main2_duration);
        btnFind.setOnClickListener(this);
        btnFind.setEnabled(false);
        tvOris.setOnClickListener(this);
        tvDes.setOnClickListener(this);
        mapMethod = new MapMethod(this);
        getIntentFromMainActivity();
    }

    private void getIntentFromMainActivity() {
        Intent in = getIntent();
        if (in == null) {
            return;
        } else {
            String intentAddress = in.getStringExtra(MainActivity.INTENT_RESULT_SEARCH);
            tvDes.setText(intentAddress);
            String ori = mapMethod.getMyLocation().latitude + "," + mapMethod.getMyLocation().longitude;
            try {

                findPath(ori, intentAddress);
            } catch (Exception e) {
                Toast.makeText(this, "ERROR: ", Toast.LENGTH_LONG).show();
            }
//            String gDis = "" + (new DecimalFormat("#.##").format((mapMethod.getDistance(mapMethod.getLocationFromAddress
//                    (ori), mapMethod.getLocationFromAddress(intentAddress)) / 1000))) + " Km";
//            Toast.makeText(this, "Distance: " + gDis, Toast.LENGTH_LONG).show();
        }
    }

    // find path of 2 location
    private void findPath(String ori, String des) {
        try {
            new DirectionFinder(this, ori, des).executeGray();
            new DirectionFinder(this, ori, des).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        mapCircle = mapMethod.showCircleToGoogleMap(mMap, mapCircle, mapMethod.getMyLocation(), 1);
        checkButtonEnable();
    }

    private void checkButtonEnable() {
        if (tvOris.getText().toString().equals("Vị trí của bạn") && tvDes.getText().toString().equals("Điểm đến")) {
            btnFind.setEnabled(false);
        } else {
            btnFind.setEnabled(true);
        }
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btn_main2_findPath:
                try {
                    mMap.clear();
                    findPath(tvOris.getText().toString(), tvDes.getText().toString());
                } catch (Exception e) {
                    Log.d(TAG, e + "");
                }
                break;
            case R.id.tv_main2_ori:
                i = new Intent(this, SearchAutocompleteActivity.class);
                startActivityForResult(i, REQUEST_CODE_SEARCH_ORI);
                break;
            case R.id.tv_main2_des:
                i = new Intent(this, SearchAutocompleteActivity.class);
                startActivityForResult(i, REQUEST_CODE_SEARCH_DES);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SEARCH_ORI:
                if (resultCode == Activity.RESULT_OK) {
                    mMap.clear();
                    String result = data.getStringExtra(SearchAutocompleteActivity.INTENT_RETURN_MAIN);
                    tvOris.setText(result);
                    LatLng la = mapMethod.getLocationFromAddress(result);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la.latitude, la.longitude), 12.0f));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(la.latitude, la.longitude)));
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // no result
                }
                break;
            case REQUEST_CODE_SEARCH_DES:
                if (resultCode == Activity.RESULT_OK) {
                    mMap.clear();
                    String result = data.getStringExtra(SearchAutocompleteActivity.INTENT_RETURN_MAIN);
                    tvDes.setText(result);
                    LatLng la = mapMethod.getLocationFromAddress(result);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la.latitude, la.longitude), 12.0f));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(la.latitude, la.longitude)));
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // no result
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
        progressDialog.dismiss();

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
        Log.d("start: ", "3");
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 300));
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

    @Override
    public void onDirectionFinderSuccessGray(List<Route> routes) {
        Log.d("start: ", "3");
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 300));
            tvDuration.setText(route.duration.text);
            tvDistance.setText(route.distance.text);
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.GRAY).
                    width(10);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
