package modele.plateau;

/**
 *
 * @author lenymetzger
 */
public class Colonne extends EntiteDynamique {
    
    /**
     * Constructeur de Colonne 
     * @param _jeu
     */
    public Colonne(Jeu _jeu) { 
        super(_jeu); 
    }

    public boolean peutEtreEcrase() { return false; }

    public boolean peutServirDeSupport() { return true; }

    public boolean peutPermettreDeMonterDescendre() { return false; };

    public boolean peutEtreRamasse() {return false;};
}
