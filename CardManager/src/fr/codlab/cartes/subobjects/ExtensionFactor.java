package fr.codlab.cartes.subobjects;

import java.util.Random;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import fr.codlab.cartes.R;
import fr.codlab.cartes.dl.Downloader;
import fr.codlab.cartes.dl.DownloaderFactory;
import fr.codlab.cartes.util.Extension;

public class ExtensionFactor {
	private int _id;
	private String _name;
	private String _intitule;
	private static Downloader _downloader;
	private static Random _rand;
	private Activity _activity;
	private Extension _extension;
	private boolean to_change;

	public ExtensionFactor(Activity activity){
		this();
		setActivity(activity);
	}

	public ExtensionFactor(){
		
	}
	public void setActivity(Activity activity){
		_activity = activity;
	}
	public void definir(String name, int id, String intitule) {
		_name = name;
		_id=id;
		_intitule=intitule;
		to_change = true;
		upExtension();
	}

	private void upExtension(){
		if(_activity != null && to_change){
			_extension = new Extension(_activity.getApplication().getApplicationContext(), _id, 0, _intitule, _name, true);
			to_change = false;
		}
	}
	public Extension getExtension(){
		upExtension();
		return _extension;
	}
	
	public void onPause(){
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public void onResume(){
		if(_downloader != null)
			_downloader.downloadLoad();
	}

	public void onDestroy(){
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public boolean onOptionsItemSelected(MenuItem item){
		//On regarde quel item a été cliqué grâce à son id et on déclenche une action
		switch (item.getItemId()) {
		case R.extension.download:
			//downloadImages();
			_downloader = DownloaderFactory.downloadFR(_activity,_intitule);
			return true;
		case R.extension.downloadus:
			//downloadImages();
			_downloader = DownloaderFactory.downloadUS(_activity,_intitule);
			return true;
		default:
			return false;
		}
	}
	
	

	public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.extensionmenu, menu);

		return true;
	}
    public void updateNom(TextView v, String nom){
		v.setText(nom);
    }
    
    public void updateTotal(TextView v, int t, int m){
		v.setText(" "+t+"/"+m);
    }
    
    public void updatePossedees(TextView v, int p){
		v.setText(" "+p);
    }
}
