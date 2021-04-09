package modele.plateau;

import modele.deplacements.Direction;

/**
 * Entités amenées à bouger (colonnes, ennemis)
 */
public abstract class EntiteDynamique extends Entite {

    protected Direction directionCourante = Direction.droite;
    
    /**
     * Constructeur de EntiteDynamique
     * @param _jeu
     */
    public EntiteDynamique(Jeu _jeu) { super(_jeu); }

    /**
     * Méthode qui permet de déplacer une entité dynamique en fonction de la direction donnée
     * @param d Direction
     * @return true di l'entitée à bien été déplacé
     */
    public boolean avancerDirectionChoisie(Direction d) {
        return jeu.deplacerEntite(this, d);
    }

    /**
     *
     * @param d Direction
     * @return l'élément à coté de l'entitée en fonction de la direction donnée
     */
    public Entite regarderDansLaDirection(Direction d) {return jeu.regarderDansLaDirection(this, d);}

    /**
     *
     * @param dg Direction (droite ou gauche)
     * @param hb Direction (haut ou bas)
     * @return l'élément dans une diagonale de l'entitée en fonction de la direction donnée
     */
    public Entite regarderDansLaDirection(Direction dg, Direction hb) {return jeu.regarderDansLaDirection(this, dg, hb);}
    
    /**
     * Permet de changer la direction courante
     * @param _directionCourante Direction
     */
    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
        //System.out.println(directionCourante);
    }
    
    /**
     *
     * @return la direction courante
     */
    public Direction getDirection() {
        return directionCourante;
    }
}
