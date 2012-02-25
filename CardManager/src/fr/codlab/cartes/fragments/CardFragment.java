package fr.codlab.cartes.fragments;

import fr.codlab.cartes.IClickBundle;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListImageAdapter;
import fr.codlab.cartes.manageui.CarteUi;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.widget.Gallery3D;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

final public class CardFragment extends Fragment implements IClickBundle{
	private View _this;
	private Bundle _pack;
	private CarteUi _factorise;
	private Extension _extension;
	private Gallery3D gallery;
	
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
	public void onClick(Bundle pack){
		_factorise = new CarteUi();
		_pack = pack;
		createUi();
	}

	public Bundle createBundle(int _pos,boolean imgVue){
		Bundle objetbundle = new Bundle();
		//objetbundle.putInt("nb", _item.getCarte(_pos).getNb());
		objetbundle.putSerializable("card", _extension.getCarte(_pos));
		if(imgVue)
			objetbundle.putInt("next", 1);
		objetbundle.putInt("extension", _extension.getId());
		objetbundle.putString("intitule", _extension.getShortName());

		return objetbundle;
	}

	public void createUi(){
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
		
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		if(savedInstanceState != null){
			restore(savedInstanceState);
		}


		createUi();

		if(getActivity().findViewById(R.visucarte.gallery) != null){
			_extension = new Extension(getActivity().getApplicationContext(), _pack.getInt("extension"), 0, _factorise.getSetShortName(), "", true);

			gallery = (Gallery3D)getActivity().findViewById(R.visucarte.gallery);
			ExtensionListImageAdapter coverImageAdapter =  new ExtensionListImageAdapter(getActivity(), _extension);
			gallery.setAdapter(coverImageAdapter);
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					onClick(createBundle(position, true));
				}		   
			});
			gallery.setSelection(((Card)_pack.getSerializable("card")).getCarteIdInt()-1);
		}
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
