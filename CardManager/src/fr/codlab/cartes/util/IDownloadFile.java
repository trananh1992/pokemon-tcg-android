package fr.codlab.cartes.util;

interface IDownloadFile {
	public void receiveProgress(String msg, Double args);
	
	public void onPost(Long result);
}
