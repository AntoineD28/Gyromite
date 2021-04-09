/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

/**
 *
 * @author Antoine
 */
public class Bombe extends EntiteStatique {

    /**
     * Constructeur de Bombe 
     * @param _jeu
     */
    public Bombe(Jeu _jeu) { super(_jeu); }
    
    @Override
    public boolean peutEtreRamasse() {return true;};

    @Override
    public boolean peutServirDeSupport() { return false; }
}