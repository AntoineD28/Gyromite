package modele.plateau;

import modele.deplacements.Direction;

/**
 * Entités amenées à bouger (colonnes, ennemis)
 */
public abstract class EntiteDynamique extends Entite {
    public EntiteDynamique(Jeu _jeu) { super(_jeu); }

    public boolean avancerDirectionChoisie(Direction d) {
        return jeu.deplacerEntite(this, d);
    }
    public Entite regarderDansLaDirection(Direction d) {return jeu.regarderDansLaDirection(this, d);}
    public Entite regarderDansLaDirection(Direction dg, Direction hb) {return jeu.regarderDansLaDirection(this, dg, hb);}
    protected Direction directionCourante = Direction.droite;
    
    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
        //System.out.println(directionCourante);
    }
    
    public Direction getDirection() {
        return directionCourante;
    }
}
