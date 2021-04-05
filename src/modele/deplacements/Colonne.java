package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.*;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class Colonne extends RealisateurDeDeplacement {
    private Direction directionCourante;
    
    private static Colonne colRouge;
    private static Colonne colBleu;
    
    private int colCpt = 0;
    
    private Direction lastDirection;
    
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
        //System.out.println(directionCourante);
        /*if ((lastDirection != directionCourante) && (lastDirection != null)){
            this.setCpt(0);
        }*/
            for (EntiteDynamique e : lstEntitesDynamiques) {
                //System.out.println(getInstanceR().directionCourante);
                //System.out.println(getInstanceB().directionCourante);
                if ((getInstanceR().directionCourante != null) && (getInstanceB().directionCourante != null)) {
                    switch (getInstanceR().directionCourante) {
                        case changer:
                            //lastDirection = Direction.monter;
                            if (((e instanceof ColonneBasR) || (e instanceof ColonneMilieuR) || (e instanceof ColonneHautR)) && (position==Direction.bas)){
                                if (e.avancerDirectionChoisie(Direction.haut)) {
                                    ret = true;
                                    Colonne.getInstanceR().colCpt++;
                                    //getInstanceB().setPosition(Direction.haut);
                                }
                            }
                            if (((e instanceof ColonneBasR) || (e instanceof ColonneMilieuR) || (e instanceof ColonneHautR)) && (position==Direction.haut)){
                                if (e.avancerDirectionChoisie(Direction.bas)) {
                                    ret = true;
                                    Colonne.getInstanceR().colCpt++;
                                    //getInstanceB().setPosition(Direction.bas);
                                }
                            }
                            if (((e instanceof ColonneBasB) || (e instanceof ColonneMilieuB) || (e instanceof ColonneHautB)) && (position==Direction.bas)){
                                if (e.avancerDirectionChoisie(Direction.haut)) {
                                    ret = true;
                                    Colonne.getInstanceB().colCpt++;
                                    //getInstanceB().setPosition(Direction.haut);
                                }
                            }
                            if (((e instanceof ColonneBasB) || (e instanceof ColonneMilieuB) || (e instanceof ColonneHautB)) && (position==Direction.haut)){
                                if (e.avancerDirectionChoisie(Direction.bas)) {
                                    ret = true;
                                    Colonne.getInstanceB().colCpt++;
                                    //getInstanceB().setPosition(Direction.bas);
                                }
                            }
                            break;
                    }
                }
            }
            /*
            System.out.println(getInstanceR().getPosition());
            System.out.println(getInstanceB().getPosition());
            Direction tmp = getInstanceR().getPosition();
            getInstanceR().setPosition(getInstanceB().getPosition());
            getInstanceB().setPosition(tmp);
            System.out.println(getInstanceR().getPosition());
            System.out.println(getInstanceB().getPosition());
            */

        return ret;
    }
    
    public void resetDirection() {
        directionCourante = null;
    }
    
    
    public int getCpt(){
        return colCpt;
    }
    
    public void setCpt(int cpt){
        colCpt = cpt;
    }
    
    public void setPosition(Direction d){
        position = d;
    }
    
    public Direction getPosition(){
        return position;
    }
    
}
