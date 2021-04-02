package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class Colonne extends RealisateurDeDeplacement {
    private Direction directionCourante;
    
    private static Colonne c3d;
    
    public static Colonne getInstance() {
        if (c3d == null) {
            c3d = new Colonne();
        }
        return c3d;
    }
    
    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
        //System.out.println(directionCourante);
    }
   
    protected boolean realiserDeplacement() { 
        boolean ret = false;
        System.out.println(directionCourante);
            for (EntiteDynamique e : lstEntitesDynamiques) {
                //System.out.println(e);
                //System.out.println(directionCourante);
                if (directionCourante != null) {
                    switch (directionCourante) {
                        case monter:
                            if (e.avancerDirectionChoisie(Direction.haut)&&e.avancerDirectionChoisie(Direction.haut)&&e.avancerDirectionChoisie(Direction.haut)) {
                                ret = true;
                            }
                            break;
                        case descendre:
                            if (e.avancerDirectionChoisie(Direction.bas)) {
                                ret = true;
                            }
                            break;
                        /*case bas:
                        if (e.avancerDirectionChoisie(Direction.bas)) {
                            ret = true;
                        }
                        break;
                    case haut:
                        // on ne peut pas sauter sans prendre appui
                        // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
                        Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas != null && (eBas.peutServirDeSupport()||eBas.peutPermettreDeMonterDescendre())) {
                            if (e.avancerDirectionChoisie(Direction.haut))
                                ret = true;
                        }
                        break;*/
                    }
                }
            }


        return ret;
    }
    
    public void resetDirection() {
        directionCourante = null;
    }
}
