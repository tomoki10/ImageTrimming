package com.example.imagetrimming;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class TrimingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triming);
        System.gc();
        _bmOriginal = BitmapHolder._holdedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        BitmapHolder._holdedBitmap = null;
    }
    
    Bitmap _bmOriginal;
    
    
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        
        final TrimView _tview = new TrimView(getApplicationContext());
        ((LinearLayout)findViewById(R.id.imgcontainer)).addView(_tview);
        int _width = ((FrameLayout)findViewById(R.id.fl1)).getWidth();
        int _height = ((FrameLayout)findViewById(R.id.fl1)).getHeight();
        
        //_bmOriginal = BitmapFactory.decodeResource(getResources(),R.drawable.temple);
        
        float _scaleW = (float) _width / (float) _bmOriginal.getWidth();
        float _scaleH = (float) _height / (float) _bmOriginal.getHeight();
        Log.d("TAG", String.valueOf(_scaleW));
        Log.d("TAG", String.valueOf(_scaleH));
        final float _scale = Math.max(_scaleW, _scaleH);
        Matrix matrix = new Matrix();
        matrix.postScale(_scale, _scale);
        
        //ここをsurfaceviewに切り替える
        Bitmap _bm = Bitmap.createBitmap(_bmOriginal, 0, 0, _bmOriginal.getWidth(),_bmOriginal.getHeight(), matrix, true);
        ((ImageView)findViewById(R.id.imageView1)).setImageBitmap(_bm);
        
        
        //画像のキャプチャ
        ((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ArrayList<Integer> _al = _tview.getTrimData();
                
                int _ix = (int)(_al.get(0)/_scale);
                int _iy = (int)(_al.get(1)/_scale);
                int _iwidth = (int)(_al.get(2)/_scale);
                int _iheight = (int)(_al.get(3)/_scale);
                
                _ix = (_ix>0) ? _ix : 0;
                _iy = (_iy>0) ? _iy : 0;
                
                _iwidth = (_iwidth + _ix < _bmOriginal.getWidth()) ? _iwidth : _bmOriginal.getWidth() - _ix;
                _iheight = (_iheight + _iy < _bmOriginal.getHeight()) ? _iheight : _bmOriginal.getHeight() - _iy;
                //トリミングした領域を保存する
                BitmapHolder._holdedBitmap = Bitmap.createBitmap(_bmOriginal, _ix, _iy, _iwidth, _iheight, null, true);
                
                //画像の保存
                Calendar calendar = Calendar.getInstance();;
                File filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/DCIM/Camera/" +
						"TestData"+calendar.get(Calendar.SECOND)+".jpg");
                Log.d("FilePass",String.valueOf(filePath));
                bmpSaved(BitmapHolder._holdedBitmap, filePath);
                setResult(RESULT_OK);
                finish();
            }
        });
        
        super.onWindowFocusChanged(hasFocus);
        _tview.sizeSet((int)(_bmOriginal.getWidth()*_scale),(int)(_bmOriginal.getHeight()*_scale));
    }
    
    
    private void bmpSaved(Bitmap bmp, File filePath){
    	OutputStream out = null;
		try{
			//BitmapRegionDecoder reBmp = BitmapRegionDecoder.newInstance(data, 0, data.length, false);
			//画像ファイルが生成できるならば
			out = new FileOutputStream(filePath);
			//bmp = reBmp.decodeRegion(rect, null);
			bmp.compress(CompressFormat.JPEG, 100, out);
			out.flush();
		}catch(FileNotFoundException e){
		}catch(IOException e){
		}finally{
			try{
				if(out != null)
					out.close();
			}catch(IOException ex){}
		}
    }
}
