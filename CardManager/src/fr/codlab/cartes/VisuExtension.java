package fr.codlab.cartes;

import java.util.Random;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.dl.Downloader;
import fr.codlab.cartes.dl.DownloaderFactory;
import fr.codlab.cartes.util.Extension;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Classe de visualisation des cartes d'une extension
 *
 * @author kevin
 *
 */
public class VisuExtension extends Activity {
	private Extension _extension;
	private int _id;
	private String _nom;
	private String _intitule;
	private static Downloader _downloader;
	private static Random _rand;
	
	public VisuExtension(){
		
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(_rand == null)
        	_rand = new Random();
        
    	Bundle objetbunble  = this.getIntent().getExtras();
    	if (objetbunble != null && objetbunble.containsKey("extension")) {
        	_id = (objetbunble.getInt("extension",0));
        }

    	if (objetbunble != null && objetbunble.containsKey("nom")) {
        	_nom = this.getIntent().getStringExtra("nom");
        }
    	if (objetbunble != null && objetbunble.containsKey("intitule")) {
        	_intitule = this.getIntent().getStringExtra("intitule");
        }
        _extension = new Extension(this, _id, 0, _intitule, _nom, true);

        
		this.setContentView(R.layout.extension);

		//mise a jour du nom de l'extension et des informations
		//du nombre de cartes possedees
		updateNom();
		updateTotal(_extension.getProgression(),_extension.getCount());
		updatePossedees(_extension.getPossedees());

		//liste des images
		ExtensionListeAdapter _adapter = new ExtensionListeAdapter(this, _extension);
		ListView _liste = (ListView)findViewById(R.id.visu_extension_liste);
		_liste.setAdapter(_adapter);
	}
    

	public void onPause(){
		super.onPause();
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public void onResume(){
		super.onResume();
		if(_downloader != null)
			_downloader.downloadLoad();
	}

	public void onDestroy(){
		super.onDestroy();
		if(_downloader != null)
			_downloader.downloadQuit();
	}


	
	public boolean onOptionsItemSelected(MenuItem item) {
		//On regarde quel item a été cliqué grâce à son id et on déclenche une action
		switch (item.getItemId()) {
		case R.extension.download:
			//downloadImages();
			_downloader = DownloaderFactory.downloadFR(this,_extension.getIntitule());
			return true;
		case R.extension.downloadus:
			//downloadImages();
			_downloader = DownloaderFactory.downloadUS(this,_extension.getIntitule());
			return true;
		default:
			return false;
		}
	}
	
    public void updateNom(){
		((TextView)findViewById(R.id.visu_extension_nom)).setText(_nom);
    }
    
    public void updateTotal(int t, int m){
		((TextView)findViewById(R.id.visu_extension_cartes)).setText(" "+t+"/"+m);
    }
    
    public void updatePossedees(int p){
		TextView t = ((TextView)findViewById(R.id.visu_extension_possess));
		t.setText(" "+p);
    }
    
    public void miseAjour(){
    	Bundle bundle = new Bundle();
    	bundle.putInt("update", _id);
    	Intent i = new Intent();
    	i.putExtras(bundle);
    	setResult(RESULT_OK, i); 
    }
}
