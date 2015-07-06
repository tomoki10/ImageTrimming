package com.example.imagetrimming;

import android.app.Activity;
import 	android.graphics.BitmapFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static int GETTRIM = 1;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent _intent = new Intent(getApplicationContext(),TrimingActivity.class);
        
        BitmapHolder._holdedBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        Log.d("TAG","AA");
        startActivityForResult(_intent,GETTRIM);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GETTRIM){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "画像の取得に成功しました。\nトリミングおめでとう", Toast.LENGTH_SHORT).show();
                //テスト
                //ImageView imgV = (ImageView)findViewById(R.id.imageView2);
                //Log.d("TAGGG",String.valueOf(BitmapHolder._holdedBitmap));
                //imgV.setImageBitmap(BitmapHolder._holdedBitmap);
                //BitmapHolder._holdedBitmap = null;
            }else{
                Toast.makeText(this, "画像の取得に失敗しました。", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
