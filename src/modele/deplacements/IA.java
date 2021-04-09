package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Mur;
import modele.plateau.Corde;
import modele.plateau.Brique;

/**
 *
 * @author lenymetzger
 */
public class IA extends RealisateurDeDeplacement {
    
    private static IA ia;

    /**
     *
     * @return une instance de IA
     */
    public static IA getInstance() {
        if (ia == null) {
            ia = new IA();
        }
        return ia;
    }

    /**
     * Réalise le déplacement d'une instance de IA
     * @return true si le déplacement à pu se faire, false sinon
     */
    @Override
    protected boolean realiserDeplacement() {
        boolean ret = false;
        Entite eGauche, eHaut, eBas, eBasG;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            modele.plateau.IA actuel = (modele.plateau.IA)e;
            boolean monter = actuel.getMonter(); // Booléen qui permet de savoir si le smicks est monté à une corde
            
            if (e.getDirection() != null) {          
                // On récupere les entitées autour de l'entitée courante
                eHaut = e.regarderDansLaDirection(Direction.haut);
                eBas = e.regarderDansLaDirection(Direction.bas);
                eGauche = e.regarderDansLaDirection(Direction.gauche);

                // Permet au smicks de prendre les cordes (50% de chance qu'ils prennent les cordes)
                if (eHaut != null && eHaut.peutPermettreDeMonterDescendre() && !monter){
                    double r = Math.random();
                    if (r < 0.5)
                        e.setDirectionCourante(Direction.haut);
                }
                // Permet au smicks de sortir de la corde lorsqu'il atteind le haut de celle ci
                else if (eHaut instanceof Mur && eBas instanceof Corde && (eGauche == null || eGauche.peutEtreEcrase() || eGauche.peutEtreRamasse()) && !monter){
                    e.setDirectionCourante(Direction.gauche);
                    actuel.setMonter(true);
                }
                // Si il ne peut pas aller à gauche en haut d'une corde, il ira à droite
                else if (eHaut instanceof Mur && eBas instanceof Corde && (eGauche == null || eGauche.peutEtreEcrase()) && !monter){
                    e.setDirectionCourante(Direction.gauche);
                    actuel.setMonter(true);
                }
                // Permet au smicks de descendre une corde
                else if (eBas instanceof Corde && monter){
                    e.setDirectionCourante(Direction.bas);
                } 
                // Permet au smicks de reprendre son déplacement apres etre descendu d'une corde (50% de chance de partir de chaque coté)
                else if ((eBas instanceof Mur || eBas instanceof Brique) && eHaut instanceof Corde && monter){
                    double r = Math.random();
                    if (r < 0.5)
                        e.setDirectionCourante(Direction.gauche);
                    else 
                        e.setDirectionCourante(Direction.droite);
                    actuel.setMonter(false);
                }
                
                
                switch (e.getDirection()) {
                    case gauche:
                        eBasG = e.regarderDansLaDirection(Direction.gauche, Direction.bas); // Vérification de la case en bas à gauche

                        //Gère les collisions et permet au smick de ne pas tomber
                        if ((eGauche == null || eGauche.peutEtreRamasse() || (eGauche.peutEtreEcrase() && !(eGauche instanceof modele.plateau.IA)) || eGauche.peutPermettreDeMonterDescendre()) && eBasG != null) {
                            //System.out.println("avancer");
                            if (e.avancerDirectionChoisie(Direction.gauche)) {
                                    ret = true;
                            }
                        }
                        else {
                            e.setDirectionCourante(Direction.droite);
                        }
                        break;
                        
                    case droite:
                        Entite eDroite = e.regarderDansLaDirection(Direction.droite); // Vérification de la case à droite du smick
                        Entite eBasD = e.regarderDansLaDirection(Direction.droite, Direction.bas); // Vérification de la case en bas à droite
                        //Gère les collisions et permet au smick de ne pas tomber
                        
                        if ((eDroite == null || eDroite.peutEtreRamasse() || (eDroite.peutEtreEcrase() && !(eDroite instanceof modele.plateau.IA)) || eDroite.peutPermettreDeMonterDescendre()) && eBasD != null) {
                            if (e.avancerDirectionChoisie(Direction.droite)) {
                                ret = true;
                            }
                        }
                        else {
                            e.setDirectionCourante(Direction.gauche);
                        }
                        break;
                        
                    case bas:
                        if (e.avancerDirectionChoisie(Direction.bas)) {
                            ret = true;
                        }
                        break;
                        
                    case haut:
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
