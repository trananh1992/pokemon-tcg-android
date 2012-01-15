package fr.codlab.cartes;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.MainPagerAdapter;
import fr.codlab.cartes.adaptaters.PrincipalExtensionAdapter;
import fr.codlab.cartes.fragments.CardFragment;
import fr.codlab.cartes.fragments.InformationScreenFragment;
import fr.codlab.cartes.fragments.ExtensionFragment;
import fr.codlab.cartes.fragments.ListViewExtensionFragment;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
/**
 * Classe de dŽmarrage de l'application
 * 
 * utilise un Pager
 * premi�re frame : information textuelle
 * deuxi�me : liste des extensions
 * a venir : troisieme : liste des codes boosters online
 * 
 * @author kevin
 * 
 *
 */
public class MainActivity extends FragmentActivity implements IExtensionMaster{
	public static final int MAX=60;
	private ArrayList<Extension> _arrayExtension;
	public final static String PREFS = "_CODLABCARTES_";
	public final static String USE = "DISPLAY";
	public final static int US=0;
	public final static int FR=1;
	public static int InUse = MainActivity.FR;
	
	/**
	 * The Acitivty receive an intent
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent i){
		try{
			super.onActivityResult(requestCode, resultCode, i);
			if(i!=null){
				Bundle bd = i.getExtras();
				//on observe les modifications apportees
				int miseAjour = bd.getInt("update",0);
				if( miseAjour >= 0){
					update(miseAjour);
				}
			}
		}catch(Exception e)
		{
		}
	} 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		SharedPreferences shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
		if(!shared.contains(MainActivity.USE)){
			if("FR".equals(this.getString(R.string.lang)))
				shared.edit().putInt(MainActivity.USE, MainActivity.FR).commit();
			else
				shared.edit().putInt(MainActivity.USE, MainActivity.US).commit();
		}

		createExtensions();
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		ViewPager pager = (ViewPager)findViewById( R.id.viewpager );
		if(pager != null){
			MainPagerAdapter adapter = new MainPagerAdapter( this );
			TitlePageIndicator indicator =
					(TitlePageIndicator)findViewById( R.id.indicator );
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);
		}else{
			//on est sur tablette
			//donc gestion avec les fragments
			ListViewExtensionFragment viewer = (ListViewExtensionFragment) getSupportFragmentManager().findFragmentById(R.id.liste_extension_fragment);
			viewer.setListExtension(this);

		}
	}

	/**
	 * Call after onResume or onCreate
	 */
	@Override
	public void onStart(){
		super.onStart();
		//on rajoute le fragment si on est sur tablette
		if(findViewById( R.id.liste_extension_fragment ) != null && getSupportFragmentManager().getBackStackEntryCount() == 0){
			FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
			xact.add(R.id.extension_fragment, new InformationScreenFragment());
			xact.commit();
		}
	}
	private void createExtensions(){
		if(_arrayExtension != null)
			return;

		_arrayExtension = new ArrayList<Extension>();

		XmlPullParser parser = getResources().getXml(R.xml.extensions);
		//StringBuilder stringBuilder = new StringBuilder();
		//  <extension nom="Base" nb="1" id="id de l'extension" intitule="tag pour les images" />
		try {
			int id=0;
			int nb = 0;
			String intitule="";
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				intitule = "";
				id=0;
				nb=0;
				String name = parser.getName();
				String extension = null;
				if((name != null) && name.equals("extension")) {
					int size = parser.getAttributeCount();
					for(int i = 0; i < size; i++) {
						String attrNom = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if((attrNom != null) && attrNom.equals("nom")) {
							extension = attrValue;
						} else if ((attrNom != null) && attrNom.equals("id")) {
							try{
								id = Integer.parseInt(attrValue);
							}
							catch(Exception e){
								id=0;
							}
						} else if ((attrNom != null) && attrNom.equals("nb")) {
							try{
								nb = Integer.parseInt(attrValue);
							}
							catch(Exception e){
								nb=0;
							}
						} else if ((attrNom != null) && attrNom.equals("intitule")) {
							intitule = attrValue;
						}
					}

					if((extension != null) && (id > 0 && id<MAX)) {
						_arrayExtension.add(new Extension(this, id, nb, intitule, extension, false));
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	PrincipalExtensionAdapter _adapter;
	public void setListExtension(View v){
		_adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
		ListView _list = (ListView)v.findViewById(R.id.principal_extensions);
		_list.setAdapter(_adapter);
	}
	public void notifyChanged(){
		_adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.principalmenu, menu);
		boolean state = super.onCreateOptionsMenu(menu);

		return state;
	}

	//creation du menu de l'application
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences _shared = null;

		switch (item.getItemId()) {
		//modification en mode US
		case android.R.id.home:
			if(_carte != null || _extension != null){
				FragmentManager fm = getSupportFragmentManager();
				fm.popBackStack();
				if(_carte != null){
					_carte = null;
				}else{
					if(_extension != null){
						_extension = null;
						getSupportActionBar().setDisplayHomeAsUpEnabled(false);
					}
				}
			}
		case R.principal.useus:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.US).commit();
			MainActivity.InUse = MainActivity.US;
			return true;
			//modification en mode fr
		case R.principal.usefr:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.FR).commit();
			MainActivity.InUse = MainActivity.FR;
			return true;
		default:
			return false;
		}
	}

	ExtensionFragment _extension;
	CardFragment _carte;
	String _name;//last extension
	int _id;//last extension
	String _intitule;//last extension

	@Override
	public void onSaveInstanceState(Bundle out){

		boolean ext=false;
		boolean car=false;
		if(_name != null){
			out.putString("NAME", _name);
			out.putInt("ID", _id);
			out.putString("INTIT", _intitule);

			try{
				getSupportFragmentManager().putFragment(out, "EXTENSION", _extension);
			}catch(Exception e){
			}
		}

		if(_carte != null){
			try{
				getSupportFragmentManager().putFragment(out, "CARTE", _carte);
			}catch(Exception e){
			}
		}
		_carte = null;
		_extension = null;

		super.onSaveInstanceState(out);
	}

	@Override
	public void onRestoreInstanceState(Bundle in){
		if(in!= null && in.containsKey("NAME") && in.containsKey("ID") && in.containsKey("INTIT")){
			_name = in.getString("NAME");
			_id = in.getInt("ID");
			_intitule = in.getString("INTIT");
			Fragment frag = getSupportFragmentManager().getFragment(in, "EXTENSION");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		if(in != null && in.containsKey("CARTE")){
			Log.d("on restore","BUNDLE");
			_carte = (CardFragment) getSupportFragmentManager().getFragment(in, "CARTE");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if(in != null)
			super.onRestoreInstanceState(in);

	}
	public void onClick(String nom,
			int id,
			String intitule){
		Fragment viewer = getSupportFragmentManager().findFragmentById(R.id.extension_fragment);
		if(viewer != null){
			_name = nom;
			_id = id;
			_intitule = intitule;
			FragmentManager fm = getSupportFragmentManager();
			if(_carte != null){
				if(_carte.isAdded()){
					FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
					xact.remove(_carte);
					xact.commit();
				}
				_carte = null;
				fm.popBackStackImmediate();
			}
			if(_extension == null  || !_extension.isVisible()){
				//Fragment extension = getSupportFragmentManager().findFragmentByTag(nom);
				FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
				_extension = new ExtensionFragment(this, nom, id, intitule);
				//xact.show(_extension);
				//xact.replace(R.id.extension_fragment, _extension, nom);
				xact.replace(R.id.extension_fragment, _extension,"Extensions");
				xact.addToBackStack(null);
				xact.commit();
			}else{
				_extension.setExtension(nom, id, intitule);
			}
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			Bundle objetbundle = new Bundle();
			objetbundle.putString("nom", nom);
			objetbundle.putInt("extension", id);
			objetbundle.putString("intitule", intitule);
			Intent intent = new Intent().setClass(this, ExtensionActivity.class);
			intent.putExtras(objetbundle);
			startActivityForResult(intent,42);
		}
	}

	public void onClick(Bundle pack) {
		FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(_carte == null || !_carte.isVisible()){
			_carte = new CardFragment(pack);
			//xact.show(_extension);
			//xact.replace(R.id.extension_fragment, _extension, nom);
			xact.add(R.id.extension_fragment, _carte,"Carte");
			xact.addToBackStack(null);
			xact.commit();	
		}
	}

	/**
	 * Receive a notification about modification
	 * so update the fragment >> cause we have it since this function is called
	 */
	@Override
	public void update(int extension_id) {
		int ind = 0;
		for(;ind<_arrayExtension.size() && 
				_arrayExtension.get(ind).getId()!=extension_id;ind++);
		if(ind<_arrayExtension.size()){
			_arrayExtension.get(ind).updatePossessed();
		}
		notifyChanged();
	}

	public void setCarte(CardFragment carte){
		_carte = carte;
	}

	public void setExtension(ExtensionFragment extension){
		_extension = extension;
		_extension.setParent(this);
	}

}