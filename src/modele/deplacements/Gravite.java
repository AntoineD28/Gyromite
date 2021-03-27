package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class Gravite extends RealisateurDeDeplacement {
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            //System.out.println(eBas);
            if (eBas == null || (eBas != null && !eBas.peutServirDeSupport() && !eBas.peutPermettreDeMonterDescendre())) {
                if (e.avancerDirectionChoisie(Direction.bas))
                    ret = true;
            }
        }

        return ret;
    }
}
