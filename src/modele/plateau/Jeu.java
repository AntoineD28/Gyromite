/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.deplacements.Gravite;
import modele.deplacements.Ordonnanceur;

import java.awt.Point;
import java.util.HashMap;

// Java Program to illustrate reading from 
// FileReader using FileReader 
import java.io.*; 

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;
    private Entite tmp; // Permet de stocker une entité 

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHector() {
        return hector;
    }
    
    private void initialisationDesEntites() {
        hector = new Heros(this);
        
        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        ordonnanceur.add(g);

        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());
        
        char[][] tab = new char[SIZE_X][SIZE_Y]; // Tableau tampon qui va contenir le fichier txt
        
        try {
            // We need to provide file path as the parameter: 
            // double backquote is to avoid compiler interpret words 
            // like \test as \t (ie. as a escape sequence) 
            File file = new File("map.txt"); // Fichier texte contenant le squelette de la carte 

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            int j = 0;
            while ((st = br.readLine()) != null) { // st -> contient la ligne du fichier à chaque boucle
                for (int i = 0; i < st.length(); i++) { // On découpe st en char
                    //System.out.print(st.charAt(j));
                    tab[i][j] = st.charAt(i); // chaque char est stocké dans la case du tableau correspondant
                }
                j++;
            }
        } catch (IOException e) {
            e.getMessage();
        }
        
        // Un fois tab rempli on le parcours pour connaître les entitées à ajouter dans la grille
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                switch (tab[i][j]) {
                    case 'm' -> addEntite(new Mur(this), i, j); // mur
                    case 'h' -> addEntite(hector, i, j); // héro
                    case 'b' -> addEntite(new Bombe(this), i, j); // bombe
                    case 'a' -> addEntite(new ColonneHaut(this), i, j);  
                    case 'z' -> addEntite(new ColonneMilieu(this), i, j);  
                    case 'e' -> addEntite(new ColonneBas(this), i, j);  
                    case 'c' -> addEntite(new Corde(this), i, j);
                    case 'X' -> addEntite(new Bot(this), i, j); // bombe
                    default -> {
                    }
                }
                // m -> mur
                //System.out.print(tab[i][j]);
                // h -> héro
                // b -> bombe
                            }
        }

        // murs extérieurs horizontaux
        /*for (int x = 0; x < 20; x++) {
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), 19, y);
        }

        addEntite(new Mur(this), 2, 6);
        addEntite(new Mur(this), 3, 6);*/
    }

    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }
    
    /** Permet par exemple a une entité de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);   
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || objetALaPosition(pCible) instanceof Corde)) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            switch (d) {
                case bas:
                case haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);

                        retour = true;
                    }
                    break;
                case gauche:
                case droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;

                    }
                    break;
            }
        }

        if (retour) {
            if (objetALaPosition(pCible) instanceof Corde) {
                if (tmp instanceof Corde)
                    grilleEntites[pCourant.x][pCourant.y] = tmp;
                tmp = grilleEntites[pCible.x][pCible.y];
                deplacerEntite(pCourant, pCible, e, true);
            }
            else  {
                deplacerEntite(pCourant, pCible, e, false);
            }
        }

        return retour;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
            
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e, Boolean Corde) {
        if (!Corde) {
            grilleEntites[pCourant.x][pCourant.y] = tmp;
            tmp = null;
        }
        else {
            grilleEntites[pCourant.x][pCourant.y] = null;
        }
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
}
