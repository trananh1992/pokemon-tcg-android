package fr.codlab.cartes;



import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.VisuCartePagerAdapter;
import fr.codlab.cartes.attributes.Attaque;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
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


public class Carte extends Activity {
	private int _id;
	private int _extension;
	private String _intitule;
	private String _drawable;
	private String _rarete;
	private int _img;
	private String _nom;
	private String _numero;
	private Attaque attaque1=null;
	private Attaque attaque2=null;
	private PokePower _pokepower=null;
	private PokeBody _pokebody=null;
	private int _retraite = 0;
	private int _pv=0;
	private String [] _faiblesses;
	private String [] _resistances;
	private String _description;
	private boolean showNext = false;
	private boolean visibleAll=true;	

	public Carte(){

	}

	private void onCreateDefault(){
		_id=0;
		_extension=0;
		_intitule = null;
		_drawable = null;
		_rarete = null;
		_img=0;
		_nom = null;
		_numero = null;
		attaque1=null;
		attaque2=null;
		_pokepower=null;
		_pokebody=null;
		_retraite = 0;
		_pv=0;
		_faiblesses = null;
		_resistances = null;
		_description = null;

		showNext = false;
		visibleAll=true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		onCreateDefault();

		Bundle objetbunble  = this.getIntent().getExtras();
		if (objetbunble != null && objetbunble.containsKey("description")) {
			_description = this.getIntent().getStringExtra("description");
		}
		if (objetbunble != null && objetbunble.containsKey("visible")) {
			visibleAll = this.getIntent().getBooleanExtra("visible",true);
		}
		if (objetbunble != null && objetbunble.containsKey("numero")) {
			_numero = this.getIntent().getStringExtra("numero");
		}
		if (objetbunble != null && objetbunble.containsKey("intitule")) {
			_intitule = this.getIntent().getStringExtra("intitule");
		}
		if (objetbunble != null && objetbunble.containsKey("attaque1")) {
			attaque1 = (Attaque) this.getIntent().getSerializableExtra("attaque1");
		}
		if (objetbunble != null && objetbunble.containsKey("faiblesses")) {
			_faiblesses = this.getIntent().getStringExtra("faiblesses").split(",");
		}
		if (objetbunble != null && objetbunble.containsKey("resistances")) {
			_resistances = this.getIntent().getStringExtra("resistances").split(",");
		}
		if (objetbunble != null && objetbunble.containsKey("attaque2")) {
			attaque2 = (Attaque) this.getIntent().getSerializableExtra("attaque2");
		}
		if (objetbunble != null && objetbunble.containsKey("rarete")) {
			_rarete = this.getIntent().getStringExtra("rarete");
		}
		if (objetbunble != null && objetbunble.containsKey("pv")) {
			_pv = this.getIntent().getIntExtra("pv",0);
		}
		if (objetbunble != null && objetbunble.containsKey("retraite")) {
			_retraite = this.getIntent().getIntExtra("retraite",0);
		}
		if (objetbunble != null && objetbunble.containsKey("pokepower")) {
			_pokepower = (PokePower) this.getIntent().getSerializableExtra("pokepower");
		}
		if (objetbunble != null && objetbunble.containsKey("pokebody")) {
			_pokebody = (PokeBody) this.getIntent().getSerializableExtra("pokebody");
		}
		if (objetbunble != null && objetbunble.containsKey("next")) {
			showNext = true;
		}
		if (objetbunble != null && objetbunble.containsKey("drawable")) {
			_drawable = this.getIntent().getStringExtra("drawable");
		}
		if (objetbunble != null && objetbunble.containsKey("id")) {
			_id = this.getIntent().getIntExtra("id",0);
		}
		if (objetbunble != null && objetbunble.containsKey("extension")) {
			_extension = this.getIntent().getIntExtra("extension",0);
		}

		if (objetbunble != null && objetbunble.containsKey("nom")) {
			_nom = this.getIntent().getStringExtra("nom");
		}


		this.setContentView(R.layout.visucarte);

		//populateImage(this);
		//populateText(this);

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

	void populateImage(Activity activity){
		if(_intitule != null){
			ImageView iv = (ImageView) findViewById(R.carte.visu);
			SharedPreferences _shared = getSharedPreferences(Principal.PREFS, Carte.MODE_PRIVATE);
			int _mode = _shared.getInt(Principal.USE, Principal.FR);

			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_intitule+"_"+_id+(_mode == Principal.FR ? "" : "_us" )+".jpg");
			if(_bmp != null)
				iv.setImageBitmap(_bmp);
			else
				iv.setImageResource(R.drawable.back);
		}
	}
	public void populateImage(View activity){
		if(_intitule != null){
			ImageView iv = (ImageView)activity.findViewById(R.carte.visu);
			SharedPreferences _shared = getSharedPreferences(Principal.PREFS, Carte.MODE_PRIVATE);
			int _mode = _shared.getInt(Principal.USE, Principal.FR);

			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_intitule+"_"+_id+(_mode == Principal.FR ? "" : "_us" )+".jpg");

			if(_bmp != null)
				iv.setImageBitmap(_bmp);
			else
				iv.setImageResource(R.drawable.back);
		}
	}

	void populateText(Activity activity){
		((TextView)activity.findViewById(R.id.carte_description)).setText(_description);
		((TextView)activity.findViewById(R.id.carte_numero)).setText(_numero);
		((TextView)activity.findViewById(R.id.carte_nom)).setText(_nom);

		if(visibleAll && _pv>0)
			((TextView)activity.findViewById(R.id.carte_pv)).setText(Integer.toString(_pv));

		if(getResources().getIdentifier(_rarete , "drawable", this.getPackageName())>0){
			ImageView iv = (ImageView) activity.findViewById(R.id.carte_rarete);
			iv.setImageResource(getResources().getIdentifier(_rarete, "drawable", this.getPackageName()));
		}

		LinearLayout attaques = (LinearLayout)activity.findViewById(R.id.carte_attaques);
		attaques.removeAllViews();
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(visibleAll && attaque1!=null){
			View attaqueView1 = inflater.inflate(R.layout.infos_attaque, null);
			attaques.addView(attaqueView1);
			((TextView)attaqueView1.findViewById(R.id.nom_attaque)).setText(attaque1.getNom());
			((TextView)attaqueView1.findViewById(R.id.attaque_degat)).setText(""+attaque1.getDegats());
			((TextView)attaqueView1.findViewById(R.id.description)).setText(attaque1.getDescription());
			String [] _types = attaque1.getTypes();
			LinearLayout _vue_types = (LinearLayout)attaqueView1.findViewById(R.id.type);
			_vue_types.removeAllViews();
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_types[i] , "drawable", this.getPackageName()));
				_vue_types.addView(_type);
			}
		}

		if(_faiblesses!=null){
			LinearLayout _vue_faiblesses = (LinearLayout)activity.findViewById(R.id.carte_faiblesse);
			if(!visibleAll)
				_vue_faiblesses.removeAllViews();
			for(int i=0;i<_faiblesses.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_faiblesses[i] , "drawable", this.getPackageName()));
				_vue_faiblesses.addView(_type);
			}
		}

		if(_resistances!=null){
			LinearLayout _vue_resistances = (LinearLayout)activity.findViewById(R.id.carte_resistance);
			if(!visibleAll)
				_vue_resistances.removeAllViews();
			for(int i=0;i<_resistances.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_resistances[i] , "drawable", this.getPackageName()));
				_vue_resistances.addView(_type);
			}
		}
		if(visibleAll && attaque2!=null){
			View attaqueView2 = inflater.inflate(R.layout.infos_attaque, null);
			attaques.addView(attaqueView2);
			((TextView)attaqueView2.findViewById(R.id.nom_attaque)).setText(attaque2.getNom());
			((TextView)attaqueView2.findViewById(R.id.description)).setText(attaque2.getDescription());
			((TextView)attaqueView2.findViewById(R.id.attaque_degat)).setText(attaque2.getDegats());
			String [] _types = attaque2.getTypes();
			LinearLayout _vue_types = (LinearLayout)attaqueView2.findViewById(R.id.type);
			_vue_types.removeAllViews();
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_types[i] , "drawable", this.getPackageName()));
				_vue_types.addView(_type);
			}
		}
		LinearLayout pouvoirs = (LinearLayout)activity.findViewById(R.id.carte_pokebody_pokepower);
		pouvoirs.removeAllViews();
		//inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(visibleAll && _pokepower!=null){
			View pouvoir = inflater.inflate(R.layout.pokepower, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokepower_nom)).setText(_pokepower.getNom());
			((TextView)pouvoir.findViewById(R.id.pokepower_description)).setText(_pokepower.getDescription());
		}
		if(visibleAll && _pokebody!=null){
			View pouvoir = inflater.inflate(R.layout.pokebody, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokebody_nom)).setText(_pokebody.getNom());
			((TextView)pouvoir.findViewById(R.id.pokebody_description)).setText(_pokebody.getDescription());
		}


		LinearLayout _vue_retraite = (LinearLayout)activity.findViewById(R.id.carte_retraite);
		for(int i=0;i<_retraite;i++){
			ImageView _type = new ImageView(this);
			_type.setImageResource(this.getResources().getIdentifier("type_incolore" , "drawable", this.getPackageName()));
			_vue_retraite.addView(_type);
		}
		if(!visibleAll)
			_vue_retraite.removeAllViews();		
	}
	public void populateText(View activity){
		((TextView)activity.findViewById(R.id.carte_description)).setText(_description);
		((TextView)activity.findViewById(R.id.carte_numero)).setText(_numero);
		((TextView)activity.findViewById(R.id.carte_nom)).setText(_nom);

		if(visibleAll && _pv>0)
			((TextView)activity.findViewById(R.id.carte_pv)).setText(Integer.toString(_pv));

		if(getResources().getIdentifier(_rarete , "drawable", this.getPackageName())>0){
			ImageView iv = (ImageView) activity.findViewById(R.id.carte_rarete);
			iv.setImageResource(getResources().getIdentifier(_rarete, "drawable", this.getPackageName()));
		}

		LinearLayout attaques = (LinearLayout)activity.findViewById(R.id.carte_attaques);
		attaques.removeAllViews();
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(visibleAll && attaque1!=null){
			View attaqueView1 = inflater.inflate(R.layout.infos_attaque, null);
			attaques.addView(attaqueView1);
			((TextView)attaqueView1.findViewById(R.id.nom_attaque)).setText(attaque1.getNom());
			((TextView)attaqueView1.findViewById(R.id.attaque_degat)).setText(""+attaque1.getDegats());
			((TextView)attaqueView1.findViewById(R.id.description)).setText(attaque1.getDescription());
			String [] _types = attaque1.getTypes();
			LinearLayout _vue_types = (LinearLayout)attaqueView1.findViewById(R.id.type);
			_vue_types.removeAllViews();
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_types[i] , "drawable", this.getPackageName()));
				_vue_types.addView(_type);
			}
		}

		if(_faiblesses!=null){
			LinearLayout _vue_faiblesses = (LinearLayout)activity.findViewById(R.id.carte_faiblesse);
			if(!visibleAll)
				_vue_faiblesses.removeAllViews();
			for(int i=0;i<_faiblesses.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_faiblesses[i] , "drawable", this.getPackageName()));
				_vue_faiblesses.addView(_type);
			}
		}

		if(_resistances!=null){
			LinearLayout _vue_resistances = (LinearLayout)activity.findViewById(R.id.carte_resistance);
			if(!visibleAll)
				_vue_resistances.removeAllViews();
			for(int i=0;i<_resistances.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_resistances[i] , "drawable", this.getPackageName()));
				_vue_resistances.addView(_type);
			}
		}
		if(visibleAll && attaque2!=null){
			View attaqueView2 = inflater.inflate(R.layout.infos_attaque, null);
			attaques.addView(attaqueView2);
			((TextView)attaqueView2.findViewById(R.id.nom_attaque)).setText(attaque2.getNom());
			((TextView)attaqueView2.findViewById(R.id.description)).setText(attaque2.getDescription());
			((TextView)attaqueView2.findViewById(R.id.attaque_degat)).setText(attaque2.getDegats());
			String [] _types = attaque2.getTypes();
			LinearLayout _vue_types = (LinearLayout)attaqueView2.findViewById(R.id.type);
			_vue_types.removeAllViews();
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(this);
				_type.setImageResource(getResources().getIdentifier("type_"+_types[i] , "drawable", this.getPackageName()));
				_vue_types.addView(_type);
			}
		}
		LinearLayout pouvoirs = (LinearLayout)activity.findViewById(R.id.carte_pokebody_pokepower);
		pouvoirs.removeAllViews();
		//inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(visibleAll && _pokepower!=null){
			View pouvoir = inflater.inflate(R.layout.pokepower, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokepower_nom)).setText(_pokepower.getNom());
			((TextView)pouvoir.findViewById(R.id.pokepower_description)).setText(_pokepower.getDescription());
		}
		if(visibleAll && _pokebody!=null){
			View pouvoir = inflater.inflate(R.layout.pokebody, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokebody_nom)).setText(_pokebody.getNom());
			((TextView)pouvoir.findViewById(R.id.pokebody_description)).setText(_pokebody.getDescription());
		}


		LinearLayout _vue_retraite = (LinearLayout)activity.findViewById(R.id.carte_retraite);
		for(int i=0;i<_retraite;i++){
			ImageView _type = new ImageView(this);
			_type.setImageResource(this.getResources().getIdentifier("type_incolore" , "drawable", this.getPackageName()));
			_vue_retraite.addView(_type);
		}
		if(!visibleAll)
			_vue_retraite.removeAllViews();		
	}

}
