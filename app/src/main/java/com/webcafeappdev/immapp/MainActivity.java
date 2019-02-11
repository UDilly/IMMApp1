package com.webcafeappdev.immapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText Name;
    EditText Date;
    EditText Description;
    Spinner IncidentClass;
    TextView Locations;
    ImageView imageView;
    Button btnSub,btnList_incident, pick_location;
    Geocoder geocoder;
    List<Address> address;
    LocationManager locationManager;
    LocationListener locationListener;



    final int REQUEST_CODE_GALLERY = 999;
    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//This code creates database and table

        init();
        sqLiteHelper = new SQLiteHelper(this,"IncidentDB.sqlite",null,1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS INCIDENT (Id INTEGER PRIMARY KEY AUTOINCREMENT,Name VARCHAR, Date VARCHAR, Description VARCHAR,IncidentClass VARCHAR,Locations VARCHAR, image BLOG ) ");

//This code request for permisiion to use the gallery
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
//gets inputed data from text views to submit to database
        btnSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqLiteHelper.insertData(
                            Name.getText().toString().trim(),
                            Date.getText().toString().trim(),
                            Description.getText().toString().trim(),
                            IncidentClass.getSelectedItem().toString().trim(),
                            Locations.getText().toString().trim(),
                            imageViewToByte(imageView));

                    //reset the text views to blank
                    Toast.makeText(getApplicationContext(), "Your incident has been reported successfully!", Toast.LENGTH_SHORT).show();
                    Name.setText("");
                    Date.setText("");
                    Description.setText("");
                    IncidentClass.setSelection(Integer.parseInt(""));
                    Locations.setText("");
                    imageView.setImageResource(R.mipmap.ic_immapplogo);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
//opens the Incident list view activity
        btnList_incident.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, incidentList.class);
                startActivity(intent);
            }
        });

    }

// This code will convert bitmap image to jpeg format

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return byteArray;
    }
// grants permission to pick an image from gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "No permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void init() {

        Calendar calendar= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance().format(calendar.getTime());
        Date = (EditText) findViewById(R.id.Date);
        Date.setText(currentDate);
        Name = (EditText) findViewById(R.id.Name);
        Description = (EditText) findViewById(R.id.Description);
        IncidentClass = (Spinner) findViewById(R.id.IncidentClass);
        Locations = (TextView) findViewById(R.id.Locations);
        btnSub = (Button) findViewById(R.id.btnSub);
      // btntake_image = (Button) findViewById(R.id.btntake_image);
        btnList_incident = (Button) findViewById(R.id.btnList_incident);
        pick_location = (Button) findViewById(R.id.pick_location);
      //  btnViewMap =(Button)findViewById(R.id.btnViewMap);
        imageView = (ImageView) findViewById(R.id.imageView);


//This code is to locate a gps location

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Locations.append("\n " + location.getLatitude() + " " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            configureButton();
        }
    }
    private void configureButton() {
        pick_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                locationManager.requestLocationUpdates("gps",50000000, 0, locationListener);
            }

        });
    }
}


