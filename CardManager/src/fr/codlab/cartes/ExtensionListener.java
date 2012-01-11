package fr.codlab.cartes;

public interface ExtensionListener {
	void updateNom(String nom);
	void updateTotal(int progression,int count);
	void updatePossedees(int total);
	void miseAjour(int id);

}
