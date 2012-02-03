package fr.codlab.cartes.manageui;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import fr.codlab.cartes.R;
import fr.codlab.cartes.dl.Downloader;
import fr.codlab.cartes.dl.DownloaderFactory;
import fr.codlab.cartes.util.Extension;

/**
 * Class made to manage how a specified Ui must be modified with set data
 * @author kevin le perf
 *
 */
final public class ExtensionUi {
	private int _id;
	private String _name;
	private String _intitule;
	private static Downloader _downloader;
	private Activity _activity;
	private Extension _extension;
	private boolean to_change;

	public ExtensionUi(Activity activity){
		this();
		setActivity(activity);
	}

	private ExtensionUi(){
		
	}
	public void setActivity(Activity activity){
		_activity = activity;
	}
	public void define(String name, int id, String intitule) {
		_name = name;
		_id=id;
		_intitule=intitule;
		to_change = true;
		update();
	}

	private void update(){
		if(_activity != null && to_change){
			_extension = new Extension(_activity.getApplication().getApplicationContext(), _id, 0, _intitule, _name, true);
			to_change = false;
		}
	}
	public Extension getExtension(){
		update();
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
		switch (item.getItemId()) {
		case R.extension.download:
			_downloader = DownloaderFactory.downloadFR(_activity,_intitule);
			return true;
		case R.extension.downloadus:
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
	
    public void updateName(TextView v, String nom){
		v.setText(nom);
    }
    
    public void updateProgress(TextView v, int t, int m){
		v.setText(" "+t+"/"+m);
    }
    
    public void updatePossessed(TextView v, int p){
		v.setText(" "+p);
    }
}
