package modele.deplacements;

import java.util.ArrayList;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.*;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des
 * colonnes se déplacent dans la direction définie (vérifier "collisions" avec
 * le héros)
 */
public class Colonne extends RealisateurDeDeplacement {

    private Direction directionCourante;

    private static Colonne colRouge;
    private static Colonne colBleu;

    private int colCpt = 0;

    private Direction position;

    
    
    public static Colonne getInstanceR() {
        if (colRouge == null) {
            colRouge = new Colonne();
        }
        return colRouge;
    }

    public static Colonne getInstanceB() {
        if (colBleu == null) {
            colBleu = new Colonne();
        }
        return colBleu;
    }

    public Direction getDirectionCourante() {
        //lastDirection = directionCouranteR;
        return directionCourante;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        //lastDirection = directionCouranteR;
        directionCourante = _directionCourante;
    }

    protected boolean realiserDeplacement() {
        boolean ret = false;
        ArrayList<EntiteDynamique> listeTmp = new ArrayList<EntiteDynamique>();

        if (position == Direction.haut){
            listeTmp = reverseLst();
            int i = 0;
        }
        
        else if (position == Direction.bas){
            listeTmp = lstEntitesDynamiques;
        }
        
        for (EntiteDynamique e : listeTmp) {
            if (directionCourante != null) {
                switch (directionCourante) {
                    case changer:
                        if (((e instanceof ColonneBasR) || (e instanceof ColonneMilieuR) || (e instanceof ColonneHautR)) && (position == Direction.bas)) {
                            if (e.avancerDirectionChoisie(Direction.haut)) {
                                ret = true;
                            }
                            colCpt++;
                        } 
                        else if (((e instanceof ColonneBasR) || (e instanceof ColonneMilieuR) || (e instanceof ColonneHautR)) && (position == Direction.haut)) {
                            if (e.avancerDirectionChoisie(Direction.bas)) {
                                ret = true;
                            }
                            colCpt++;
                        } 
                        else if (((e instanceof ColonneBasB) || (e instanceof ColonneMilieuB) || (e instanceof ColonneHautB)) && (position == Direction.bas)) {
                            if (e.avancerDirectionChoisie(Direction.haut)) {
                                ret = true;
                            }
                            colCpt++;
                        } 
                        else if (((e instanceof ColonneBasB) || (e instanceof ColonneMilieuB) || (e instanceof ColonneHautB)) && (position == Direction.haut)) {
                            if (e.avancerDirectionChoisie(Direction.bas)) {
                                ret = true;
                            }
                            colCpt++;
                        }
                        break;
                }
            }
        }
        return ret;
    }

    public void resetDirection() {
        directionCourante = null;
    }

    public int getCpt() {
        return colCpt;
    }

    public void setCpt(int cpt) {
        colCpt = cpt;
    }

    public void setPosition(Direction d) {
        position = d;
    }

    public Direction getPosition() {
        return position;
    }

}
