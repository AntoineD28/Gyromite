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
public class Bot extends EntiteDynamique {
    
    private boolean monter = false; 
    
    public Bot(Jeu _jeu) {
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
    
    public void setMonter(boolean _monter){
        monter = _monter;
    }
    
}
