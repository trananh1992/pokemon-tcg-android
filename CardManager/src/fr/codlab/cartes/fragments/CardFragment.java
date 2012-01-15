package fr.codlab.cartes.fragments;

import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.manageui.CarteUi;
import fr.codlab.cartes.util.Card;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

final public class CardFragment extends Fragment{
	private View _this;
	private Bundle _pack;
	private CarteUi _factorise;

	public CardFragment(Bundle pack) {
		this();
		_pack = pack;
	}

	public CardFragment(){
		_factorise = new CarteUi();
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putBundle("BUNDLE", _pack);
		super.onSaveInstanceState(outState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.visucarte, container, false);		
		_this = mainView;

		return mainView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		if(savedInstanceState != null){
			restore(savedInstanceState);
		}

		if(_pack.containsKey("card"))
			_factorise.setCard((Card) _pack.getSerializable("card"));

		//tout visible ? - actuellement toujours vrai
		if (_pack.containsKey("visible")) {
			_factorise.setAllObjectVisible(_pack.getBoolean("visible",true));
		}

		//intitule
		if (_pack.containsKey("intitule")) {
			_factorise.setSetShortName(_pack.getString("intitule"));
		}

		//affichage du texte ou de l'image? - showNext = true > image
		if (_pack.containsKey("next")) {
			_factorise.showImageAtFirst(true);
		}

		//mise en forme avec le pager
		_factorise.setContext(_this);
		_factorise.manageFirstPopulate();
		setHasOptionsMenu(true);

		((MainActivity)getActivity()).setCarte(this);
	}
	
	public void setListExtension(MainActivity _activity_main){
		_activity_main.setListExtension(_this);
	}

	public void save(Bundle saveInstance){
		saveInstance.putBundle("BUNDLE", _pack);
	}
	public void restore(Bundle restore){
		_pack = restore.getBundle("BUNDLE");
	}

}
