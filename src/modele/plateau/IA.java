/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.Direction;

/**
 * Ennemis (Smicks)
 */
public class IA extends EntiteDynamique {
    
    private boolean monter = false; 
    
    /**
     * Constructeur de IA
     * @param _jeu
     */
    public IA(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreEcrase() { return true; }

    @Override
    public boolean peutServirDeSupport() { return true; }

    @Override
    public boolean peutPermettreDeMonterDescendre() { return false; };

    @Override
    public boolean peutEtreRamasse() {return false;};
    
    public boolean getMonter(){
        return monter;
    }
    
    /**
     * Permet de modifier l'attribut monter 
     * @param _monter true si l'IA est monté en haut d'une corde
     */
    public void setMonter(boolean _monter){
        monter = _monter;
    }
    
}
