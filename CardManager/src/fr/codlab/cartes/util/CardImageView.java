package fr.codlab.cartes.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import fr.codlab.cartes.R;

public class CardImageView {
	private static int quality = 70;
	public static void createThumb(String original){
		File _origin = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+original);
		File _mini = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/thumb_"+original);
		if(_origin.exists() && !_mini.exists()){
			Bitmap _bmp_origin = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+original);
			if(_bmp_origin != null){
				//int newWidth = 113;
				//int newHeight = 150;
				float scale_height = (150f)/_bmp_origin.getHeight();
				Matrix matrix = new Matrix();
				// resize the bit map
				matrix.postScale(scale_height, scale_height);
				// recreate the new Bitmap
				Bitmap _bmp = Bitmap.createBitmap(_bmp_origin, 0, 0,
						_bmp_origin.getWidth(), _bmp_origin.getHeight(), matrix, true);
				FileOutputStream out;
				try {
					out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/thumb_"+original);
					_bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				_bmp_origin.recycle();
				return;
			}
		}
	}
	public static void setBitmapToImageView(ImageView i, String name){
		File _mini = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/thumb_"+name+".jpg");
		if(_mini.exists()){
			Bitmap _bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/thumb_"+name+".jpg");
			i.setImageBitmap(_bmp);
			_mini = null;
			return;
		}else{
			_mini = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+name+".jpg");
			if(_mini.exists()){
				Bitmap _bmp_origin = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+name+".jpg");
				if(_bmp_origin != null){
					//int newWidth = 113;
					//int newHeight = 150;
					float scale_height = (150f)/_bmp_origin.getHeight();
					Matrix matrix = new Matrix();
					// resize the bit map
					matrix.postScale(scale_height, scale_height);

					// recreate the new Bitmap
					Bitmap _bmp = Bitmap.createBitmap(_bmp_origin, 0, 0,
							_bmp_origin.getWidth(), _bmp_origin.getHeight(), matrix, true);
					i.setImageBitmap(_bmp);
					FileOutputStream out;
					try {
						out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/thumb_"+name+".jpg");
						_bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					_bmp_origin.recycle();
					return;
				}
			}
		}
		i.setImageResource(R.drawable.back);
	}
}
