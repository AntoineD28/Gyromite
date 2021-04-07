package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class Gravite extends RealisateurDeDeplacement {
    
    private static Gravite grav;

    public static Gravite getInstance() {
        if (grav == null) {
            grav = new Gravite();
        }
        return grav;
    }
    
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            //System.out.println(eBas);
            if (eBas == null || (!eBas.peutServirDeSupport() && !eBas.peutPermettreDeMonterDescendre())) {
                if (e.avancerDirectionChoisie(Direction.bas))
                    ret = true;
            }
        }

        return ret;
    }
}
