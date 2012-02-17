package fr.codlab.cartes.util;

import java.io.Serializable;
import java.util.ArrayList;

import fr.codlab.cartes.attributes.Ability;
import fr.codlab.cartes.attributes.Attack;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.bdd.SGBD;
import android.content.Context;
import android.util.Log;

public class Card implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5371807270118129999L;
	private int _extension;
	private int _idImage;
	private boolean _visible;
	private String _nom=null;
	private int _id_pkmn=0;
	private String _rarete=null;
	private int _carteId=0;
	private String _specialId=null;
	private int _pv;
	private int _quantite_normal;
	private int _quantite_reverse;
	private int _quantite_holo;
	private int _id;
	private String [] _type;
	private ArrayList<Attack> _attaques;
	private PokePower _pokepower;
	private PokeBody _pokebody;
	private Ability _ability;
	private int _retraite;
	private String _faiblesses;
	private String _resistances;
	private String _numero;
	private String _description;
	private boolean _is_normal;
	private boolean _is_holo;
	private boolean _is_reverse;
	
	public Card(int extension){
		_faiblesses="";
		_resistances="";
		_extension=extension;
		_description="";
		_pv=0;
		_quantite_normal=-1;
		_quantite_reverse=-1;
		_quantite_holo=-1;
		_visible=true;
		_retraite=0;
		_rarete="aucune";
		_type = new String[1];
		_type[0]="type";
		_numero="";
		_attaques = new ArrayList<Attack>();
		_is_normal = true;
		_is_holo = false;
		_is_reverse = false;
		_pokepower = null;
		_pokebody = null;
		_ability = null;
	}
	
	public void setVisible(boolean b){
		_visible=b;
	}
	public boolean getVisible(){
		return isTrainer() ? false : _visible ;
	}
	
	public void setRetraite(int i){
		_retraite = i;
	}
	public void addRetraite(int i){
		_retraite+= i;
	}
	public int getRetraite(){
		return _retraite;
	}
	public String getDescription(){
		return _description;
	}
	public void setDescription(String description){
		_description=description;
	}
	public void setPokeBody(PokeBody pokebody){
		_pokebody=pokebody;
	}
	public void setAbility(Ability ability){
		_ability = ability;
	}
	public PokeBody getPokeBody(){
		return _pokebody;
	}
	public void setPokePower(PokePower pokepower){
		_pokepower=pokepower;
	}
	public PokePower getPokePower(){
		return _pokepower;
	}
	public Ability getAbility(){
		return _ability;
	}
	public void addAttaque(Attack at){
		_attaques.add(at);
	}
	public int getNbAttaques(){
		return _attaques.size();
	}
	public Attack getAttaque(int i){
		return _attaques.get(i);
	}
	public void setId(int id){
		_id=id;
		Log.d("setid"," "+_id);
	}
	public int getId(){
		return _id;
	}
	
	public void setPV(int pv){
		_pv=pv;
	}
	public int getPV(){
		return _pv;
	}
	
	public void setNumero(String numero){
		_numero=numero;
	}
	public String getNumero(){
		return (_numero==null || _numero.length()==0)?"#"+Integer.toString(_carteId):_numero;
	}
	
	public void setType(String t){
		if(t.split(",").length>0){
			_type = null;
			_type = t.split(",");
			configureTrainer();
		}
	}
	private void configureTrainer(){
		for(int i=0;i<_type.length;i++){
			if("supporter".equals(_type[i]) ||
					"dresseur".equals(_type[i]) ||
					"trainer".equals(_type[i]) ||
					"stadium".equals(_type[i]) ||
					"stade".equals(_type[i]))
				this._visible = false;
		}
	}
	private boolean isTrainer(){
		for(int i=0;_type != null && i<_type.length;i++){
			Log.d("type", _type[i]);
			if("supporter".equals(_type[i]) ||
					"dresseur".equals(_type[i]) ||
					"trainer".equals(_type[i]) ||
					"stadium".equals(_type[i]) ||
					"stade".equals(_type[i]))
				return true;
		}
		return false;
	}
	public void addFaiblesse(String t){
		_faiblesses=((_faiblesses.length()>0)?",":"")+
			t;
	}
	public void addResistance(String t){
		_resistances=((_resistances.length()>0)?",":"")+
			t;
	}
	
	public String [] getResistances(){
		return _resistances.split(",");
	}
	public String getResistance(){
		return _resistances;
	}
	public String [] getFaiblesses(){
		return _faiblesses.split(",");
	}
	public String getFaiblesse(){
		return _faiblesses;
	}
	public String [] getTypes(){
		return _type;
	}
	
	public void setSpecialId(String spid){
		_specialId=spid;
	}
	public void setCarteId(int id){
		_carteId=id;
	}
	public int getCarteIdInt(){
		return _carteId;
	}
	public String getCarteId(){
		return _specialId == null || _specialId.length() == 0 ? 
				Integer.toString(_carteId) : _specialId;
	}
	
	/**
	 * 
	 * @param principal
	 * @param p
	 * @param rarete
	 */
	public void addQuantite(Context principal, int p, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			_quantite_normal+=p;
			if(_quantite_normal<0)
				_quantite_normal=getQuantite(principal, Rarity.NORMAL);
			SGBD dd=new SGBD(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, _quantite_normal);
			dd.close();
		}
		if(rarete == Rarity.REVERSE){
			_quantite_reverse+=p;
			if(_quantite_reverse<0)
				_quantite_reverse=getQuantite(principal, Rarity.REVERSE);
			SGBD dd=new SGBD(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, _quantite_reverse);
			dd.close();
		}
		if(rarete == Rarity.HOLO){
			_quantite_holo+=p;
			if(_quantite_holo<0)
				_quantite_holo=getQuantite(principal, Rarity.HOLO);
			SGBD dd=new SGBD(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, _quantite_holo);
			dd.close();
		}
	}
	
	public int getQuantite(Context principal, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			if(_quantite_normal==-1){
				SGBD dd=new SGBD(principal);
				dd.open();
				_quantite_normal = dd.getPossessionCarteExtensionNormal(_extension, _carteId);
				dd.close();
			}
			return _quantite_normal;
		}
		if(rarete == Rarity.REVERSE){
			if(_quantite_reverse==-1){
				SGBD dd=new SGBD(principal);
				dd.open();
				_quantite_reverse = dd.getPossessionCarteExtensionReverse(_extension, _carteId);
				dd.close();
			}
			return _quantite_reverse;
		}
		if(rarete == Rarity.HOLO){
			if(_quantite_holo==-1){
				SGBD dd=new SGBD(principal);
				dd.open();
				_quantite_holo = dd.getPossessionCarteExtensionHolo(_extension, _carteId);
				dd.close();
			}
			return _quantite_holo;
		}
		return _quantite_reverse;
	}
	
	public void setRarete(String rarete){
		_rarete=rarete;
	}
	public void setIdPkmn(int id_pkmn){
		_id_pkmn=id_pkmn;
	}
	public int getIdPkmn(){
		return _id_pkmn;
	}
	public void setNom(String nom){
		_nom=nom;
	}

	public String getDrawableString(){
		return "img"+Integer.toString(_extension)+"_"+Integer.toString(_carteId);
	}
	
	public String getDrawableRarete(){
		return "rarete_"+_rarete;
	}
	
	public int getDrawableImage(){
		return _idImage;
	}
	
	public String getNom(){
		return _nom == null && _id_pkmn > 0 && Pokemon.valid(_id_pkmn) ? Pokemon.getName(_id_pkmn): (_nom != null ? _nom : "");
	}

	public String getInfos(){
		return getNumero();
	}
	
	public void setNormal(){
		_is_normal = true;
	}
	public boolean getIsNormal(){
		return _is_normal || true;
	}
	
	public void setHolo(){
		_is_holo = true;
	}

	public boolean getIsHolo(){
		//TODO implement reverse="true"
		return _is_holo || true;
	}
	
	public void setReverse(){
		_is_reverse = true;
	}
	public boolean getIsReverse(){
		//TODO implement reverse="true"
		return _is_reverse || true;
	}
}
