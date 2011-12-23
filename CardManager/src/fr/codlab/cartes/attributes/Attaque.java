package fr.codlab.cartes.attributes;

import java.io.Serializable;


public class Attaque implements Serializable{
	/**
	 * Auto Generated
	 */
	private static final long serialVersionUID = -5023627505698314231L;
	/**
	 * Le nom de l'attaque
	 */
	private String _nom;
	/**
	 * les types sŽparŽs par des virgules
	 */
	private String _types;
	/**
	 * La description textuelle de l'attaque
	 */
	private String _description;
	/**
	 * Les dŽg‰ts de l'attaque. Par exemple +30, x50, etc...
	 */
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
