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
    public static int r,g,b;
    int arraysize = 50;
    int[][] pixel = new int[arraysize][arraysize];
    public float transmission = (float)0.0;
    public float initialtransmission = 0.0f;
    public TextView text;
    public TextView inten;
    public float wavelength =(float)0.0;
    float[] hsv = new float[3];
    double brightness =0.0;
    double inibrightness = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button)findViewById(R.id.button);
        final Button button2 = (Button)findViewById(R.id.button2);
        result = (ImageView)findViewById(R.id.imageView2);
        text = (TextView)findViewById(R.id.textView2);
        inten = (TextView)findViewById(R.id.textView3);
        result.setImageResource(android.R.color.transparent);
        for(int i = 0; i<arraysize; i++)
            for(int j = 0; j<arraysize; j++)
                pixel[i][j] =0;
        r=0;
        g=0;
        b=0;

        Color.RGBToHSV(r,g,b,hsv);
        text.setText("H:  "+hsv[0]+"\nS:  "+hsv[1]+"\nV:  "+hsv[2]+"\nWavelength:  "+wavelength);
        inten.setText("Brightness:\n"+brightness);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
                button.setText("Photo");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                for(int i = 0; i<arraysize; i++)
                    for(int j = 0; j<arraysize; j++)
                        pixel[i][j] =0;
                r=0;
                g=0;
                b=0;
                transmission = 0.0f;
                initialtransmission =0.0f;
                wavelength =0.0f;
                inibrightness = 0.0;
                brightness = 0.0;
                Color.RGBToHSV(r,g,b,hsv);
                result.setImageResource(android.R.color.transparent);
                text.setText("H:  "+hsv[0]+"\nS:  "+hsv[1]+"\nV:  "+hsv[2]+"\nWavelength:  "+wavelength);
                inten.setText("Brightness:\n"+brightness);
                button.setText("Calibrate");
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
            result.setImageBitmap(pic);
            for(int i = 0; i< arraysize; i++)
                for(int j = 0; j<arraysize; j++)
                    pixel[i][j] = pic.getPixel(pic.getWidth()/2-(arraysize/2)+i,pic.getHeight()/2-(arraysize/2)+j);

            for(int i = 0; i<arraysize; i++) {
                for (int j = 0; j < arraysize; j++){
                    r += Color.red(pixel[i][j]);
                    g += Color.green(pixel[i][j]);
                    b += Color.blue(pixel[i][j]);
                }
            }

            r = (int)Math.round((double)r/(arraysize*arraysize));
            g = (int)Math.round((double)g/(arraysize*arraysize));
            b = (int)Math.round((double)b/(arraysize*arraysize));
//            int pixel = pic.getPixel(pic.getWidth()/2,pic.getHeight()/2);
//            r = Color.red(pixel);
//            g = Color.green(pixel);
//            b = Color.blue(pixel);
            Color.RGBToHSV(r,g,b,hsv);

            wavelength = (float)(700.0-1.23*(hsv[0]));
//            text.setText("R: "+r+"\nG: "+g+"\nB: "+b);
           text.setText("H:"+hsv[0]+"\nS:"+hsv[1]+"\nV:"+hsv[2]+"\nWavelength:"+wavelength);
            if(inibrightness<=0.0) {
                inibrightness = Math.sqrt(.241 * r * r + .691 * g * g + .068 * g * g);
                //inibrightness = hsv[1];
            }else {
                brightness = Math.sqrt(.241 * r * r + .691 * g * g + .068 * g * g);
                //brightness = hsv[1];
                transmission = (float) (brightness / inibrightness);
                inten.setText("Brightness:\n" + transmission);
            }
        }


//            wavelength = (float)(700 - 1.3f*hsv[0]);
//            float t=0.0f;
//            float absorbance = 0.0f;
//            if(initialtransmission==0.0f){initialtransmission= Color.luminance(pixel);}
//            else{
//                transmission=Color.luminance(pixel);
//                t = transmission/initialtransmission;
//                absorbance=(float)(-Math.log10(t));
//            }
//
//            float[] hsv = new float[3];
//            Color.RGBToHSV(r,g,b,hsv);
//            wavelength = (float)(700 - 1.3f*hsv[0]);
//
//            text.setText("RGB:"+absorbance);
//            inten.setText("Wave: "+wavelength);
//            result.setImageBitmap(pic);


    }


}
