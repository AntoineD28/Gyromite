package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Mur;
import modele.plateau.Heros;
import modele.plateau.Corde;
import modele.plateau.Bot;

public class IA extends RealisateurDeDeplacement {

    //private boolean monter = false;
    
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
        Entite eGauche, eHaut, eBas, eBasG;
        
        for (EntiteDynamique e : lstEntitesDynamiques) {
            Bot actuel = (Bot)e;
            boolean monter = actuel.getMonter();
            
            if (e.getDirection() != null) {             
                // Début du processus pour permettre au smicks de prendre les cordes
                eHaut = e.regarderDansLaDirection(Direction.haut);
                eBas = e.regarderDansLaDirection(Direction.bas);
                eGauche = e.regarderDansLaDirection(Direction.gauche);

                if (eHaut != null && eHaut.peutPermettreDeMonterDescendre() && !monter){
                    double r = Math.random();
                    if (r < 0.5)
                        e.setDirectionCourante(Direction.haut);
                }
                else if (eHaut instanceof Mur && eBas instanceof Corde && (eGauche == null || eGauche.peutEtreEcrase()) && !monter){
                    //System.out.println(eHaut);
                    e.setDirectionCourante(Direction.gauche);
                    actuel.setMonter(true);
                }
                else if (eHaut instanceof Mur && eBas instanceof Corde && (eGauche == null || eGauche.peutEtreEcrase()) && !monter){
                    e.setDirectionCourante(Direction.gauche);
                    actuel.setMonter(true);
                }
                else if (eBas instanceof Corde && monter){
                    e.setDirectionCourante(Direction.bas);
                }
                else if (eBas instanceof Mur && eHaut instanceof Corde && monter){
                    double r = Math.random();
                    if (r < 0.5)
                        e.setDirectionCourante(Direction.gauche);
                    else 
                        e.setDirectionCourante(Direction.droite);
                    actuel.setMonter(false);
                }
                
                
                switch (e.getDirection()) {
                    case gauche:
                        //eGauche = e.regarderDansLaDirection(Direction.gauche); //Vérification de la case à gauche
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
                        break;
                    case droite:
                        Entite eDroite = e.regarderDansLaDirection(Direction.droite);
                        Entite eBasD = e.regarderDansLaDirection(Direction.bas);
                        if ((eDroite == null || eDroite.peutEtreRamasse() || eDroite.peutEtreEcrase()|| eDroite.peutPermettreDeMonterDescendre()) && eBasD != null) {
                            if (e.avancerDirectionChoisie(Direction.droite)) {
                                ret = true;
                            }
                        }
                        else e.setDirectionCourante(Direction.gauche);
                        break;
                    case bas:
                        if (e.avancerDirectionChoisie(Direction.bas)) {
                            ret = true;
                        }
                        break;
                    case haut:
                        // on ne peut pas sauter sans prendre appui
                        // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
                        //Entite eBas = e.regarderDansLaDirection(Direction.bas);
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
