package fr.codlab.cartes.updater;

import android.content.Context;

public interface IUpdater {
	void notifyWork();
	public byte [] receiveData();
	public void error();
	public void stopWaiter();
	void createWaiter(Context c, String title, String text);
	void okcreate();
	void okauth();
}
