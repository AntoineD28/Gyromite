package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le
 * clavier) à un ensemble d'entités dynamiques
 */
public class Controle4Directions extends RealisateurDeDeplacement {

    private Direction directionCourante;
    // Design pattern singleton
    private static Controle4Directions c3d;

    /**
     *
     * @return une instance de Controle4Directions
     */
    public static Controle4Directions getInstance() {
        if (c3d == null) {
            c3d = new Controle4Directions();
        }
        return c3d;
    }

    /**
     * Permet de modifier la direction courante d'une instance de Controle4Directions
     * @param _directionCourante
     */
    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }
    
    /**
     *
     * @return la direction courante
     */
    public Direction getDirectionCOurante() {
        return directionCourante;
    }

    /**
     * Réalise le déplacement d'une instance de Controle4Directions
     * @return true si le déplacement à pu se faire, false sinon
     */
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;
        
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null) {
                switch (directionCourante) {
                    case gauche:
                    case droite:
                        if (e.avancerDirectionChoisie(directionCourante)) {
                            ret = true;
                        }
                        break;
                    case bas:
                        if (e.avancerDirectionChoisie(Direction.bas)) {
                            ret = true;
                        }
                        break;
                    case haut:
                        // on ne peut pas sauter sans prendre appui
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

    /**
     * Permet de mettre la direction courante à null
     */
    public void resetDirection() {
        directionCourante = null;
    }
}
