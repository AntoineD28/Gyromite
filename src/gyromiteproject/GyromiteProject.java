/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyromiteproject;
import VueControleur.VueControleurGyromite;
import modele.plateau.Jeu;
/**
 *
 * @author Antoine
 */
public class GyromiteProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        
        VueControleurGyromite vc = new VueControleurGyromite(jeu);

        jeu.getOrdonnanceur().addObserver(vc);
        
        vc.setVisible(true);
        jeu.start(300);
    }
    
}
