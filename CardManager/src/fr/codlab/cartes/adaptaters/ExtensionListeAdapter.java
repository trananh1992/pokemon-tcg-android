package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.Carte;
import fr.codlab.cartes.Extension;
import fr.codlab.cartes.ImageCarte;
import fr.codlab.cartes.Principal;
import fr.codlab.cartes.R;
import fr.codlab.cartes.VisuExtension;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class ExtensionListeAdapter extends BaseAdapter {
	private Context context;
	private VisuExtension _principal;
	private Extension _item;
	public ImageCarte _vueClic=null;
	public View v2=null;
	private SharedPreferences _shared = null;
	private int _mode;
	
	public ExtensionListeAdapter(VisuExtension a,Extension item) {
		_principal=a;
		context=a;
		_item=item;
		_shared = a.getPreferences(Activity.MODE_PRIVATE);
		_mode = _shared.getInt(Principal.USE, Principal.FR);
	}

	private Bundle createBundle(int _pos,boolean imgVue){
		Bundle objetbundle = new Bundle();
		objetbundle.putInt("id", _item.getCarte(_pos).getCarteId());
		//objetbundle.putInt("nb", _item.getCarte(_pos).getNb());
		if(imgVue)
			objetbundle.putInt("next", 1);
		objetbundle.putInt("extension", _item.getId());
		objetbundle.putBoolean("visible", _item.getCarte(_pos).getVisible());
		objetbundle.putString("description", _item.getCarte(_pos).getDescription());
		objetbundle.putString("intitule", _item.getIntitule());
		objetbundle.putString("nom", _item.getCarte(_pos).getNom());
		objetbundle.putString("drawable", _item.getCarte(_pos).getDrawableString());
		objetbundle.putString("resistances", _item.getCarte(_pos).getResistance());
		objetbundle.putString("faiblesses", _item.getCarte(_pos).getFaiblesse());
		objetbundle.putString("numero", _item.getCarte(_pos).getNumero());
		objetbundle.putInt("retraite", _item.getCarte(_pos).getRetraite());
		objetbundle.putInt("pv", _item.getCarte(_pos).getPV());
		objetbundle.putString("rarete", _item.getCarte(_pos).getDrawableRarete());
		if(_item.getCarte(_pos).getPokePower()!=null)
			objetbundle.putSerializable("pokepower",
					_item.getCarte(_pos).getPokePower());
		if(_item.getCarte(_pos).getPokeBody()!=null)
			objetbundle.putSerializable("pokebody",
					_item.getCarte(_pos).getPokeBody());

		if(_item.getCarte(_pos).getNbAttaques()>=1 && _item.getCarte(_pos).getAttaque(0)!=null){
			objetbundle.putSerializable("attaque1",
					_item.getCarte(_pos).getAttaque(0));
		}
		if(_item.getCarte(_pos).getNbAttaques()>1 && _item.getCarte(_pos).getAttaque(1)!=null){
			objetbundle.putSerializable("attaque2",
					_item.getCarte(_pos).getAttaque(1));
		}
		return objetbundle;
	}

	public View getView(final int pos, View inView, ViewGroup parent) {
		if(inView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inView = inflater.inflate(R.layout.image_list, null);
		}
		final View v = inView;
		try{
			ImageCarte iv = (ImageCarte) v.findViewById(R.id.image);
			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_item.getIntitule()+"_"+_item.getCarte(pos).getCarteId()+( _mode == Principal.FR ? "" : "_us")+".jpg");
			if(_bmp != null)
				iv.setImageBitmap(_bmp);
			else
				iv.setImageResource(R.drawable.back);
		}
		catch(Exception e)
		{
		}

		try{
			if(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _principal.getPackageName())>0){
				String [] _types = _item.getCarte(pos).getTypes();
				LinearLayout _vue_types = (LinearLayout)v.findViewById(R.id.vue_types);
				_vue_types.removeAllViews();


				try{
					if(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _principal.getPackageName())>0){
						ImageView iv = new ImageView(_principal);
						iv.setImageResource(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _principal.getPackageName()));
						_vue_types.addView(iv);
					}
				}
				catch(Exception e)
				{
				}

				for(int i=0;i<_types.length;i++){
					ImageView _type = new ImageView(_principal);
					_type.setImageResource(v.getResources().getIdentifier("type_"+_types[i] , "drawable", _principal.getPackageName()));
					_vue_types.addView(_type);
				}
			}
		}
		catch(Exception e)
		{
		}
		final int position=pos;
		TextView bTitle = (TextView) v.findViewById(R.id.carte);
		bTitle.setText(_item.getCarte(pos).getNom());

		TextView bInfos = (TextView) v.findViewById(R.id.infos);
		bInfos.setText(_item.getCarte(pos).getInfos());

		final View sav=v;

		ImageView moins = (ImageView)v.findViewById(R.id.carte_sub);
		moins.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantiteNormal(-1);
				TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantiteNormal()));
				_item.updatePossedees();
				_principal.updateTotal(_item.getProgression(),_item.getCount());
				_principal.updatePossedees(_item.getPossedees());
				_principal.miseAjour();
			}
		});
		
		moins = (ImageView)v.findViewById(R.id.reversecarte_sub);
		moins.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantiteReverse(-1);
				TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantiteReverse()));
				_item.updatePossedees();
				_principal.updateTotal(_item.getProgression(),_item.getCount());
				_principal.updatePossedees(_item.getPossedees());
				_principal.miseAjour();
			}
		});	 
		
		moins = (ImageView)v.findViewById(R.id.holocarte_sub);
		moins.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantiteHolo(-1);
				TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantiteHolo()));
				_item.updatePossedees();
				_principal.updateTotal(_item.getProgression(),_item.getCount());
				_principal.updatePossedees(_item.getPossedees());
				_principal.miseAjour();
			}
		});
		
		ImageView plus = (ImageView)v.findViewById(R.id.carte_add);
		plus.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantiteNormal(1);
				TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantiteNormal()));
				_item.updatePossedees();
				_principal.updateTotal(_item.getProgression(),_item.getCount());
				_principal.updatePossedees(_item.getPossedees());
				_principal.miseAjour();
			}
		});
		
		plus = (ImageView)v.findViewById(R.id.reversecarte_add);
		plus.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantiteReverse(1);
				TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantiteReverse()));
				_item.updatePossedees();
				_principal.updateTotal(_item.getProgression(),_item.getCount());
				_principal.updatePossedees(_item.getPossedees());
				_principal.miseAjour();
			}
		});
		
		plus = (ImageView)v.findViewById(R.id.holocarte_add);
		plus.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantiteHolo(1);
				TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantiteHolo()));
				_item.updatePossedees();
				_principal.updateTotal(_item.getProgression(),_item.getCount());
				_principal.updatePossedees(_item.getPossedees());
				_principal.miseAjour();
			}
		});
		
		final int _pos=pos;

		ImageCarte iv = (ImageCarte) v.findViewById(R.id.image);
		if(iv!=null)
			iv.setOnClickListener(new OnClickListener(){

				//@Override
				public void onClick(View v) {

					Intent intent = new Intent().setClass(_principal, Carte.class);
					intent.putExtras(createBundle(_pos,true));
					_principal.startActivityForResult(intent,42);
				}

			});
		v.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				Intent intent = new Intent().setClass(_principal, Carte.class);
				intent.putExtras(createBundle(_pos,false));
				_principal.startActivityForResult(intent,42);	
			}	
		});


		TextView editQuantite=(TextView)v.findViewById(R.id.carte_possedees);
		editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantiteNormal()));
		editQuantite=(TextView)v.findViewById(R.id.reversecarte_possedees);
		editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantiteReverse()));
		editQuantite=(TextView)v.findViewById(R.id.holocarte_possedees);
		editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantiteHolo()));
		return(v);
	}

	//@Override
	public int getCount() {
		return _item.getCount();
	}

	//@Override
	public Object getItem(int pos) {
		return _item.getCarte(pos);//_items[arg0];
	}

	//@Override
	public long getItemId(int pos) {
		return pos;//_items[arg0];
	}
}
