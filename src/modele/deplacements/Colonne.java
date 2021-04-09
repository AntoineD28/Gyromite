package modele.deplacements;

import java.util.ArrayList;
import modele.plateau.EntiteDynamique;
import modele.plateau.*;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des
 * colonnes se déplacent dans la direction définie (les Colonnes bleues et 
 * les Colonnes rouges se déplacent toujours dans le sens contraire les unes 
 * des autres)
 */
public class Colonne extends RealisateurDeDeplacement {

    private Direction directionCourante;

    private static Colonne colRouge; // Instance de colonne rouge 
    private static Colonne colBleu; // Instance de colonne bleu

    private int colCpt = 0; // Compteur pour que les colonnes se déplace le bon nombre de fois 

    private Direction position; // Position de la colonne (soit bas, soit haut)

    /**
     *
     * @return l'instance qui contient les colonnes rouges 
     */
    public static Colonne getInstanceR() {
        if (colRouge == null) {
            colRouge = new Colonne();
        }
        return colRouge;
    }

    /**
     *
     * @return l'instance qui contient les colonnes bleues 
     */
    public static Colonne getInstanceB() {
        if (colBleu == null) {
            colBleu = new Colonne();
        }
        return colBleu;
    }

    /**
     *
     * @return la direction courante
     */
    public Direction getDirectionCourante() {
        //lastDirection = directionCouranteR;
        return directionCourante;
    }

    /**
     * Permet de modifier la direction courante d'une instance de Colonne
     * @param _directionCourante
     */
    public void setDirectionCourante(Direction _directionCourante) {
        //lastDirection = directionCouranteR;
        directionCourante = _directionCourante;
    }

    /**
     * Réalise le déplacement d'une instance de Colonne (soit les rouges, soit les bleues)
     * @return true si le déplacement à pu se faire, false sinon
     */
    @Override
    protected boolean realiserDeplacement() {
        boolean ret = false;
        ArrayList<EntiteDynamique> listeTmp = new ArrayList<EntiteDynamique>();

        // On a besoin de retourner la liste dans le cas ou on monte car il faut commencer par ColonneHaut et pas par ColonneBas
        if (position == Direction.haut){
            listeTmp = reverseLst();
        }
        
        // Si c'est pas le cas, on garde l'orde de la liste tel qu'on l'a créé 
        else if (position == Direction.bas){
            listeTmp = lstEntitesDynamiques;
        }
        
        for (EntiteDynamique e : listeTmp) {
            
            // Le seul cas possible est "changer" donc il n'y a pas besoin de switch 
            if (directionCourante != null) {
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
                }
            }
        return ret;
    }

    /**
     * Mettre la direction courante à null
     */
    public void resetDirection() {
        directionCourante = null;
    }

    /**
     *
     * @return le Cpt d'une instance de Colonne
     */
    public int getCpt() {
        return colCpt;
    }

    /**
     * Permet de modifier le Cpt d'une instance de Colonne
     * @param cpt
     */
    public void setCpt(int cpt) {
        colCpt = cpt;
    }

    /**
     * Permet de modifier la position d'une instance de Colonne
     * @param d Direction
     */
    public void setPosition(Direction d) {
        position = d;
    }

    /**
     *
     * @return la position d'une instance de Colonne
     */
    public Direction getPosition() {
        return position;
    }

}
