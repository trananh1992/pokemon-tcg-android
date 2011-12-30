package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.Carte;
import fr.codlab.cartes.Principal;
import fr.codlab.cartes.R;
import fr.codlab.cartes.VisuExtension;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.views.ImageCarte;
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
		//objetbundle.putInt("nb", _item.getCarte(_pos).getNb());
		objetbundle.putSerializable("card", _item.getCarte(_pos));
		if(imgVue)
			objetbundle.putInt("next", 1);
		objetbundle.putInt("extension", _item.getId());
		objetbundle.putString("intitule", _item.getIntitule());
		
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
				_item.getCarte(position).addQuantite(_principal, -1, "normal");
				TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_principal, "normal")));
				updateAllCompteurs();
			}
		});
		
		moins = (ImageView)v.findViewById(R.id.reversecarte_sub);
		moins.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantite(_principal, -1, "reverse");
				TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_principal, "reverse")));
				updateAllCompteurs();
			}
		});
		
		moins = (ImageView)v.findViewById(R.id.holocarte_sub);
		moins.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantite(_principal, -1, "holo");
				TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_principal, "holo")));
				updateAllCompteurs();
			}
		});
		
		ImageView plus = (ImageView)v.findViewById(R.id.carte_add);
		plus.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantite(_principal, 1, "normal");
				TextView editQuantite=(TextView)sav.findViewById(R.id.carte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_principal, "normal")));
				updateAllCompteurs();
			}
		});
		
		plus = (ImageView)v.findViewById(R.id.reversecarte_add);
		plus.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantite(_principal, 1, "reverse");
				TextView editQuantite=(TextView)sav.findViewById(R.id.reversecarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_principal, "reverse")));
				updateAllCompteurs();
			}
		});
		
		plus = (ImageView)v.findViewById(R.id.holocarte_add);
		plus.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_item.getCarte(position).addQuantite(_principal, 1, "holo");
				TextView editQuantite=(TextView)sav.findViewById(R.id.holocarte_possedees);
				editQuantite.setText(Integer.toString(_item.getCarte(position).getQuantite(_principal, "holo")));
				updateAllCompteurs();
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
		editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_principal, "normal")));
		editQuantite=(TextView)v.findViewById(R.id.reversecarte_possedees);
		editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_principal, "reverse")));
		editQuantite=(TextView)v.findViewById(R.id.holocarte_possedees);
		editQuantite.setText(Integer.toString(_item.getCarte(pos).getQuantite(_principal, "holo")));
		return(v);
	}

	public void updateAllCompteurs() {
		_item.updatePossedees();
		_principal.updateTotal(_item.getProgression(),_item.getCount());
		_principal.updatePossedees(_item.getPossedees());
		_principal.miseAjour();
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
