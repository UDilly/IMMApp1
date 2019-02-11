package com.webcafeappdev.immapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Urgent Nkala
 */

public class incidentList extends AppCompatActivity {
    ListView listView;
    ArrayList<Incident> list;
    IncidentListAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidentlist);

        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new IncidentListAdapter(this, R.layout.incident_item, list);
        listView.setAdapter(adapter);

        //get all data from sqlite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM INCIDENT");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String Name = cursor.getString(1);
            String Date = cursor.getString(2);
            String Description = cursor.getString(3);
            String IncidentClass = cursor.getString(4);
            String Locations = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            list.add(new Incident(Name, Date, Description, IncidentClass, Locations, image, id));
        }

        adapter.notifyDataSetChanged();

// On longClick on the list the AlertDialog opens with options to Delete or Update information

     listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
     @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
      CharSequence[] items = {"Update", "Delete"};
       AlertDialog.Builder dialog = new AlertDialog.Builder(incidentList.this);
       dialog.setTitle("Choose and action");
       dialog.setItems(items, new DialogInterface.OnClickListener() {
       @Override
       public void onClick(DialogInterface dialog, int item) {
       if (item == 0) {
//Update
        Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM INCIDENT");
        ArrayList<Integer> arrID = new ArrayList<Integer>();
        while (c.moveToNext()) {
        arrID.add(c.getInt(0));
         }
//show dialog update at here
       showDialogUpdate(incidentList.this, arrID.get(position));
        } else {
//Delete
         Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM INCIDENT");
         ArrayList<Integer> arrID = new ArrayList<Integer>();
          while (c.moveToNext()) {
          arrID.add(c.getInt(0));
         }
         showDialogDelete(arrID.get(position));
        }
       }
      });
      dialog.show();
      return true;
         }
        });
    }
//upDATE WINDOW
    ImageView imageView1;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_incident);
        dialog.setTitle("Update incident");

        imageView1=(ImageView) dialog.findViewById(R.id.imageView1);
        final EditText Name = (EditText) dialog.findViewById(R.id.Name);
        final EditText Date = (EditText) dialog.findViewById(R.id.Date);
        final EditText Description = (EditText) dialog.findViewById(R.id.Description);
        final Spinner IncidentClass = (Spinner) dialog.findViewById(R.id.IncidentClass);
        final TextView Locations = (TextView) dialog.findViewById(R.id.Locations);
        Button btnUpdate=(Button) dialog.findViewById(R.id.btnUpdate);
        Button Cancel=(Button) dialog.findViewById(R.id.Cancel);

//set width for dialog update window
        int width=(int) (activity.getResources().getDisplayMetrics().widthPixels * 0.99);
//set height for dialog
        int height= (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.9);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        imageView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        incidentList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.sqLiteHelper.updateData(
                            Name.getText().toString().trim(),
                            Date.getText().toString().trim(),
                            Description.getText().toString().trim(),
                            IncidentClass.getSelectedItem().toString(),
                            Locations.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageView1),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateIncidentList();
            }
        });
    }
    private void showDialogDelete(final int idImage){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(incidentList.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to delete this?");
        dialogDelete.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.sqLiteHelper.deleteData(idImage);
                    Toast.makeText(getApplicationContext(), "Deleted successfully!!!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                updateIncidentList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

//This code gets data from update window to populate into sqlite database

    private void updateIncidentList(){
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM INCIDENT");
        list.clear();
        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String Name = cursor.getString(1);
            String Date = cursor.getString(2);
            String Description = cursor.getString(3);
            String IncidentClass = cursor.getString(4);
            String Locations = cursor.getString(5);
            byte[] image = cursor.getBlob(6);

            list.add(new Incident(Name, Date,Description,IncidentClass,Locations, image, id));
        }
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "No permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data !=null){
            Uri uri = data.getData();
            try {
                InputStream inputStream =
                        getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView1.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

