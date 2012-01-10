package fr.codlab.cartes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VisuListExtensionFragment extends Fragment{
	View _this;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.main_pager_list, container, false);		
		_this = mainView;
		return mainView;
	}
	
	public void setListExtension(Principal _activity_main){
		_activity_main.setListExtension(_this);
	}
	
}
