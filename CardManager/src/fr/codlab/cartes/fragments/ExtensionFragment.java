package fr.codlab.cartes.fragments;

import fr.codlab.cartes.IExtensionListener;
import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.manageui.ExtensionUi;
import fr.codlab.cartes.util.Extension;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

final public class ExtensionFragment extends Fragment implements IExtensionListener{
	private static ExtensionUi _factorise = null;
	private IExtensionMaster _parent;
	private View _this;

	public ExtensionFragment(){
		super();
		if(_factorise == null)
			_factorise = new ExtensionUi();
	}

	public ExtensionFragment(IExtensionMaster parent, String name, int id, String intitule){
		this();
		_parent = parent;
		define(name, id, intitule);
	}

	private void define(String name, int id, String intitule) {
		_factorise.define(name, id, intitule);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.extension, container, false);		
		_this = mainView;
		return mainView;

	}

	@Override
	public void onViewCreated(View v, Bundle saved){
		if(getActivity() instanceof MainActivity)
			((MainActivity)getActivity()).setExtension(this);
		if(_factorise == null)
			_factorise = new ExtensionUi(this.getActivity());
		else
			_factorise.setActivity(getActivity());
		setHasOptionsMenu(true);
		update();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		_factorise.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(_factorise.onOptionsItemSelected(item) == false)
			return super.onOptionsItemSelected(item);
		else
			return true;
	}

	public void onPause(){
		super.onPause();
		_factorise.onPause();
	}

	public void onResume(){
		super.onResume();
		_factorise.onResume();
	}

	public void onDestroy(){
		super.onDestroy();
		_factorise.onDestroy();
	}



	public void updateName(String nom){
		_factorise.updateName(((TextView)_this.findViewById(R.id.visu_extension_nom)), nom);
	}

	public void updateProgress(int t, int m){
		_factorise.updateProgress(((TextView)_this.findViewById(R.id.visu_extension_cartes)), t, m);
	}

	public void updatePossessed(int p){
		_factorise.updatePossessed(((TextView)_this.findViewById(R.id.visu_extension_possess)), p);
	}

	public void updated(int id){
		_parent.update(id); 
	}

	private void update(){
		try{
			Extension _extension = _factorise.getExtension();
			updateName(_extension.getName());
			updateProgress(_extension.getProgress(),_extension.getCount());
			updatePossessed(_extension.getPossessed());

			//liste des images
			ExtensionListeAdapter _adapter = new ExtensionListeAdapter(this, this.getActivity().getApplication().getApplicationContext(), _extension);
			ListView _liste = (ListView)_this.findViewById(R.id.visu_extension_liste);
			_liste.setAdapter(_adapter);
		}catch(Exception e){

		}
	}

	public void setExtension(String nom, int id, String intitule) {
		define(nom, id, intitule);
		update();
	}

	@Override
	public void onClick(Bundle pack) {
		_parent.onClick(pack);
	}

	public void setParent(IExtensionMaster parent) {
		_parent = parent;		
	}


}
