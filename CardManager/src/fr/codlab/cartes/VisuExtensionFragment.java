package fr.codlab.cartes;

import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.util.Extension;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class VisuExtensionFragment extends Fragment implements ExtensionListener{
	View _this;
	String _name;
	int _id;
	String _intitule;

	public VisuExtensionFragment(){
		super();
	}
	
	public VisuExtensionFragment(String name, int id, String intitule){
		super();
		definir(name, id, intitule);
	}

	private void definir(String name, int id, String intitule) {
		_name = name;
		_id=id;
		_intitule=intitule;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.extension, container, false);		
		_this = mainView;

		up();
		return mainView;
	}

	public void updateNom(String nom){
		((TextView)_this.findViewById(R.id.visu_extension_nom)).setText(nom);
	}

	public void updateTotal(int t, int m){
		((TextView)_this.findViewById(R.id.visu_extension_cartes)).setText(" "+t+"/"+m);
	}

	public void updatePossedees(int p){
		TextView t = ((TextView)_this.findViewById(R.id.visu_extension_possess));
		t.setText(" "+p);
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
			Extension _extension = new Extension(this.getActivity().getApplication().getApplicationContext(), _id, 0, _intitule, _name, true);
			updateNom(_name);
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
	
	
}
