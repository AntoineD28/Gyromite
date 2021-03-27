package modele.deplacements;

import modele.plateau.Jeu;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;
    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        new Thread(this).start(); // Appel de la fonction Ordonnanceur.run()
    }

    @Override
    public void run() {
        boolean update = false;
        
        while(true) {
            jeu.resetCmptDepl();
            for (RealisateurDeDeplacement d : lstDeplacements) { // On parcours la liste lstDeplacements
                if (d.realiserDeplacement()) // On appel realiserDeplacement() de la classe à laquelle appartient d (Gravite, Controle4directions, ...)
                    update = true;
            }

            Controle4Directions.getInstance().resetDirection(); // On remet à null la direction courante 

            if (update) { // Si update == true
                setChanged();
                notifyObservers(); // Appel de la fonction VueControleurGyromite.update()
            }

            try {
                sleep(pause); // pause == 300 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
