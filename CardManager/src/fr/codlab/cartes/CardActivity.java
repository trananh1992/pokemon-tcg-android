package fr.codlab.cartes;



import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListImageAdapter;
import fr.codlab.cartes.manageui.CarteUi;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.widget.Gallery3D;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * CLasse de visualisation d'une carte
 * 
 * 
 * @author kevin
 *
 */
public class CardActivity extends FragmentActivity implements IClickBundle{
	private CarteUi _factorise;
	private Extension _extension;
	private Gallery3D gallery;
	private Bundle _bundle;

	public CardActivity(){
		_factorise = new CarteUi();
	}


	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putBundle("BUNDLE", _bundle);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle){
		if(bundle.containsKey("BUNDLE")){
			_bundle = bundle.getBundle("BUNDLE");
		}
		
		createUi();

		if(gallery != null){
			gallery.setSelection(((Card)_bundle.getSerializable("card")).getCarteIdInt()-1);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.visucarte);
		_bundle  = this.getIntent().getExtras();

		if(_bundle.containsKey("BUNDLE")){
			_bundle = _bundle.getBundle("BUNDLE");
			Log.d("restoration","bundle trouve");
		}
		
		createUi();

		if(findViewById(R.visucarte.gallery) != null){
			Log.d("information","gallerie");

			_extension = new Extension(this.getApplicationContext(), _bundle.getInt("extension"), 0, _factorise.getSetShortName(), "", true);

			gallery = (Gallery3D)findViewById(R.visucarte.gallery);
			ExtensionListImageAdapter coverImageAdapter =  new ExtensionListImageAdapter(this.getApplicationContext(),  _extension);
			gallery.setAdapter(coverImageAdapter);
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					onClick(createBundle(position, true));
				}
			});
			gallery.setSelection(((Card)_bundle.getSerializable("card")).getCarteIdInt()-1);
		}
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

	@Override
	public void onClick(Bundle pack){
		_factorise = new CarteUi();
		_bundle = pack;
		createUi();
	}

	public void createUi(){
		//chargement de la carte
		if(_bundle != null && _bundle.containsKey("card"))
			_factorise.setCard((Card) _bundle.getSerializable("card"));

		//tout visible ? - actuellement toujours vrai
		if (_bundle != null && _bundle.containsKey("visible")) {
			_factorise.setAllObjectVisible(this.getIntent().getBooleanExtra("visible",true));
		}

		//intitule
		if (_bundle != null && _bundle.containsKey("intitule")) {
			_factorise.setSetShortName(this.getIntent().getStringExtra("intitule"));
		}

		//affichage du texte ou de l'image? - showNext = true > image
		if (_bundle != null && _bundle.containsKey("next")) {
			_factorise.showImageAtFirst(true);
		}

		//mise en forme avec le pager
		_factorise.setContext(this.findViewById(R.visucarte.top));
		_factorise.manageFirstPopulate();
	}
}
