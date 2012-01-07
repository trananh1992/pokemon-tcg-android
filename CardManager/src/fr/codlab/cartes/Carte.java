package fr.codlab.cartes;



import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.VisuCartePagerAdapter;
import fr.codlab.cartes.attributes.Attaque;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.util.CartePkmn;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * CLasse de visualisation d'une carte
 * 
 * 
 * @author kevin
 *
 */
public class Carte extends Activity {
	private CartePkmn _card;
	private String _intitule;
	private boolean showNext = false;
	private boolean _all_visible=true;	

	public Carte(){

	}

	private void onCreateDefault(){		
		showNext = false;
		_all_visible=true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		onCreateDefault();

		Bundle objetbunble  = this.getIntent().getExtras();
		
		//chargement de la carte
		if(objetbunble != null && objetbunble.containsKey("card"))
			_card = (CartePkmn) objetbunble.getSerializable("card");
		
		//tout visible ? - actuellement toujours vrai
		if (objetbunble != null && objetbunble.containsKey("visible")) {
			_all_visible = this.getIntent().getBooleanExtra("visible",true);
		}
		
		//intitule
		if (objetbunble != null && objetbunble.containsKey("intitule")) {
			_intitule = this.getIntent().getStringExtra("intitule");
		}
		
		//affichage du texte ou de l'image? - showNext = true > image
		if (objetbunble != null && objetbunble.containsKey("next")) {
			showNext = true;
		}

		//mise en forme avec le pager
		this.setContentView(R.layout.visucarte);

		ViewPager pager = (ViewPager)findViewById( R.id.viewpager );
		if(pager != null){
			VisuCartePagerAdapter adapter = new VisuCartePagerAdapter( this );
			TitlePageIndicator indicator =
					(TitlePageIndicator)findViewById( R.id.indicator );
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);

			if(showNext)
				pager.setCurrentItem(0);
			else
				pager.setCurrentItem(1);
		}
	}

	public void populateImage(View activity){
		if(_intitule != null){
			//chargement de l'image
			ImageView iv = (ImageView)activity.findViewById(R.carte.visu);
			SharedPreferences _shared = getSharedPreferences(Principal.PREFS, Carte.MODE_PRIVATE);
			int _mode = _shared.getInt(Principal.USE, Principal.FR);

			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_intitule+"_"+_card.getCarteId()+(_mode == Principal.FR ? "" : "_us" )+".jpg");

			//si le scan existe, on le charge
			if(_bmp != null)
				iv.setImageBitmap(_bmp);
			else
				iv.setImageResource(R.drawable.back);
		}
	}

	public void populateText(View activity){
		//mise a jour de la description, nom et numero
		((TextView)activity.findViewById(R.id.carte_description)).setText(_card.getDescription());
		((TextView)activity.findViewById(R.id.carte_numero)).setText(" "+_card.getCarteId());
		((TextView)activity.findViewById(R.id.carte_nom)).setText(_card.getNom());

		//mise a jour pv
		if(_all_visible && _card.getPV()>0)
			((TextView)activity.findViewById(R.id.carte_pv)).setText(Integer.toString(_card.getPV()));

		//affichage common, rare, uncommon, holo, ultra
		if(getResources().getIdentifier(_card.getDrawableRarete() , "drawable", this.getPackageName())>0){
			ImageView iv = (ImageView) activity.findViewById(R.id.carte_rarete);
			iv.setImageResource(getResources().getIdentifier(_card.getDrawableRarete(), "drawable", this.getPackageName()));
		}

		
		
		//gestion des attaques
		LinearLayout attaques = (LinearLayout)activity.findViewById(R.id.carte_attaques);
		attaques.removeAllViews();
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View _current_attack = null;
		Attaque attaque;
		for(int index = 0; _all_visible && index < _card.getNbAttaques(); index++){
			//creation d'un layout secondaire pour l'attaque actuelle
			_current_attack = inflater.inflate(R.layout.infos_attaque, null);
			//recuperation de l'objet attaque actuel
			attaque = _card.getAttaque(index);
			//ajout du layout au layout principal
			attaques.addView(_current_attack);
			//modification du nom, degats et description
			((TextView)_current_attack.findViewById(R.id.nom_attaque)).setText(attaque.getNom());
			((TextView)_current_attack.findViewById(R.id.attaque_degat)).setText(""+attaque.getDegats());
			((TextView)_current_attack.findViewById(R.id.description)).setText(attaque.getDescription());
			String [] _types = attaque.getTypes();
			LinearLayout _vue_types = (LinearLayout)_current_attack.findViewById(R.id.type);
			_vue_types.removeAllViews();
			//ajout des types de l'attaque
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_types[i] , "drawable", this.getPackageName()));
				_vue_types.addView(_type);
			}
		}
		
		String [] _faiblesses = _card.getFaiblesses();
		if(_faiblesses!=null){
			LinearLayout _vue_faiblesses = (LinearLayout)activity.findViewById(R.id.carte_faiblesse);
			if(!_all_visible)
				_vue_faiblesses.removeAllViews();
			for(int i=0;i<_faiblesses.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_faiblesses[i] , "drawable", this.getPackageName()));
				_vue_faiblesses.addView(_type);
			}
		}
		
		String [] _resistances = _card.getResistances();
		if(_resistances!=null){
			LinearLayout _vue_resistances = (LinearLayout)activity.findViewById(R.id.carte_resistance);
			if(!_all_visible)
				_vue_resistances.removeAllViews();
			for(int i=0;i<_resistances.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_resistances[i] , "drawable", this.getPackageName()));
				_vue_resistances.addView(_type);
			}
		}
		
		LinearLayout pouvoirs = (LinearLayout)activity.findViewById(R.id.carte_pokebody_pokepower);
		pouvoirs.removeAllViews();
		//creations et ajouts des PokePower et PokeBody
		PokePower _pokepower = _card.getPokePower();
		if(_all_visible && _pokepower!=null){
			View pouvoir = inflater.inflate(R.layout.pokepower, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokepower_nom)).setText(_pokepower.getNom());
			((TextView)pouvoir.findViewById(R.id.pokepower_description)).setText(_pokepower.getDescription());
		}
		PokeBody _pokebody = _card.getPokeBody();
		if(_all_visible && _pokebody!=null){
			View pouvoir = inflater.inflate(R.layout.pokebody, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokebody_nom)).setText(_pokebody.getNom());
			((TextView)pouvoir.findViewById(R.id.pokebody_description)).setText(_pokebody.getDescription());
		}


		//ajout des couts de retraite de la carte
		LinearLayout _vue_retraite = (LinearLayout)activity.findViewById(R.id.carte_retraite);
		
		int _retraite = _card.getRetraite();
		for(int i=0;_all_visible && i<_retraite;i++){
			ImageView _type = new ImageView(this);
			_type.setImageResource(this.getResources().getIdentifier("type_incolore" , "drawable", this.getPackageName()));
			_vue_retraite.addView(_type);
		}
		if(!_all_visible)
			_vue_retraite.removeAllViews();			
	}

}
