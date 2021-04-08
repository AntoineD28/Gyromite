package modele.deplacements;

import modele.plateau.Jeu;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Thread t;
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;
    
    
    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    public ArrayList<RealisateurDeDeplacement> getLstDeplacements() {
        return lstDeplacements;
    }
    
    public void reset() {
        lstDeplacements.clear();
    }
    
    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        t = new Thread(this); // Appel de la fonction Ordonnanceur.run()
        t.start();
    }

    @Override
    public void run() {
        boolean update = false;
        int IAlent = 0;

        while(true) {
            jeu.resetCmptDepl();
            for (RealisateurDeDeplacement d : lstDeplacements) { // On parcours la liste lstDeplacements
                if (d instanceof IA){
                    if (IAlent%2==0){
                        if (d.realiserDeplacement()) // On appel realiserDeplacement() de la classe à laquelle appartient d (Gravite, Controle4directions, ...)
                            update = true;
                    }
                    IAlent++;
                }
                else{
                    if (d.realiserDeplacement()) // On appel realiserDeplacement() de la classe à laquelle appartient d (Gravite, Controle4directions, ...)
                        update = true;
                }
            }

            Controle4Directions.getInstance().resetDirection(); // On remet à null la direction courante 
            
            if (Colonne.getInstanceR().getCpt()==6){
                Colonne.getInstanceR().resetDirection(); // On remet à null la direction courante
                Colonne.getInstanceR().setCpt(0);
                if (Colonne.getInstanceR().getPosition() == Direction.bas)
                    Colonne.getInstanceR().setPosition(Direction.haut);
                else Colonne.getInstanceR().setPosition(Direction.bas);
            }
            
            if (Colonne.getInstanceB().getCpt()==6){
                Colonne.getInstanceB().resetDirection(); // On remet à null la direction courante
                Colonne.getInstanceB().setCpt(0);
                if (Colonne.getInstanceB().getPosition() == Direction.bas)
                    Colonne.getInstanceB().setPosition(Direction.haut);
                else Colonne.getInstanceB().setPosition(Direction.bas);
            }

            if (update) { // Si update == true
                setChanged();
                notifyObservers(); // Appel de la fonction VueControleurGyromite.update()
            }
            
            jeu.ConditionFin();

            try {
                sleep(pause); // pause == 300 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}
