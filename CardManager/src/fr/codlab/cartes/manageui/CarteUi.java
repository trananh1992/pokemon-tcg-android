package fr.codlab.cartes.manageui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.VisuCartePagerAdapter;
import fr.codlab.cartes.attributes.Ability;
import fr.codlab.cartes.attributes.Attack;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;

/**
 * Class made to manage how a specified Ui must be modified with card data
 * @author kevin le perf
 *
 */
final public class CarteUi {
	private Card _card;
	private String _intitule;
	private boolean showNext = false;
	private boolean _all_visible=true;	
	private View _root;

	public CarteUi(){
		showNext = false;
		_all_visible=true;
	}

	public void setCard(Card carte){
		_card = carte;
	}
	public Card getCard(){
		return _card;
	}

	/**
	 * In case of trainer / Pokemon for instance
	 * @param state
	 */
	public void setAllObjectVisible(boolean state){
		_all_visible = state;
	}
	public boolean getAllObjectVisible(boolean state){
		return _all_visible;
	}

	public void showImageAtFirst(boolean state){
		showNext = state;
	}
	public boolean getshowImageAtFirst(){
		return showNext;
	}

	public void setSetShortName(String intitule){
		_intitule = intitule;
	}
	public String getSetShortName(){
		return _intitule;
	}

	public void setContext(View root){
		_root = root;
	}

	public void manageFirstPopulate(){
		Log.d("manage","firstpopulate");
		ViewPager pager = (ViewPager)_root.findViewById( R.id.viewpager );
		if(pager != null){
			VisuCartePagerAdapter adapter = new VisuCartePagerAdapter(_root.getContext(), this);
			TitlePageIndicator indicator =
					(TitlePageIndicator)_root.findViewById( R.id.indicator );
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);
			adapter.notifyDataSetChanged();
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
			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_intitule+"_"+_card.getCarteId()+(MainActivity.InUse == MainActivity.FR ? "" : "_us" )+".jpg");

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

		boolean _all_visible = this._all_visible;
		_all_visible = _card.getVisible();
		//mise a jour pv
		if(_all_visible && _card.getPV()>0)
			((TextView)activity.findViewById(R.id.carte_pv)).setText(Integer.toString(_card.getPV()));
		if(!_all_visible){
			((View)activity.findViewById(R.carte.hpzone)).setVisibility(View.GONE);
		}

		//affichage common, rare, uncommon, holo, ultra
		if(_root.getContext().getResources().getIdentifier(_card.getDrawableRarete() , "drawable", _root.getContext().getPackageName())>0){
			ImageView iv = (ImageView) activity.findViewById(R.id.carte_rarete);
			iv.setImageResource(_root.getContext().getResources().getIdentifier(_card.getDrawableRarete(), "drawable", _root.getContext().getPackageName()));
		}



		//gestion des attaques
		LinearLayout attaques = (LinearLayout)activity.findViewById(R.id.carte_attaques);
		attaques.removeAllViews();
		LayoutInflater inflater = (LayoutInflater)_root.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View _current_attack = null;
		Attack attaque;
		for(int index = 0; _all_visible && index < _card.getNbAttaques(); index++){
			//creation d'un layout secondaire pour l'attaque actuelle
			_current_attack = inflater.inflate(R.layout.infos_attaque, null);
			//recuperation de l'objet attaque actuel
			attaque = _card.getAttaque(index);
			//ajout du layout au layout principal
			attaques.addView(_current_attack);
			//modification du nom, degats et description
			((TextView)_current_attack.findViewById(R.id.nom_attaque)).setText(attaque.getName());
			((TextView)_current_attack.findViewById(R.id.attaque_degat)).setText(""+attaque.getDamage());
			((TextView)_current_attack.findViewById(R.id.description)).setText(attaque.getDescription());
			String [] _types = attaque.getTypes();
			LinearLayout _vue_types = (LinearLayout)_current_attack.findViewById(R.id.type);
			_vue_types.removeAllViews();
			//ajout des types de l'attaque
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(_root.getContext());
				_type.setImageResource(_root.getContext().getResources().getIdentifier("type_"+_types[i] , "drawable", _root.getContext().getPackageName()));
				_vue_types.addView(_type);
			}
		}

		String [] _faiblesses = _card.getFaiblesses();
		if(_faiblesses!=null){
			LinearLayout _vue_faiblesses = (LinearLayout)activity.findViewById(R.id.carte_faiblesse);
			if(!_all_visible)
				_vue_faiblesses.removeAllViews();
			for(int i=0;i<_faiblesses.length;i++){
				ImageView _type = new ImageView(_root.getContext());
				_type.setImageResource(_root.getContext().getResources().getIdentifier("type_"+_faiblesses[i] , "drawable", _root.getContext().getPackageName()));
				_vue_faiblesses.addView(_type);
			}
		}

		String [] _resistances = _card.getResistances();
		if(_resistances!=null){
			LinearLayout _vue_resistances = (LinearLayout)activity.findViewById(R.id.carte_resistance);
			if(!_all_visible)
				_vue_resistances.removeAllViews();
			for(int i=0;i<_resistances.length;i++){
				ImageView _type = new ImageView(_root.getContext());
				_type.setImageResource(_root.getContext().getResources().getIdentifier("type_"+_resistances[i] , "drawable", _root.getContext().getPackageName()));
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
			((TextView)pouvoir.findViewById(R.id.pokepower_nom)).setText(_pokepower.getName());
			((TextView)pouvoir.findViewById(R.id.pokepower_description)).setText(_pokepower.getDescription());
		}
		PokeBody _pokebody = _card.getPokeBody();
		if(_all_visible && _pokebody!=null){
			View pouvoir = inflater.inflate(R.layout.pokebody, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokebody_nom)).setText(_pokebody.getName());
			((TextView)pouvoir.findViewById(R.id.pokebody_description)).setText(_pokebody.getDescription());
		}
		Ability _ability = _card.getAbility();
		if(_all_visible && _ability!=null){
			View pouvoir = inflater.inflate(R.layout.ability, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.ability_nom)).setText(_ability.getName());
			((TextView)pouvoir.findViewById(R.id.ability_description)).setText(_ability.getDescription());
		}


		String _desc = _card.getDescription();
		TextView _txt_desc = (TextView)activity.findViewById(R.id.carte_description);
		_txt_desc.setText(_desc);

		//ajout des couts de retraite de la carte
		LinearLayout _vue_retraite = (LinearLayout)activity.findViewById(R.id.carte_retraite);

		int _retraite = _card.getRetraite();
		for(int i=0;_all_visible && i<_retraite;i++){
			ImageView _type = new ImageView(_root.getContext());
			_type.setImageResource(_root.getContext().getResources().getIdentifier("type_incolore" , "drawable", _root.getContext().getPackageName()));
			_vue_retraite.addView(_type);
		}
		if(!_all_visible)
			_vue_retraite.removeAllViews();			
	}

}
