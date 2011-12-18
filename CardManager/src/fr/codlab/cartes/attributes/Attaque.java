package fr.codlab.cartes.attributes;

import java.io.Serializable;


public class Attaque implements Serializable{
	/**
	 * Auto Generated
	 */
	private static final long serialVersionUID = -5023627505698314231L;
	private String _nom;
	private String _types;
	private String _description;
	private String _degats;
	
	public Attaque(){
		_nom="";
		_types="";
		_description="";
		_degats="";
		
	}
	
	public void setNom(String nom){
		_nom=nom;
	}
	public String getNom(){
		return _nom;
	}
	
	public void setTypes(String types){
		_types=types;
	}
	public void addType(String type){
		_types=_types+((_types.length()>0)?",":"")+type;
	}
	public String [] getTypes(){
		return _types.split(",");
	}
	
	public void setDescription(String description){
		_description=description;
	}
	public String getDescription(){
		return _description;
	}
	
	public void setDegats(String degats){
		_degats=degats;
	}
	public String getDegats(){
		return _degats;
	}
}
