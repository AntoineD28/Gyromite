package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Mur;
import modele.plateau.Heros;

public class IA extends RealisateurDeDeplacement {

    private static IA c3d;

    public static IA getInstance() {
        if (c3d == null) {
            c3d = new IA();
        }
        return c3d;
    }

    @Override
    protected boolean realiserDeplacement() {
        boolean ret = false;
        Entite eGauche, eHaut, eBasG;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            //System.out.println(directionCourante);
            if (e.getDirection() != null) {
                
                // Début du processus pour permettre au smicks de prendre les cordes
                eHaut = e.regarderDansLaDirection(Direction.haut);
                eGauche = e.regarderDansLaDirection(Direction.gauche);
                //eBasG = e.regarderDansLaDirection(Direction.gauche, Direction.bas);
                if (eHaut != null && eHaut.peutPermettreDeMonterDescendre())
                    e.setDirectionCourante(Direction.haut);
                else e.setDirectionCourante(Direction.gauche);
                 
                switch (e.getDirection()) {
                    case gauche:
                        eGauche = e.regarderDansLaDirection(Direction.gauche); //Vérification de la case à gauche
                        eBasG = e.regarderDansLaDirection(Direction.gauche, Direction.bas); // Vérification de la case en bas à gauche
                        //System.out.println(eBasG);
                        //System.out.println(eGauche);
                        if ((eGauche == null || eGauche.peutEtreRamasse() || eGauche.peutEtreEcrase() || eGauche.peutPermettreDeMonterDescendre()) && eBasG != null) {
                            //System.out.println("avancer");
                            if (e.avancerDirectionChoisie(Direction.gauche)) {
                                    ret = true;
                            }
                        }
                        else {
                            //System.out.println("demi-tour");
                            e.setDirectionCourante(Direction.droite);
                        }
                    case droite:
                        Entite eDroite = e.regarderDansLaDirection(Direction.droite);
                        Entite eBasD = e.regarderDansLaDirection(Direction.bas);
                        if ((eDroite == null || eDroite.peutEtreRamasse() || eDroite.peutEtreEcrase()|| eDroite.peutPermettreDeMonterDescendre()) && eBasD != null) {
                            if (e.avancerDirectionChoisie(Direction.droite)) {
                                ret = true;
                            }
                        }
                        else e.setDirectionCourante(Direction.gauche);
                    case bas:
                        if (e.avancerDirectionChoisie(Direction.bas)) {
                            ret = true;
                        }
                        break;
                    case haut:
                        // on ne peut pas sauter sans prendre appui
                        // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
                        Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas != null && (eBas.peutServirDeSupport() || eBas.peutPermettreDeMonterDescendre())) {
                            if (e.avancerDirectionChoisie(Direction.haut)) {
                                ret = true;
                            }
                        }
                        break;
                }
            }
        }
        return ret;
    }
}
