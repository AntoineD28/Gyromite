/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

/**
 *
 * @author lenymetzger
 */
public class Corde extends EntiteStatique {
    
    /**
     * Constructeur de Corde 
     * @param _jeu
     */
    public Corde(Jeu _jeu) { 
        super(_jeu); 
    }

    @Override
    public boolean peutServirDeSupport(){ return false; }; 

    @Override
    public boolean peutPermettreDeMonterDescendre() { return true; };
}
