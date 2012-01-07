package fr.codlab.cartes.util;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import fr.codlab.cartes.attributes.Attaque;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.bdd.SGBD;

public class Extension{
	private ArrayList<CartePkmn> _aCartes;
	private int _id;
	private Context _p;
	private int _possedees=0;
	private int _progression=0;
	private String _nom;
	private int _ressources;
	private int _nb;
	private String _intitule;
	
	public Extension(Context _principal, int id, int nb, String intitule, String nom, boolean mustParseXml){
		_nom=nom;
		_id=id;
		_p=_principal;
		_intitule = intitule;
		_aCartes = new ArrayList<CartePkmn>();
		//_ressources=Principal.extensions[_id-1];
		if(_id<10)
			_ressources = _principal.getResources().getIdentifier("e0"+Integer.toString(id) , "xml", "fr.codlab.cartes");
		else
			_ressources = _principal.getResources().getIdentifier("e"+Integer.toString(id) , "xml", "fr.codlab.cartes");
		_nb=nb;
		if(mustParseXml)
			parseXml();
		updatePossedees();
	}
	
	public void parseXml(){
        XmlPullParser parser = _p.getResources().getXml(_ressources);
        
        //StringBuilder stringBuilder = new StringBuilder();
        //<extension nom="Base" nb="1">
        CartePkmn tampon = null;
        Attaque attaque = null;
        PokePower pokepower = null;
        PokeBody pokebody = null;
        
        int eventType=0;
        try {
			eventType = parser.getEventType();
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
    	String tag=null;
    	int _nbCarte=1;
        while (eventType != XmlPullParser.END_DOCUMENT) {
           	if(eventType == XmlPullParser.START_TAG) {
           		tag=parser.getName();
           		if("retraite".equals(parser.getName())){
               		if(parser.getAttributeValue(null, "cout")!=null)
               			tampon.setRetraite(Integer.parseInt(parser.getAttributeValue(null, "cout")));
           		}else if("pokebody".equals(parser.getName())){
           			pokebody = new PokeBody();
           			if(parser.getAttributeValue(null, "nom")!=null)
           				pokebody.setNom(parser.getAttributeValue(null, "nom"));
           		}else if("pokepower".equals(parser.getName())){
           			pokepower = new PokePower();
           			if(parser.getAttributeValue(null, "nom")!=null)
           				pokepower.setNom(parser.getAttributeValue(null, "nom"));
           		}else if("attaque".equals(parser.getName())){
           			attaque = new Attaque();
           			if(parser.getAttributeValue(null, "nom")!=null)
           				attaque.setNom(parser.getAttributeValue(null, "nom"));
           		}else if("carte".equals(parser.getName())){
               		tampon=new CartePkmn(_id);
               		tampon.setId(_nbCarte);
               		if(parser.getAttributeValue(null, "normal") != null && 
               				"true".equals(parser.getAttributeValue(null, "normal")))
               			tampon.setNormal();
               		if(parser.getAttributeValue(null, "holo") != null &&
               				"true".equals(parser.getAttributeValue(null, "holo")))
               			tampon.setNormal();
               		if(parser.getAttributeValue(null, "reverse") != null && 
               				"true".equals(parser.getAttributeValue(null, "reverse")))
               			tampon.setNormal();
           			if(parser.getAttributeValue(null, "spcid")!=null)
           				tampon.setNumero(parser.getAttributeValue(null, "spcid"));
           			if(parser.getAttributeValue(null, "visible")!=null)
           				tampon.setVisible("true".equals(parser.getAttributeValue(null, "visible"))?true:false);
           			if(parser.getAttributeValue(null, "nom")!=null)
           				tampon.setNom(parser.getAttributeValue(null, "nom"));
           			if(parser.getAttributeValue(null, "types")!=null)
           				tampon.setType(parser.getAttributeValue(null, "types"));
           			if(parser.getAttributeValue(null, "nomPkmn")!=null)
           				tampon.setNomPkmn(parser.getAttributeValue(null, "nomPkmn"));
           			if(parser.getAttributeValue(null, "rarete")!=null)
           				tampon.setRarete(parser.getAttributeValue(null, "rarete"));
           			if(parser.getAttributeValue(null, "cid")!=null)
           				try{
           					tampon.setCarteId(Integer.parseInt(parser.getAttributeValue(null, "cid")));
           				}
        				catch(Exception e){
        				}
           		}
           	} else if(eventType == XmlPullParser.END_TAG) {
           		if("pokebody".equals(parser.getName())){
           			tampon.setPokeBody(pokebody);
           			pokebody = null;
           		}else if("pokepower".equals(parser.getName())){
           			tampon.setPokePower(pokepower);
           			pokepower = null;
           		}else if("attaque".equals(parser.getName())){
           			tampon.addAttaque(attaque);
           			attaque = null;
           		}else if("carte".equals(parser.getName())){
           			_aCartes.add(tampon);
           			//Log.d("Add carte ",tampon.getNom());
           			tampon=new CartePkmn(_id);
           		}
           	} else if(eventType == XmlPullParser.TEXT) {
           		if("retraite".equals(parser.getName()) && parser.getText()!=null && parser.getText().length()>0){
               		tampon.setRetraite(Integer.parseInt(parser.getText()));
           		}else if(tampon != null && "resistance".equals(tag)){
           			tampon.addResistance(parser.getText());
           		}else if(tampon != null && "faiblesse".equals(tag)){
           			tampon.addFaiblesse(parser.getText());
           		}else if(pokebody != null && "pokebody".equals(tag)){
           			pokebody.setDescription(parser.getText());
           		}else if(pokepower != null && "pokepower".equals(tag)){
           			pokepower.setDescription(parser.getText());
           		}else if(attaque != null && "degats".equals(tag)){
           			attaque.setDegats(parser.getText());
           		}else if(attaque != null && "description".equals(tag)){
           			attaque.setDescription(parser.getText());
           		}else if("description".equals(tag)){ //description poke
           			tampon.setDescription(parser.getText());
           		}else if(attaque != null && "type".equals(tag)){
           			attaque.addType(parser.getText());
           		}else if("pv".equals(tag)){
           			//Log.d("Add carte ",tampon.getNom());
           			try{
           				tampon.setPV(Integer.parseInt(parser.getText()));
           			}
           			catch(Exception e){
           			}
           		}
           	}
           	try {
				eventType = parser.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	public int getId(){
		return _id;
	}
	
	public String getNom(){
		return _nom;
	}

	public String getIntitule(){
		return _intitule;
	}
	
	public int getCount(){
		return (_aCartes!=null && _aCartes.size()>0)?_aCartes.size():_nb;
	}
	
	public int getPossedees(){
		return _possedees;
	}
	
	public int getProgression(){
		return _progression;
	}
	public boolean updatePossedees(){

		SGBD bdd = new SGBD(_p);
		bdd.open();
		int avant = bdd.getPossessionExtension(_id);
		_progression=bdd.getExtensionProgression(_id);
		bdd.close();
		bdd=null;
		boolean res = avant!=_possedees;
		_possedees=avant;
		//_p.setNbPossedeesExtensionAtIndex(_id, _possedees);
		//_p.setNbCartesExtensionAtIndex(_id, getCount(), _progression);
		return res;
	}
	
	public CartePkmn getCarte(int pos){
		return (pos<_aCartes.size())?_aCartes.get(pos):null;
	}
	
	public int getNb(){
		return _nb;
	}

}
