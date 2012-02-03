package fr.codlab.cartes;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.manageui.ExtensionUi;
import fr.codlab.cartes.util.Extension;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Classe de visualisation des cartes d'une extension
 *
 * @author kevin le perf
 *
 */
public class ExtensionActivity extends FragmentActivity implements IExtensionListener {
	private static ExtensionUi _factorise;

	public ExtensionActivity(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(_factorise == null)
			_factorise = new ExtensionUi(this);
		_factorise.setActivity(this);

		int _id = 0;
		String _nom="";
		String _intitule="";
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
		
		_factorise.define(_nom, _id, _intitule);
		this.setContentView(R.layout.extension);
		//mise a jour du nom de l'extension et des informations
		//du nombre de cartes possedees
		Extension _extension = _factorise.getExtension();
		updateName(_nom);
		updateProgress(_extension.getProgress(),_extension.getCount());
		updatePossessed(_extension.getPossessed());

		//liste des images
		ExtensionListeAdapter _adapter = new ExtensionListeAdapter(this, this, _extension);
		ListView _liste = (ListView)findViewById(R.id.visu_extension_liste);
		_liste.setAdapter(_adapter);
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

	public boolean onCreateOptionsMenu(Menu menu) {
		return _factorise.onCreateOptionsMenu(menu, getMenuInflater());
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(_factorise.onOptionsItemSelected(item) == false)
			return super.onOptionsItemSelected(item);
		else
			return true;
	}

	@Override
	public void onClick(Bundle pack) {
		Intent intent = new Intent().setClass(this, CardActivity.class);
		intent.putExtras(pack);
		startActivityForResult(intent,42);
	}
	@Override
	public void updateName(String nom) {
		_factorise.updateName(((TextView)findViewById(R.id.visu_extension_nom)), nom);
	}

	@Override
	public void updateProgress(int progression, int count) {
		_factorise.updateProgress(((TextView)findViewById(R.id.visu_extension_cartes)), progression, count);
	}

	@Override
	public void updatePossessed(int total) {
		_factorise.updatePossessed(((TextView)findViewById(R.id.visu_extension_possess)), total);
	}
	
	@Override
	public void updated(int id) {
		Bundle bundle = new Bundle();
		bundle.putInt("update", id);
		Intent i = new Intent();
		i.putExtras(bundle);
		setResult(RESULT_OK, i); 
	}
}
