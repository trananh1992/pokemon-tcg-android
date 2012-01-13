package fr.codlab.cartes.fragments;

import fr.codlab.cartes.Principal;
import fr.codlab.cartes.R;
import fr.codlab.cartes.subobjects.CarteFactor;
import fr.codlab.cartes.util.CartePkmn;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CarteFragment extends Fragment{
	private View _this;
	private Bundle _pack;
	private CarteFactor _factorise;
	
	public CarteFragment(Bundle pack) {
		this();
		_pack = pack;
	}
	
	public CarteFragment(){
		_factorise = new CarteFactor();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.visucarte, container, false);		
		_this = mainView;
		if(_pack.containsKey("card"))
			_factorise.setCard((CartePkmn) _pack.getSerializable("card"));
		
		//tout visible ? - actuellement toujours vrai
		if (_pack.containsKey("visible")) {
			_factorise.setAllObjectVisible(_pack.getBoolean("visible",true));
		}
		
		//intitule
		if (_pack.containsKey("intitule")) {
			_factorise.setIntitule(_pack.getString("intitule"));
		}
		
		//affichage du texte ou de l'image? - showNext = true > image
		if (_pack.containsKey("next")) {
			_factorise.showImageAtFirst(true);
		}

		//mise en forme avec le pager
		_factorise.setContext(_this);
		_factorise.manageFirstPopulate();
		  setHasOptionsMenu(true);

		return mainView;
	}

	public void setListExtension(Principal _activity_main){
		_activity_main.setListExtension(_this);
	}
	
	public void save(Bundle saveInstance){
		saveInstance.putBundle("BUNDLE", _pack);
	}
	public void restore(Bundle restore){
		_pack = restore.getBundle("BUNDLE");
	}
}
