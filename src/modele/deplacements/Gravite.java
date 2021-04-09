package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 *
 * @author lenymetzger
 */
public class Gravite extends RealisateurDeDeplacement {
    
    private static Gravite grav;

    /**
     *
     * @return une instance de Gravite
     */
    public static Gravite getInstance() {
        if (grav == null) {
            grav = new Gravite();
        }
        return grav;
    }
    
    /**
     * Réalise le déplacement d'une instance de Gravite
     * @return true si le déplacement à pu se faire, false sinon
     */
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            if (eBas == null || (!eBas.peutServirDeSupport() && !eBas.peutPermettreDeMonterDescendre())) {
                if (e.avancerDirectionChoisie(Direction.bas))
                    ret = true;
            }
        }
        return ret;
    }
    
}
