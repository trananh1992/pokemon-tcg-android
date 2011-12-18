package fr.codlab.cartes;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.PrincipalExtensionAdapter;
import fr.codlab.cartes.util.Downloader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
	private static final int SWIPE_MIN_DISTANCE = 9;
	private static final int SWIPE_MAX_OFF_PATH = 999;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	//private static final int SWIPE_THRESHOLD_VELOCITY_TWICE = 1500;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;
	private GestureDetector mGestureDetector;
	public static final int MAX=50;
	private ArrayList<Extension> _arrayExtension;
	private static Downloader _downloader;

	/** Called when the activity is first created. */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent i){
		super.onActivityResult(requestCode, resultCode, i);
		try{
			if(i!=null){
				Bundle bd = i.getExtras();
				int miseAjour = bd.getInt("update",0);
				if( miseAjour >= 0){
					boolean trouve = false;
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

		viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

		mGestureDetector = new GestureDetector(mScrollHandler);

		Button _switch = (Button)findViewById(R.main.flip);
		_switch.setOnClickListener(new OnClickListener(){

			//@Override
			public void onClick(View v) {
				if(viewFlipper != null)
					viewFlipper.showNext();
			}

		});
		XmlPullParser parser = getResources().getXml(R.xml.extensions);
		//StringBuilder stringBuilder = new StringBuilder();
		//  <extension nom="Base" nb="1">
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrincipalExtensionAdapter _adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
		ListView _list = (ListView)findViewById(R.id.principal_extensions);
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
	
	public boolean onOptionsItemSelected(MenuItem item) {
		//On regarde quel item a été cliqué grâce à son id et on déclenche une action
		SharedPreferences _shared = null;
		
		switch (item.getItemId()) {
		case R.principal.useus:
			_shared = this.getSharedPreferences(Principal.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(Principal.USE, Principal.US).commit();
			return true;
		case R.principal.usefr:
			_shared = this.getSharedPreferences(Principal.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(Principal.USE, Principal.FR).commit();
			return true;
			//downloadImages();
			//_downloader = new Downloader(this, "http://www.pkmndb.net/images.zip");
		default:
			return false;
		}
	}

	/*public void setNomExtensionAtIndex(int idExtension,String nomval){
        TextView _cartesNom = (TextView) this.findViewById(this.extensionsNom[idExtension-1]);
    	_cartesNom.setText(nomval);
    }

    public void setNbCartesExtensionAtIndex(int idExtension,int nomval, int progression){
    	TextView _cartesGauche = (TextView) this.findViewById(this.extensionsCartes[idExtension-1]);
    	_cartesGauche.setText("Nb. Cartes : "+Integer.toString(progression)+"/"+Integer.toString(nomval));
    }

    public void setNbPossedeesExtensionAtIndex(int idExtension,int nomval){
    	TextView _cartesDroite = (TextView) this.findViewById(this.extensionsPossess[idExtension-1]);
    	_cartesDroite.setText("Possedees : "+Integer.toString(nomval));
    }*/

	private GestureDetector.SimpleOnGestureListener mScrollHandler = new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onDown(MotionEvent arg0) {
			// Don't forget to return true here to get the following touch events
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
				} 
			} catch (Exception e) {
				// nothing
			}
			return true;    	}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}





}