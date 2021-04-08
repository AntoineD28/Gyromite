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
import modele.deplacements.Colonne;
import modele.deplacements.IA;


import java.awt.Point;
import java.util.HashMap;

// Java Program to illustrate reading from 
// FileReader using FileReader 
import java.io.*; 
import java.util.ArrayList;
import modele.deplacements.RealisateurDeDeplacement;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {
    
    public static boolean GameOver;
    private static boolean HerosDead;
    
    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;
    private Direction directionCouranteHeros;
    private Entite tmp; // Permet de stocker une entité 
    private int cptCol = 1;
    private int NbBombe = 0;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private ArrayList<Entite> grilleEntites[][]  = new ArrayList[SIZE_X][SIZE_Y];
   //private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur;

    public Jeu() {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                grilleEntites[i][j] = new ArrayList<Entite>();
            }
        }
        GameOver = false;
        ordonnanceur = new Ordonnanceur(this);
        initialisationDesEntites();
    }

    private void reset() {
        for (RealisateurDeDeplacement r : ordonnanceur.getLstDeplacements()){r.reset();}
        ordonnanceur.reset();
        map = new  HashMap<Entite, Point>();
        resetCmptDepl();
        grilleEntites = new ArrayList[SIZE_X][SIZE_Y];
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                grilleEntites[i][j] = new ArrayList<Entite>();
            }
        }
        NbBombe = 0;
        HerosDead = false;
        initialisationDesEntites();
    }
    
    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public  ArrayList<Entite>[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHector() {
        return hector;
    }
    
    public Point getHectorPos() {
        return map.get(hector);
    }
    public Direction getDirCouranteHector() {
        return directionCouranteHeros;
    }
   
    
    private void initialisationDesEntites() {
        hector = new Heros(this);
        
        //Gravite g = new Gravite();
        Gravite.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Gravite.getInstance());

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
                    case 'm':
                        addEntite(new Mur(this), i, j);
                        break;// mur
                    case 'h':
                        addEntite(hector, i, j);
                        break;// héro
                    case 'b':
                        addEntite(new Bombe(this), i, j);
                        NbBombe++;
                        break;// bombe
                    case '1':
                        ColonneHautR C_hr = new ColonneHautR(this);
                        addEntite(C_hr, i, j);
                        Colonne.getInstanceR().addEntiteDynamique(C_hr);
                        Colonne.getInstanceR().setPosition(Direction.bas);
                        break;
                    case '2':
                        ColonneMilieuR C_mr = new ColonneMilieuR(this);
                        addEntite(C_mr, i, j);
                        Colonne.getInstanceR().addEntiteDynamique(C_mr);
                        Colonne.getInstanceR().setPosition(Direction.bas);
                        break;
                    case '3':
                        ColonneBasR C_br = new ColonneBasR(this);
                        addEntite(C_br, i, j);
                        Colonne.getInstanceR().addEntiteDynamique(C_br);
                        Colonne.getInstanceR().setPosition(Direction.bas);
                        break;
                    case '4':
                        ColonneHautB C_hb = new ColonneHautB(this);
                        addEntite(C_hb, i, j);
                        Colonne.getInstanceB().addEntiteDynamique(C_hb);
                        Colonne.getInstanceB().setPosition(Direction.haut);
                        break;
                    case '5':
                        ColonneMilieuB C_mb = new ColonneMilieuB(this);
                        addEntite(C_mb, i, j);
                        Colonne.getInstanceB().addEntiteDynamique(C_mb);
                        Colonne.getInstanceB().setPosition(Direction.haut);
                        break;
                    case '6':
                        ColonneBasB C_bb = new ColonneBasB(this);
                        addEntite(C_bb, i, j);
                        Colonne.getInstanceB().addEntiteDynamique(C_bb);
                        Colonne.getInstanceB().setPosition(Direction.haut);
                        break;
                    case 'c':
                        addEntite(new Corde(this), i, j);
                        break;
                    case 'X':
                        Bot b = new Bot(this); 
                        addEntite(b, i, j);
                        IA.getInstance().addEntiteDynamique(b);
                        Gravite.getInstance().addEntiteDynamique(b);
                        break;// bombe
                    default: {
                    }
                }
                
                
                
                // m -> mur
                //System.out.print(tab[i][j]);
                // h -> héro
                // b -> bombe
            }
        }
        
        ordonnanceur.add(Colonne.getInstanceR());
        ordonnanceur.add(Colonne.getInstanceB());
        ordonnanceur.add(IA.getInstance());

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
        grilleEntites[x][y].add(e);
        //System.out.println(grilleEntites[x][y].get(0));
        map.put(e, new Point(x, y));
    }
    
    /** Permet par exemple a une entité de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);   
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    // Surcharge pour regarder en diagonale
    public Entite regarderDansLaDirection(Entite e, Direction dg, Direction hb) { 
        Point positionEntite = map.get(e); 
        Entite tmp = objetALaPosition(calculerPointCible(positionEntite, hb));
        Point positionEntiteCote = map.get(tmp);
        return objetALaPosition(calculerPointCible(positionEntiteCote, dg));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        
        if ( e instanceof Heros)
            directionCouranteHeros = d;
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || objetALaPosition(pCible).peutPermettreDeMonterDescendre() || objetALaPosition(pCible).peutEtreEcrase() ||objetALaPosition(pCible).peutEtreRamasse())) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            switch (d) {
                case bas:
                case haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);
                        retour = true;
                        if (objetALaPosition(pCible) == null && objetALaPosition(pCourant) instanceof Corde && d == Direction.haut)
                            retour = false;
                    }
                    break;
                case gauche:
                case droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;
                    }
                    break;
                case monter:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);
                        retour = true;
                    }
                    break;
            }
        }

        if (retour) {
            //if (e instanceof Heros) System.out.println("héros");
            if ((objetALaPosition(pCible) instanceof Corde || (objetALaPosition(pCible) instanceof Bombe && objetALaPosition(pCourant) instanceof Bot)) /*&& grilleEntites[pCible.x][pCible.y].size() == 1*/) { // Si la pCible est une corde
                //if (e instanceof modele.plateau.Colonne) 
                //System.out.println("test");
                deplacerEntite(pCourant, pCible, e, true);
            } 
            else if ((objetALaPosition(pCible) instanceof Heros || objetALaPosition(pCible) instanceof Bot) && objetALaPosition(pCourant) instanceof modele.plateau.Colonne && objetALaPositionSuivante(pCible, d) instanceof Mur) {
                supprimerDeplacerEntite(pCourant, pCible, e);
            }
            else if ((objetALaPosition(pCible) instanceof Heros || objetALaPosition(pCible) instanceof Bot) && objetALaPosition(pCourant) instanceof modele.plateau.Colonne && !(objetALaPositionSuivante(pCible, d) instanceof Mur)) {
                monterDeplacerEntite(pCourant, pCible, e, d);
            }
            else if (objetALaPosition(pCible) instanceof Heros && objetALaPosition(pCourant) instanceof Bot) {
                supprimerDeplacerEntite(pCourant, pCible, e);
            }
            else if (objetALaPosition(pCible) instanceof Bot && objetALaPosition(pCourant) instanceof Heros) {
                deplacerSupprimerEntite(pCourant, pCible, e);
            }
            else if (objetALaPosition(pCible) instanceof Bombe) {
                //System.out.println(NbBombe);
                NbBombe--;
                deplacerEntite(pCourant, pCible, e);
                //System.out.println(NbBombe);
                //if (NbBombe == 0) GameOver = true;
            }
            else {
                //if (e instanceof modele.plateau.Colonne) System.out.println("Colonne3");
                deplacerEntite(pCourant, pCible, e);
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

    // Surcharge pour le déplacement sur une corde
    private void deplacerEntite(Point pCourant, Point pCible, Entite e, Boolean Corde) {
        if (Corde)
            //System.out.println("//");
            //System.out.println(grilleEntites[pCible.x][pCible.y].get(0));
            //System.out.println(grilleEntites[pCible.x][pCible.y].get(1));
            if (grilleEntites[pCible.x][pCible.y].size() == 2 && grilleEntites[pCible.x][pCible.y].get(1) instanceof Heros){
                grilleEntites[pCourant.x][pCourant.y].remove(1);
                grilleEntites[pCible.x][pCible.y].remove(1);
                HerosDead = true;
            }
            else if (grilleEntites[pCible.x][pCible.y].size() == 2 && grilleEntites[pCible.x][pCible.y].get(1) instanceof Bot){
                //System.out.println("test");
                grilleEntites[pCourant.x][pCourant.y].remove(1);
                //System.out.println("test");
                grilleEntites[pCible.x][pCible.y].remove(1);
                //System.out.println("test");
                HerosDead = true;
            }
            else if (grilleEntites[pCourant.x][pCourant.y].size() == 2)
                grilleEntites[pCourant.x][pCourant.y].remove(1);
            else grilleEntites[pCourant.x][pCourant.y].clear();
        grilleEntites[pCible.x][pCible.y].add(e);
        map.put(e, pCible);
    }

    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        if (grilleEntites[pCourant.x][pCourant.y].size() == 2) {
            grilleEntites[pCourant.x][pCourant.y].remove(1);
        } else grilleEntites[pCourant.x][pCourant.y].clear();
        //System.out.println(grilleEntites[pCourant.x][pCourant.y]);
        grilleEntites[pCible.x][pCible.y].clear();
        grilleEntites[pCible.x][pCible.y].add(e);
        map.put(e, pCible);
    }
    
    private void supprimerDeplacerEntite(Point pCourant, Point pCible, Entite e) {
        map.remove(objetALaPosition(pCible));
        if(objetALaPosition(pCible) instanceof Bot){
            IA.getInstance().RemEntiteDynamique((EntiteDynamique)objetALaPosition(pCible));
        }
        if(objetALaPosition(pCible) instanceof Heros){
            Controle4Directions.getInstance().RemEntiteDynamique((EntiteDynamique)objetALaPosition(pCible));
            Gravite.getInstance().RemEntiteDynamique((EntiteDynamique)objetALaPosition(pCible));
            HerosDead = true;
        }
        grilleEntites[pCible.x][pCible.y].clear();
        grilleEntites[pCible.x][pCible.y].add(e);
        grilleEntites[pCourant.x][pCourant.y].clear();
        map.put(e, pCible);
    }
    
    private void deplacerSupprimerEntite(Point pCourant, Point pCible, Entite e) {
        map.remove(objetALaPosition(pCourant));
        HerosDead = true;
        grilleEntites[pCourant.x][pCourant.y].clear();
    }
    
    private void monterDeplacerEntite(Point pCourant, Point pCible, Entite e, Direction d) {
        Point newPosition = new Point();
        
        switch(d){
            case droite : 
                newPosition = new Point(pCible.x-1,pCible.y);
                break;
            case gauche : 
                newPosition = new Point(pCible.x+1,pCible.y);
                break;
            case haut : 
                newPosition = new Point(pCible.x,pCible.y-1);
                break;
            case bas : 
                newPosition = new Point(pCible.x,pCible.y+1);
                break;
        }
        
        Entite tmp = grilleEntites[pCible.x][pCible.y].get(0);
        grilleEntites[pCible.x][pCible.y].clear();
        grilleEntites[newPosition.x][newPosition.y].add(tmp);
        grilleEntites[pCible.x][pCible.y].add(e);
        grilleEntites[pCourant.x][pCourant.y].clear();
        map.put(tmp, newPosition);
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
            //System.out.println(grilleEntites[p.x][p.y]);
            if (grilleEntites[p.x][p.y].isEmpty())
                retour = null;
            else retour = grilleEntites[p.x][p.y].get(0);
        }
        
        return retour;
    }
    
    private Entite objetALaPositionSuivante(Point p, Direction d) {
        Entite retour = null;
        Point pt = new Point();
        
        switch(d){
            case droite : 
                pt = new Point(p.x-1,p.y);
                break;
            case gauche : 
                pt = new Point(p.x+1,p.y);
                break;
            case haut : 
                pt = new Point(p.x,p.y-1);
                break;
            case bas : 
                pt = new Point(p.x,(p.y)+1);
                break;
        }
        
        if (contenuDansGrille(pt)) {
            //System.out.println(grilleEntites[p.x][p.y]);
            if (grilleEntites[pt.x][pt.y].isEmpty())
                retour = null;
            else retour = grilleEntites[pt.x][pt.y].get(0);
        }
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
    
    public boolean getGameOver() {
        return GameOver;
    }
    
    public void setGameOver(boolean b) {
        GameOver = b;
    }
    
    public boolean getHerosDead() {
        return HerosDead;
    }
    
    public void ConditionFin() {
        if (NbBombe == 0 || HerosDead) {
            reset();
        }
    }
}
