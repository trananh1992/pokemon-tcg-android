package fr.codlab.cartes.fragments;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.MainPagerAdapter;
import fr.codlab.cartes.adaptaters.MainTabletPagerAdapter;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

final public class InformationScreenFragment extends Fragment{
	View _this;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.informationscreenpager, container, false);		
		_this = mainView;
		
		ViewPager pager = (ViewPager)mainView.findViewById( R.maintablet.pager );
		if(pager != null){
			MainTabletPagerAdapter adapter = new MainTabletPagerAdapter( this );
			TitlePageIndicator indicator =
					(TitlePageIndicator)mainView.findViewById( R.maintablet.indicator );
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);
		}
		return mainView;
	}	
}
