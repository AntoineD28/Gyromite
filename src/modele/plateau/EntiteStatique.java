package modele.plateau;

/**
 * Ne bouge pas (murs...)
 */
public abstract class EntiteStatique extends Entite {

    /**
     * Constructeur d'EntiteStatique
     * @param _jeu
     */
    public EntiteStatique(Jeu _jeu) {
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return false; }

    public boolean peutServirDeSupport() { return true; }

    public boolean peutPermettreDeMonterDescendre() { return false; };

    public boolean peutEtreRamasse() {return false;};
}