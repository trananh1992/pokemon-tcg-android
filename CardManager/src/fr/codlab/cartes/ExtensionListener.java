package fr.codlab.cartes;

import android.os.Bundle;

public interface ExtensionListener {
	void updateNom(String nom);
	void updateTotal(int progression,int count);
	void updatePossedees(int total);
	void miseAjour(int id);
	public void onClick(Bundle pack);

}
