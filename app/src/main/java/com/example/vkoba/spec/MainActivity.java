package com.example.vkoba.spec;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView result;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static Bitmap pic;
    public static int pixel,r,g,b,al;
    public float tran = (float)0.0;
    public float initialtran = 0.0f;
    public TextView text;
    public TextView inten;
    public float kek =(float)0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
//        Button button2 = (Button)findViewById(R.id.button2);
        result = (ImageView)findViewById(R.id.imageView);
        text = (TextView)findViewById(R.id.textView2);
        inten = (TextView)findViewById(R.id.textView3);
        pixel =0;
        r=0;
        g=0;
        b=0;
        al=0;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
                //text.setText(r+","+g+","+b+","+al);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            pic = (Bitmap) extras.get("data");
            pixel = pic.getPixel(pic.getWidth()/2, pic.getHeight()/2);

            r = Color.red(pixel);
            g = Color.green(pixel);
            b = Color.blue(pixel);
            float t=0.0f;
            float a = 0.0f;
            if(initialtran==0.0f){initialtran= Color.luminance(pixel);}
            else{
                tran=Color.luminance(pixel);
                t = tran/initialtran;
                a=(float)(-Math.log10(t));
            }

            float[] hsv = new float[3];
            Color.RGBToHSV(r,g,b,hsv);
            kek = (float)(700 - 1.3f*hsv[0]);

            text.setText("RGB:"+a);
            inten.setText("Wave: "+kek);
            result.setImageBitmap(pic);
        }

    }
}
