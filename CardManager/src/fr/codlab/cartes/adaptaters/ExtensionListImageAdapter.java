package fr.codlab.cartes.adaptaters;

import java.io.FileInputStream;

import fr.codlab.cartes.IClickBundle;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.views.CardImage;
import fr.codlab.cartes.widget.Gallery3D;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ExtensionListImageAdapter extends BaseAdapter {
	int _background;
	private Context _context;
	private IClickBundle _clicker;
	private FileInputStream fis;

	//private ImageView[] mImages;
	private Extension _extension;
	int _mode;

	public ExtensionListImageAdapter(Context c, IClickBundle clicker, Extension extension) {
		_context = c;
		_clicker = clicker;
		_extension = extension;
		SharedPreferences _shared = _context.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
		_mode = _shared.getInt(MainActivity.USE, MainActivity.FR);
	}



	public boolean createReflectedImages() {
		return true;
		//The gap we want between the reflection and the original image
		/*final int reflectionGap = 4;


		int index = 0;
		for (int imageId : mImageIds) {
			Bitmap originalImage = BitmapFactory.decodeResource(_context.getResources(), 
					imageId);
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();


			//This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			//Create a Bitmap with the flip matrix applied to it.
			//We only want the bottom half of the image
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);


			//Create a new bitmap with same width but taller to fit reflection
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
					, (height + height/2), Config.ARGB_8888);

			//Create a new Canvas with the bitmap that's big enough for
			//the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			//Draw in the original image
			canvas.drawBitmap(originalImage, 0, 0, null);
			//Draw in the gap
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
			//Draw in the reflection
			canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);

			//Create a shader that is a linear gradient that covers the reflection
			Paint paint = new Paint(); 
			LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
					bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
					TileMode.CLAMP); 
			//Set the paint to use this shader (linear gradient)
			paint.setShader(shader); 
			//Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
			//Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width, 
					bitmapWithReflection.getHeight() + reflectionGap, paint); 

			ImageView imageView = new ImageView(_context);
			imageView.setImageBitmap(bitmapWithReflection);
			imageView.setLayoutParams(new Gallery3D.LayoutParams(120, 180));
			imageView.setScaleType(ScaleType.MATRIX);
			mImages[index++] = imageView;

		}
		return true;*/
	}

	public int getCount() {
		return _extension.getCount();
	}

	public Object getItem(int position) {
		return _extension.getCarte(position);
	}

	public long getItemId(int position) {
		return _extension.getCarte(position).getId();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		//Use this code if you want to load from resources
		ImageView i = new ImageView(_context);
		try{
			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_extension.getShortName()+"_"+_extension.getCarte(position).getCarteId()+( _mode == MainActivity.FR ? "" : "_us")+".jpg");
			if(_bmp != null)
				i.setImageBitmap(_bmp);
			else
				i.setImageResource(R.drawable.back);
		}
		catch(Exception e)
		{
		}
		//i.setImageBitmap(bm);//.setImageResource(mImageIds[position]);
		i.setLayoutParams(new Gallery3D.LayoutParams(130, 130));
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 

		//Make sure we set anti-aliasing otherwise we get jaggies
		BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
		drawable.setAntiAlias(true);

		return i;

		//return mImages[position];
	}
	/** Returns the size (0.0f to 1.0f) of the views 
	 * depending on the 'offset' to the center. */ 
	public float getScale(boolean focused, int offset) { 
		/* Formula: 1 / (2 ^ offset) */ 
		return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
	} 

}

