package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.Carte;
import fr.codlab.cartes.R;
import fr.codlab.cartes.subobjects.CarteFactor;
import fr.codlab.cartes.viewpagerindicator.TitleProvider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class VisuCartePagerAdapter extends PagerAdapter implements TitleProvider{

	private String [] _titles;
	private final Context _context;
	private final CarteFactor _activity_main;


	public VisuCartePagerAdapter(Context context, CarteFactor parent)
	{
		_activity_main = parent;
		_context = context;
		_titles = new String[]{
				_context.getString(R.string.card_img),
				_context.getString(R.string.card_info)};
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		try{
			((ViewPager)pager).removeView((View)view);
		}catch(Exception e){
			
		}
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
		return _titles.length;
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		View v = null;
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
		if(position == 0){
			v = inflater.inflate(R.layout.visucarte_image, null);
			_activity_main.populateImage(v);
		}else{
			v = inflater.inflate(R.layout.visucarte_text, null);
			_activity_main.populateText(v);
		}
		//TextView v = new TextView(_activity_main);
		//v.setText(_titles[position]);
		((ViewPager)pager).addView( v, 0 );
		return v;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	@Override
	public String getTitle(int position) {
		return _titles[position];
	}

}
