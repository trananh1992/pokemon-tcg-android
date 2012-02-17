package fr.codlab.cartes.fragments;

import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.MainTabletPagerAdapter;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

final public class InformationScreenFragment extends Fragment{
	private IExtensionMaster _master;
	
	public InformationScreenFragment(){
		
	}

	public InformationScreenFragment(IExtensionMaster master){
		this();
		_master = master;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.informationscreenpager, container, false);
		
		ViewPager pager = (ViewPager)mainView.findViewById( R.maintablet.pager );
		if(pager != null){
			MainTabletPagerAdapter adapter = new MainTabletPagerAdapter(this, _master);
			TitlePageIndicator indicator =
					(TitlePageIndicator)mainView.findViewById( R.maintablet.indicator );
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);
		}
		return mainView;
	}	
}
