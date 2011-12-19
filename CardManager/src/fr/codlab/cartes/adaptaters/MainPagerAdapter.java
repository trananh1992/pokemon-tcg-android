package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.Principal;
import fr.codlab.cartes.R;
import fr.codlab.cartes.viewpagerindicator.TitleProvider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class MainPagerAdapter
extends PagerAdapter
implements TitleProvider{

	private String [] _titles;

	private final Principal _activity_main;


	public MainPagerAdapter(Principal context)
	{
		_activity_main = context;
		_titles = new String[]{
				_activity_main.getString(R.string.principal_title),
				_activity_main.getString(R.string.principal_list)};
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

		if(position == 0){
			LayoutInflater inflater = (LayoutInflater) _activity_main
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.main_pager, null);
		}else{
			LayoutInflater inflater = (LayoutInflater) _activity_main
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.main_pager_list, null);
			_activity_main.setListExtension(v);
		}
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
