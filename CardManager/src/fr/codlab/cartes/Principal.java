package fr.codlab.cartes;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.MainPagerAdapter;
import fr.codlab.cartes.adaptaters.PrincipalExtensionAdapter;
import fr.codlab.cartes.adaptaters.VisuCartePagerAdapter;
import fr.codlab.cartes.util.Downloader;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;

public class Principal extends Activity{
	public static final int MAX=60;
	private ArrayList<Extension> _arrayExtension;
	private static Downloader _downloader;

	/** utilisee lorsque l'activite lancŽe retourne un resultat */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent i){
		super.onActivityResult(requestCode, resultCode, i);
		try{
			if(i!=null){
				Bundle bd = i.getExtras();
				//on observe les modifications apportees
				int miseAjour = bd.getInt("update",0);
				if( miseAjour >= 0){
					boolean trouve = false;
					//on recharge les informations concernant les listes
					// >> vues & possedees
					for(int ind=0;!trouve && ind<_arrayExtension.size();ind++){
						if(_arrayExtension.get(ind).getId()==miseAjour){
							_arrayExtension.get(ind).updatePossedees();

							PrincipalExtensionAdapter _adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
							ListView _list = (ListView)findViewById(R.id.principal_extensions);
							_list.setAdapter(_adapter);
							trouve=true;
						}
					}
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
						//stringBuilder.append(position + ", " + brand + "\n");
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		ViewPager pager = (ViewPager)findViewById( R.id.viewpager );
		if(pager != null){
			MainPagerAdapter adapter = new MainPagerAdapter( this );
			TitlePageIndicator indicator =
					(TitlePageIndicator)findViewById( R.id.indicator );
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);
		}
}

	public void setListExtension(View v){
		PrincipalExtensionAdapter _adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
		ListView _list = (ListView)v.findViewById(R.id.principal_extensions);
		_list.setAdapter(_adapter);
	}
	
	public void setListExtension(Activity v){
		PrincipalExtensionAdapter _adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
		ListView _list = (ListView)v.findViewById(R.id.principal_extensions);
		_list.setAdapter(_adapter);
	}
	
	public void onPause(){
		super.onPause();
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public void onResume(){
		super.onResume();
		if(_downloader != null)
			_downloader.downloadLoad();
	}

	public void onDestroy(){
		super.onDestroy();
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.principalmenu, menu);

		return true;
	}

	public final static String PREFS = "_CODLABCARTES_";
	public final static String USE = "DISPLAY";
	public final static int US=0;
	public final static int FR=1;
	
	//creation du menu de l'application
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences _shared = null;
		
		switch (item.getItemId()) {
		//modification en mode US
		case R.principal.useus:
			_shared = this.getSharedPreferences(Principal.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(Principal.USE, Principal.US).commit();
			return true;
		//modification en mode fr
		case R.principal.usefr:
			_shared = this.getSharedPreferences(Principal.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(Principal.USE, Principal.FR).commit();
			return true;
		default:
			return false;
		}
	}
}