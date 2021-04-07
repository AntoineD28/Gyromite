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
        boolean play = true;
        int cpt = 1;
        Jeu jeu = new Jeu();
        while(true) {
            System.out.println(jeu.getGameOver());
            if(jeu.getGameOver() || play){
                play = false;
                
                if (cpt != 1)
                    jeu = new Jeu();

                VueControleurGyromite vc = new VueControleurGyromite(jeu);

                jeu.getOrdonnanceur().addObserver(vc);

                vc.setVisible(true);
                jeu.start(300);
                cpt++;
                //System.out.println("GameOver");
            }
        } 
        
            ///recommencer = Jeu.GameOver;
            //GameOver = jeu.getGameOver();
            /*if (jeu.getGameOver()){
                System.out.println("game");
                recommencer = true;
            }*/
        //}
        //System.out.println("game");
    }
    
}
