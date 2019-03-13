package com.jskgmail.touristers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView compass_img;
    TextView txt_compass;
    private int playpause=0;
    int mAzimuth;
    TextView tt;
    int cc=0;

    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    MediaPlayer sound = null;
    MediaPlayer sound1 = null;

    FirebaseDatabase database ;

    DatabaseReference myRef,myRef1 ;
            int value1,value2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.img_compass);
        txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        sound = MediaPlayer.create(this,R.raw.t);
        sound1 = MediaPlayer.create(this, R.raw.digifest);

         database = FirebaseDatabase.getInstance();



        SharedPreferences prefs = getSharedPreferences("no",MODE_PRIVATE);
        String restoredText = prefs.getString("no", null);
if (restoredText==null) {
    LayoutInflater inflater = getLayoutInflater();
    View alertLayout = inflater.inflate(R.layout.name, null);

    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    final EditText editText = alertLayout.findViewById(R.id.editText);
    // this is set the view from XML inside AlertDialog
    alert.setView(alertLayout);
    // disallow cancel of AlertDialog on click of back button and outside touch
    alert.setTitle("Enter no ");
    alert.setIcon(R.drawable.ic_arrow_forward_black_24dp);


    alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {


        @Override
        public void onClick(DialogInterface dialog, int which) {

            SharedPreferences.Editor editor = getSharedPreferences("no", MODE_PRIVATE).edit();
            editor.putString("no", editText.getText().toString());
            scanWifiList();


            editor.apply();


        }
    });
    AlertDialog dialog = alert.create();
    dialog.show();


}

else
{
    scanWifiList();

}




        start();


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.img_compass);
        txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        sound = MediaPlayer.create(this,R.raw.subsite1);
        Button addon = (Button) findViewById(R.id.addon);


        start();

        scanWifiList();

        addon.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                Intent addon = new Intent(MainActivity.this, Addon.class);

                // Start the new activity
                startActivity(addon);
            }
        });

        final ImageView play=findViewById(R.id.imageView4);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (playpause == 0) {
                    playpause = 1;
                    play.setImageResource(R.drawable.ic_pause_black_24dp);
                        sound.start();
                     } else {
                    playpause = 0;
                    sound.stop();
                    sound=MediaPlayer.create(getApplicationContext(), R.raw.subsite1);
                    //    sound.release();
                    play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

                }

            }
        });

        play.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

go();



                return false;
            }
        });
    }

void go()
{
    LayoutInflater inflater = getLayoutInflater();
    View alertLayout = inflater.inflate(R.layout.instantdir, null);
    final ImageView canteen=alertLayout.findViewById(R.id.can);
    final ImageView wash=alertLayout.findViewById(R.id.wash);
    final ImageView exit=alertLayout.findViewById(R.id.exit);

    AlertDialog.Builder alert = new AlertDialog.Builder(this);

    // this is set the view from XML inside AlertDialog
    alert.setView(alertLayout);
    // disallow cancel of AlertDialog on click of back button and outside touch

    AlertDialog dialog = alert.create();
    dialog.show();

}






    private void scanWifiList1() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();


            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    final int rss = wifiInfo.getRssi();
                    //       Toast.makeText(getApplicationContext(),"a",Toast.LENGTH_SHORT).show();

                    scanWifiList();

                    SharedPreferences prefs = getSharedPreferences("no", MODE_PRIVATE);
                    String restoredText = prefs.getString("no", null);

                    // Write a message to the database
                    if (restoredText != null) {
                        myRef = database.getReference(restoredText);

                        myRef.setValue(rss);

                    }


                    myRef = database.getReference("2");
                    myRef1 = database.getReference("1");

                    myRef.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            Integer value = dataSnapshot.getValue(Integer.class);
                            Log.d("mmmm", "Value is: " + value);
                            value1 = value;
                            //    trilateration(new int[]{10, 20, 30}, new int[]{rss, value, 0});

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("mmmmmmmmm", "Failed to read value.", error.toException());
                        }
                    });


                    // close this activity


                    myRef1.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.)
                            Integer value = dataSnapshot.getValue(Integer.class);
                            Log.d("mmmm", "Value is: " + value);
                            value2 = value;
                            //trilateration(new int[]{10, 20, 30}, new int[]{rss, value1, value2});

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("mmmmmmmmm", "Failed to read value.", error.toException());
                        }
                    });


                    // close this activity
                }


            }, 2000);
        }








}

















































    private void scanWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        if (wifiManager.isWifiEnabled())
        {
            final WifiInfo wifiInfo=wifiManager.getConnectionInfo();


            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    tt=findViewById(R.id.textView3);
//                    Toast.makeText(getApplicationContext(),wifiInfo.getRssi()+"a",Toast.LENGTH_SHORT).show();

                    if ((Math.abs(wifiInfo.getRssi())<40)&&(Math.abs(wifiInfo.getRssi())>5))
                    {
                        tt.setText("SUBSITE I");
cc=0;
                    }

                    else if ((Math.abs(wifiInfo.getRssi())>40)&&(Math.abs(wifiInfo.getRssi())<100))
                    {
                        tt.setText("SUBSITE II");
cc=1;
                    }
                    else
                        tt.setText("You are out!!!");
                    cc=2;
                    scanWifiList();
                    // close this activity

                }
            }, 2000);
        }
    }























































    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        compass_img.setRotation(-mAzimuth);

        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = "N";
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";


        txt_compass.setText(mAzimuth + "° " + where);
        if(mAzimuth <350 && mAzimuth > 280 ) {
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }










    int x, y;
    int x1, x2, x3, y1, y2, y3 = 0;

    void trilateration(int c[],int r[]){
//            # Trilateration based on 3 coordinates in c and 3 radii in r
//# C input should be a tuple list of coordinates
//# Inputs should be all in equal dimensions (ideal pixel units)
//# if len(c) != 3 or len(r) != 3:
//            # print 'Improper input.. 3 element list of coordinates expected for c'
//            # break
        int x1=c[0], y1 = c[0];
        int x2 = c[1], y2 = c[1];
        int x3=c[2], y3 = c[2];
        int r1 = r[0];
        int r2 = r[1];
        int r3 = r[2];
        // int c = (int) 0.1;
//        #Tolerance value for our checksum
//# Trilateration Linear equation parameters
        int A = -2 * x1 + 2 * x2;
        int B = -2 * y1 + 2 * y2;
        int C = r1 * r1 - r2 * r2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2;
        int D = -2 * x2 + 2 * x3;
        int E = -2 * y2 + 2 * y3;
        int F = r2 * r2 - r3 * r3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3;
//            #Obtain the actual x, y values

        int x = (C * E - F * B) / (E * A - B * D);
        int y = (C * D - A * F) / (B * D - A * E);



        Log.e("xxxxxxxxxxxxx", String.valueOf(x));
        Log.e("yyyyyyyyyyyyx", String.valueOf(y));





//            #Checksum on results
//#if y > max(y1, y2, y3) or y <min(y1, y2, y3) or x >max(x1, x2, x3) or x <min(x1, x2, x3):
//            #print 'No Real intersection'
//            #return 0
    }






























}

