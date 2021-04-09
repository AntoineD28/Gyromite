package modele.deplacements;

import modele.plateau.Jeu;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

/**
 *
 * @author lenymetzger
 */
public class Ordonnanceur extends Observable implements Runnable {
    private Thread t;
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;
    
    /**
     * Permet d'ajouter des éléments à la liste des déplacements
     * @param deplacement
     */
    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    /**
     *
     * @return La liste des éléments à déplacer
     */
    public ArrayList<RealisateurDeDeplacement> getLstDeplacements() {
        return lstDeplacements;
    }
    
    /**
     * vide la liste de déplacement 
     */
    public void reset() {
        lstDeplacements.clear();
    }
    
    /**
     * Constructeur de Ordonnanceur
     * @param _jeu
     */
    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    /**
     *
     * @param _pause
     */
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
            jeu.resetCmptDepl(); // On remet à 0 le compteur de déplacement
            
            for (RealisateurDeDeplacement d : lstDeplacements) { // On parcours la liste lstDeplacements
                if (d instanceof IA){ // Permet de ralentir les IA par rapport aux autres entitées
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

            //Controle4Directions.getInstance().resetDirection(); // On remet à null la direction courante 
            
            // Les colonnes doivent monter 2 fois à chaque déplacement et sont composés de 3 éléments (haut, milieu, bas), d'ou le 6)
            if (Colonne.getInstanceR().getCpt() == 6 * (Colonne.getInstanceR().GetLengthListe()/3)){ 
                Colonne.getInstanceR().resetDirection(); // On remet à null la direction courante
                Colonne.getInstanceR().setCpt(0); // On remet le compteur à 0
                if (Colonne.getInstanceR().getPosition() == Direction.bas) // On inverse les positions 
                    Colonne.getInstanceR().setPosition(Direction.haut);
                else Colonne.getInstanceR().setPosition(Direction.bas);
            }
            
            // Les colonnes doivent monter 2 fois à chaque déplacement et sont composés de 3 éléments (haut, milieu, bas), d'ou le 6)
            if (Colonne.getInstanceB().getCpt()== 6 * (Colonne.getInstanceB().GetLengthListe()/3)){
                Colonne.getInstanceB().resetDirection(); // On remet à null la direction courante
                Colonne.getInstanceB().setCpt(0); // On remet le compteur à 0
                if (Colonne.getInstanceB().getPosition() == Direction.bas) // On inverse les positions 
                    Colonne.getInstanceB().setPosition(Direction.haut);
                else Colonne.getInstanceB().setPosition(Direction.bas);
            }

            if (update) { // Si update == true
                setChanged();
                notifyObservers(); // Appel de la fonction VueControleurGyromite.update()
            }
            Controle4Directions.getInstance().resetDirection(); // On remet à null la direction courante 
            jeu.ConditionFin(); // Vérifie si le jeu n'est pas fini

            try {
                sleep(pause); // pause == 300 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}
