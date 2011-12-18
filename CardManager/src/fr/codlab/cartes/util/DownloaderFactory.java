package fr.codlab.cartes.util;

import java.util.Random;

import android.app.Activity;

final public class DownloaderFactory {
	private static Random _rand = new Random();
	
	public static Downloader downloadFR(Activity parent, String intitule){
		return new Downloader(parent, "http://"+((_rand.nextInt(30)<=10) ? "www.pkmndb.net":"94.125.160.65:8080" )+"/images/"+intitule+".zip");
	}
	
	public static Downloader downloadUS(Activity parent, String intitule){
		return new Downloader(parent, "http://"+((_rand.nextInt(30)<=10) ? "www.pkmndb.net":"94.125.160.65:8080" )+"/images/"+intitule+"_us.zip");
	}
}
