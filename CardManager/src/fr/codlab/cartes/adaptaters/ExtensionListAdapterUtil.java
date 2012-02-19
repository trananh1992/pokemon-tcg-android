package fr.codlab.cartes.adaptaters;

import fr.codlab.cartes.IExtensionListener;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.Rarity;
import fr.codlab.cartes.views.CardImage;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExtensionListAdapterUtil {
	private Extension _item;
	private Context _context;
	private IExtensionListener _principal;
	private int _mode;
	ExtensionListAdapterUtil(IExtensionListener principal, Extension item, Context context){
		_principal = principal;
		_item = item;
		_context = context;
		SharedPreferences _shared = context.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
		_mode = _shared.getInt(MainActivity.USE, MainActivity.FR);
	}

	public Bundle createBundle(int _pos,boolean imgVue){
		Bundle objetbundle = new Bundle();
		//objetbundle.putInt("nb", _item.getCarte(_pos).getNb());
		objetbundle.putSerializable("card", _item.getCarte(_pos));
		if(imgVue)
			objetbundle.putInt("next", 1);
		objetbundle.putInt("extension", _item.getId());
		objetbundle.putString("intitule", _item.getShortName());

		return objetbundle;
	}

	public void updateAll() {
		_item.updatePossessed();
		_principal.updateProgress(_item.getProgress(),_item.getCount());
		_principal.updatePossessed(_item.getPossessed());
		_principal.updated(_item.getId());
	}
	public View populate(final View v, final int pos){
		try{
			CardImage iv = (CardImage) v.findViewById(R.id.image);
			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_item.getShortName()+"_"+_item.getCarte(pos).getCarteId()+( _mode == MainActivity.FR ? "" : "_us")+".jpg");
			if(_bmp != null)
				iv.setImageBitmap(_bmp);
			else
				iv.setImageResource(R.drawable.back);
		}
		catch(Exception e)
		{
		}

		try{
			if(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _context.getPackageName())>0){
				String [] _types = _item.getCarte(pos).getTypes();
				LinearLayout _vue_types = (LinearLayout)v.findViewById(R.id.vue_types);
				_vue_types.removeAllViews();


				try{
					if(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _context.getPackageName())>0){
						ImageView iv = new ImageView(_context);
						iv.setImageResource(v.getResources().getIdentifier(_item.getCarte(pos).getDrawableRarete() , "drawable", _context.getPackageName()));
						_vue_types.addView(iv);
					}
				}
				catch(Exception e)
				{
				}

				for(int i=0;i<_types.length;i++){
					ImageView _type = new ImageView(_context);
					_type.setImageResource(v.getResources().getIdentifier("type_"+_types[i] , "drawable", _context.getPackageName()));
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

		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)v.findViewById(R.id.carte_sub);
		plus = (ImageView)v.findViewById(R.id.carte_add);
		editQuantite=(TextView)v.findViewById(R.id.carte_possedees);
		LinearLayout normal = (LinearLayout)v.findViewById(R.extension.normal);
		LinearLayout reverse = (LinearLayout)v.findViewById(R.extension.reverse);
		LinearLayout holo = (LinearLayout)v.findViewById(R.extension.holo);

		if(_item.getCarte(position).getIsNormal()){
			normal.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_context, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, -1, Rarity.NORMAL);
					TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, 1, Rarity.NORMAL);
					TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			normal.setVisibility(View.GONE);
		}

		moins = (ImageView)v.findViewById(R.id.reversecarte_sub);
		plus = (ImageView)v.findViewById(R.id.reversecarte_add);
		editQuantite=(TextView)v.findViewById(R.id.reversecarte_possedees);

		//REVERSE OR FIRST EDITION !
		if(_item.getCarte(position).getIsReverse() || _item.isFirstEdition()){
			if(_item.isFirstEdition()){
				TextView v2 = (TextView)v.findViewById(R.extension.reversetext);
				v2.setText(R.string.firstedition);
			}
			reverse.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_context, Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, -1, Rarity.REVERSE);
					TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, 1, Rarity.REVERSE);
					TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			reverse.setVisibility(View.GONE);
		}

		moins = (ImageView)v.findViewById(R.id.holocarte_sub);
		plus = (ImageView)v.findViewById(R.id.holocarte_add);
		editQuantite=(TextView)v.findViewById(R.id.holocarte_possedees);
		if(_item.getCarte(position).getIsHolo()){
			holo.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_context, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, -1, Rarity.HOLO);
					TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_item.getCarte(position).addQuantite(_context, 1, Rarity.HOLO);
					TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
					editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_context, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			holo.setVisibility(View.GONE);
		}
		final int _pos=pos;

		CardImage iv = (CardImage) v.findViewById(R.id.image);
		if(iv!=null)
			iv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {

					_principal.onClick(createBundle(_pos,true));
				}

			});
		v.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				_principal.onClick(createBundle(_pos,false));
			}	
		});
		return v;
	}

	public int getCount() {
		return _item.getCount();
	}

	public Object getItem(int pos) {
		return _item.getCarte(pos);
	}

	public long getItemId(int pos) {
		return _item.getCarte(pos).getId();
	}
}
