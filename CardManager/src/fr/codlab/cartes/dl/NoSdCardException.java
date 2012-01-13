package fr.codlab.cartes.dl;

public class NoSdCardException extends Exception {
	@Override
	public String getMessage(){
		return "No SD Card found ! /sdcard/";
	}
}
