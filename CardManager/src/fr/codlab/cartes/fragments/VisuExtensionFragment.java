package fr.codlab.cartes.fragments;

import java.util.Random;

import fr.codlab.cartes.ExtensionListener;
import fr.codlab.cartes.Principal;
import fr.codlab.cartes.R;
import fr.codlab.cartes.R.id;
import fr.codlab.cartes.R.layout;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.dl.Downloader;
import fr.codlab.cartes.dl.DownloaderFactory;
import fr.codlab.cartes.subobjects.ExtensionFactor;
import fr.codlab.cartes.util.Extension;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class VisuExtensionFragment extends Fragment implements ExtensionListener{
	private static ExtensionFactor _factorise = null;
	private Principal _parent;
	private View _this;

	public VisuExtensionFragment(){
		super();
		if(_factorise == null)
			_factorise = new ExtensionFactor();
	}
	
	public VisuExtensionFragment(Principal parent, String name, int id, String intitule){
		this();
		_parent = parent;
		definir(name, id, intitule);
	}

	private void definir(String name, int id, String intitule) {
		_factorise.definir(name, id, intitule);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.extension, container, false);		
		_this = mainView;
		if(_factorise == null)
			_factorise = new ExtensionFactor(this.getActivity());
		else
			_factorise.setActivity(getActivity());
		this.setHasOptionsMenu(true);
		up();
		return mainView;
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



	public void updateNom(String nom){
		_factorise.updateNom(((TextView)_this.findViewById(R.id.visu_extension_nom)), nom);
	}

	public void updateTotal(int t, int m){
		_factorise.updateTotal(((TextView)_this.findViewById(R.id.visu_extension_cartes)), t, m);
	}

	public void updatePossedees(int p){
		_factorise.updatePossedees(((TextView)_this.findViewById(R.id.visu_extension_possess)), p);
	}

	public void miseAjour(int id){
		Bundle bundle = new Bundle();
		bundle.putInt("update", id);
		Intent i = new Intent();
		i.putExtras(bundle);
		//setResult(i); 
	}

	private void up(){
		try{
			Extension _extension = _factorise.getExtension();
			updateNom(_extension.getNom());
			updateTotal(_extension.getProgression(),_extension.getCount());
			updatePossedees(_extension.getPossedees());

			//liste des images
			ExtensionListeAdapter _adapter = new ExtensionListeAdapter(this, this.getActivity().getApplication().getApplicationContext(), _extension);
			ListView _liste = (ListView)_this.findViewById(R.id.visu_extension_liste);
			_liste.setAdapter(_adapter);
		}catch(Exception e){

		}
	}

	public void setExtension(String nom, int id, String intitule) {
		definir(nom, id, intitule);
		up();
	}

	@Override
	public void onClick(Bundle pack) {
		_parent.onClick(pack);
	}
	
	
}
