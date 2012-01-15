package fr.codlab.cartes;



import fr.codlab.cartes.R;
import fr.codlab.cartes.manageui.CarteUi;
import fr.codlab.cartes.util.Card;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * CLasse de visualisation d'une carte
 * 
 * 
 * @author kevin
 *
 */
public class CardActivity extends FragmentActivity {
	private CarteUi _factorise;
	public CardActivity(){
		_factorise = new CarteUi();
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle objetbunble  = this.getIntent().getExtras();
		
		//chargement de la carte
		if(objetbunble != null && objetbunble.containsKey("card"))
			_factorise.setCard((Card) objetbunble.getSerializable("card"));
		
		//tout visible ? - actuellement toujours vrai
		if (objetbunble != null && objetbunble.containsKey("visible")) {
			_factorise.setAllObjectVisible(this.getIntent().getBooleanExtra("visible",true));
		}
		
		//intitule
		if (objetbunble != null && objetbunble.containsKey("intitule")) {
			_factorise.setSetShortName(this.getIntent().getStringExtra("intitule"));
		}
		
		//affichage du texte ou de l'image? - showNext = true > image
		if (objetbunble != null && objetbunble.containsKey("next")) {
			_factorise.showImageAtFirst(true);
		}

		//mise en forme avec le pager
		this.setContentView(R.layout.visucarte);
		_factorise.setContext(this.findViewById(R.visucarte.top));
		_factorise.manageFirstPopulate();
	}
}
