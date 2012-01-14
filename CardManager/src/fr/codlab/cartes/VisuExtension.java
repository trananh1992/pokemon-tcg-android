package fr.codlab.cartes;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.subobjects.ExtensionFactor;
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
 * @author kevin
 *
 */
public class VisuExtension extends FragmentActivity implements IExtensionListener {
	private static ExtensionFactor _factorise;

	public VisuExtension(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(_factorise == null)
			_factorise = new ExtensionFactor(this);

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
		
		_factorise.definir(_nom, _id, _intitule);
		this.setContentView(R.layout.extension);
		//mise a jour du nom de l'extension et des informations
		//du nombre de cartes possedees
		Extension _extension = _factorise.getExtension();
		updateNom(_nom);
		updateTotal(_extension.getProgression(),_extension.getCount());
		updatePossedees(_extension.getPossedees());

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

	public void updateNom(String nom){
		_factorise.updateNom(((TextView)findViewById(R.id.visu_extension_nom)), nom);
	}

	public void updateTotal(int t, int m){
		_factorise.updateTotal(((TextView)findViewById(R.id.visu_extension_cartes)), t, m);
	}

	public void updatePossedees(int p){
		_factorise.updatePossedees(((TextView)findViewById(R.id.visu_extension_possess)), p);
	}

	public void miseAjour(int id){
		Bundle bundle = new Bundle();
		bundle.putInt("update", id);
		Intent i = new Intent();
		i.putExtras(bundle);
		setResult(RESULT_OK, i); 
	}
	@Override
	public void onClick(Bundle pack) {
		Intent intent = new Intent().setClass(this, Carte.class);
		intent.putExtras(pack);
		startActivityForResult(intent,42);
	}
}
