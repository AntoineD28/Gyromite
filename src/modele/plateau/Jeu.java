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
    private int NbBombe = 0; // Nombre de Bombe dans la map

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private ArrayList<Entite> grilleEntites[][]  = new ArrayList[SIZE_X][SIZE_Y];
   //private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur;

    /**
     * Constructeur de Jeu
     */
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

    // Permet de reset le jeu lorsqu'on meurt ou qu'on fini le niveau
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
    
    /**
     * Reset les compteurs de déplacement vertical et honrizontal
     */
    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    /**
     *
     * @return la grille des Entitées
     */
    public  ArrayList<Entite>[][] getGrille() {
        return grilleEntites;
    }
    
    /**
     *
     * @return l'instance de Heros 
     */
    public Heros getHector() {
        return hector;
    }
    
    /**
     *
     * @return la position du Heros
     */
    public Point getHectorPos() {
        return map.get(hector);
    }

    /**
     *
     * @return la direction courant du Heros
     */
    public Direction getDirCouranteHector() {
        return directionCouranteHeros;
    }
   
    private void initialisationDesEntites() {
        hector = new Heros(this);
        
        // On ajoute le héros dans les entitées qui subbissent la gravité
        Gravite.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Gravite.getInstance());

        // On ajoute le héros dans les entitées dont le déplacement est géré par Controle4Directions
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
                    case '#':
                        addEntite(new Mur(this), i, j);
                        break; // mur
                    case 'h':
                        addEntite(hector, i, j);
                        break; // héros
                    case 'b':
                        addEntite(new Bombe(this), i, j);
                        NbBombe++;
                        break; // bombe
                    case '1': // Haut d'une colonne rouge
                        ColonneHautR C_hr = new ColonneHautR(this);
                        addEntite(C_hr, i, j);
                        Colonne.getInstanceR().addEntiteDynamique(C_hr);
                        Colonne.getInstanceR().setPosition(Direction.bas);
                        break;
                    case '2': // Milieu d'une colonne rouge
                        ColonneMilieuR C_mr = new ColonneMilieuR(this);
                        addEntite(C_mr, i, j);
                        Colonne.getInstanceR().addEntiteDynamique(C_mr);
                        Colonne.getInstanceR().setPosition(Direction.bas);
                        break;
                    case '3': // Bas d'une colonne rouge
                        ColonneBasR C_br = new ColonneBasR(this);
                        addEntite(C_br, i, j);
                        Colonne.getInstanceR().addEntiteDynamique(C_br);
                        Colonne.getInstanceR().setPosition(Direction.bas);
                        break;
                    case '4': // Haut d'une colonne Bleue
                        ColonneHautB C_hb = new ColonneHautB(this);
                        addEntite(C_hb, i, j);
                        Colonne.getInstanceB().addEntiteDynamique(C_hb);
                        Colonne.getInstanceB().setPosition(Direction.haut);
                        break;
                    case '5': // Milieu d'une colonne Bleue
                        ColonneMilieuB C_mb = new ColonneMilieuB(this);
                        addEntite(C_mb, i, j);
                        Colonne.getInstanceB().addEntiteDynamique(C_mb);
                        Colonne.getInstanceB().setPosition(Direction.haut);
                        break;
                    case '6': // Bas d'une colonne Bleue
                        ColonneBasB C_bb = new ColonneBasB(this);
                        addEntite(C_bb, i, j);
                        Colonne.getInstanceB().addEntiteDynamique(C_bb);
                        Colonne.getInstanceB().setPosition(Direction.haut);
                        break;
                    case 'c': // Corde
                        addEntite(new Corde(this), i, j);
                        break;
                    case 'X': // IA
                        modele.plateau.IA b = new modele.plateau.IA(this); 
                        addEntite(b, i, j);
                        IA.getInstance().addEntiteDynamique(b);
                        break;
                }
            }
        }
        
        ordonnanceur.add(Colonne.getInstanceR());
        ordonnanceur.add(Colonne.getInstanceB());
        ordonnanceur.add(IA.getInstance());

    }

    /** 
     * Permet d'ajouter une entité dans la grille
     * @param e Entité
     * @param x position abscisse
     * @param y position ordonnée
     */
    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y].add(e);
        //System.out.println(grilleEntites[x][y].get(0));
        map.put(e, new Point(x, y));
    }
    
    /** 
     * Permet par exemple a une entité de percevoir sont environnement proche et de définir sa stratégie de déplacement
     * @param e Entité
     * @param d Direction
     * @return l'entité présente dans la direction donnée
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);   
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    

    /**
     * Surcharge pour regarder en diagonale
     * @param e Entité
     * @param dg Direction (gauche ou droite)
     * @param hb Direction (bas ou haut)
     * @return l'entité présente dans une des diagonales
     */
    public Entite regarderDansLaDirection(Entite e, Direction dg, Direction hb) { 
        Point positionEntite = map.get(e); 
        Entite tmp = objetALaPosition(calculerPointCible(positionEntite, hb));
        Point positionEntiteCote = map.get(tmp);
        return objetALaPosition(calculerPointCible(positionEntiteCote, dg));
    }
    
    /** 
     * Réalise le déplacement d'une entité avec la direction
     * @param e Entité
     * @param d Direction
     * @return true si le déplacement de l'entité est autorisé (pas de mur ou autre entité), false sinon
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        
        if (e instanceof Heros)
            directionCouranteHeros = d;
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || objetALaPosition(pCible).peutPermettreDeMonterDescendre() || objetALaPosition(pCible).peutEtreEcrase() ||objetALaPosition(pCible).peutEtreRamasse())) {
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

        // Gère les différents cas de déplacement
        if (retour) {
            // Gère les cas ou une entité va sur une corde OU si un smick va sur une bombe
            if ((objetALaPosition(pCible) instanceof Corde || (objetALaPosition(pCible) instanceof Bombe && objetALaPosition(pCourant) instanceof modele.plateau.IA)) /*&& grilleEntites[pCible.x][pCible.y].size() == 1*/) {
                deplacerEntite(pCourant, pCible, e, true);
            } 
            // Gère les écrasements des tuyaux 
            else if ((objetALaPosition(pCible) instanceof Heros || objetALaPosition(pCible) instanceof modele.plateau.IA) && objetALaPosition(pCourant) instanceof modele.plateau.Colonne && objetALaPositionSuivante(pCible, d) instanceof Mur) {
                supprimerDeplacerEntite(pCourant, pCible, e);
            }
            // Gère le cas pour monter à l'aide d'un tuyau
            else if ((objetALaPosition(pCible) instanceof Heros || objetALaPosition(pCible) instanceof modele.plateau.IA) && objetALaPosition(pCourant) instanceof modele.plateau.Colonne && !(objetALaPositionSuivante(pCible, d) instanceof Mur)) {
                monterDeplacerEntite(pCourant, pCible, e, d);
            }
            // Gère le cas ou un IA va sur la case du Heros (il meurt)
            else if (objetALaPosition(pCible) instanceof Heros && objetALaPosition(pCourant) instanceof modele.plateau.IA) {
                supprimerDeplacerEntite(pCourant, pCible, e);
            }
            // Gère le cas ou le Heros va sur la case d'un IA (le héros meurt)
            else if (objetALaPosition(pCible) instanceof modele.plateau.IA && objetALaPosition(pCourant) instanceof Heros) {
                SupprimerEntite(pCourant, pCible, e);
            }
            // Gère le cas ou le héros vas sur une bombe (il la ramasse)
            else if (objetALaPosition(pCible) instanceof Bombe) {
                NbBombe--;
                deplacerEntite(pCourant, pCible, e);
            }
            // Gère tous les autres cas
            else {
                deplacerEntite(pCourant, pCible, e);
            }
        }
        
        return retour;
    }
    
    /** 
     * Calcul le point cible
     * @param pCourant Point
     * @param d Direction
     * @return le point cible en fonction de la direction donnée
     */
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

    
    
    /** 
     * Réalise le déplacement d'une entité avec la future position
     * @param pCourant Point représentant la position courante de l'entité
     * @param pCible Point représentant la position cible de l'entité
     * @param Entite Entité
     */
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        if (grilleEntites[pCourant.x][pCourant.y].size() == 2) {
            grilleEntites[pCourant.x][pCourant.y].remove(1);
        } 
        else grilleEntites[pCourant.x][pCourant.y].clear();
        grilleEntites[pCible.x][pCible.y].clear();
        grilleEntites[pCible.x][pCible.y].add(e);
        map.put(e, pCible);
    }
     
    /** 
     * Surchage de la fonction qui réalise le déplacement pour gérer le déplacement sur une corde
     * @param pCourant Point représentant la position courante de l'entité
     * @param pCible Point représentant la position cible de l'entité
     * @param Entite Entité
     * @param Corde Booléan à true si on est sur une corde
     */
    private void deplacerEntite(Point pCourant, Point pCible, Entite e, Boolean Corde) {
        if (Corde)
            if (grilleEntites[pCible.x][pCible.y].size() == 2 && grilleEntites[pCible.x][pCible.y].get(1) instanceof Heros){
                grilleEntites[pCourant.x][pCourant.y].remove(1);
                grilleEntites[pCible.x][pCible.y].remove(1);
                HerosDead = true;
            }
            else if (grilleEntites[pCible.x][pCible.y].size() == 2 && grilleEntites[pCible.x][pCible.y].get(1) instanceof modele.plateau.IA){
                grilleEntites[pCourant.x][pCourant.y].remove(1);
                grilleEntites[pCible.x][pCible.y].remove(1);
                HerosDead = true;
            }
            else if (grilleEntites[pCourant.x][pCourant.y].size() == 2)
                grilleEntites[pCourant.x][pCourant.y].remove(1);
            else grilleEntites[pCourant.x][pCourant.y].clear();
        grilleEntites[pCible.x][pCible.y].add(e);
        map.put(e, pCible);
    }

    
    /** 
     * Réalise le déplacement d'une entité (Supprime l'élément à la position pCible avant de se déplacer)
     * @param pCourant Point représentant la position courante de l'entité
     * @param pCible Point représentant la position cible de l'entité
     * @param Entite Entité
     */
    private void supprimerDeplacerEntite(Point pCourant, Point pCible, Entite e) {
        map.remove(objetALaPosition(pCible));
        if(objetALaPosition(pCible) instanceof modele.plateau.IA){ // Ecrasement d'un IA
            IA.getInstance().RemEntiteDynamique((EntiteDynamique)objetALaPosition(pCible));
        }
        if(objetALaPosition(pCible) instanceof Heros){ // Ecrasement du héros
            Controle4Directions.getInstance().RemEntiteDynamique((EntiteDynamique)objetALaPosition(pCible));
            Gravite.getInstance().RemEntiteDynamique((EntiteDynamique)objetALaPosition(pCible));
            HerosDead = true;
        }
        grilleEntites[pCible.x][pCible.y].clear();
        grilleEntites[pCible.x][pCible.y].add(e);
        grilleEntites[pCourant.x][pCourant.y].clear();
        map.put(e, pCible);
    }
    
    /** 
     * Supprime l'entité à la position courante (on l'utilise uniquement dans le cas ou le héros se déplace sur un IA)
     * @param pCourant Point représentant la position courante de l'entité
     * @param pCible Point représentant la position cible de l'entité
     * @param Entite Entité
     */
    private void SupprimerEntite(Point pCourant, Point pCible, Entite e) {
        map.remove(objetALaPosition(pCourant));
        HerosDead = true;
        grilleEntites[pCourant.x][pCourant.y].clear();
    }
    
    /** 
     * Réalise le déplacement d'une entité (Déplace l'élément à la position pCible avant de se déplacer)
     * @param pCourant Point représentant la position courante de l'entité
     * @param pCible Point représentant la position cible de l'entité
     * @param Entite Entité
     * @parem d Direction
     */
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

    /** 
     * Indique si p est contenu dans la grille
     * @param p Point représentant la position courante de l'entité
     * @return true si le point est contenu dans la grille, false sinon
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    /** 
     * @param p Point
     * @return l'entité présente au point p
     */
    private Entite objetALaPosition(Point p) {
        Entite retour = null;     
        if (contenuDansGrille(p)) {
            if (grilleEntites[p.x][p.y].isEmpty())
                retour = null;
            else retour = grilleEntites[p.x][p.y].get(0);
        }     
        return retour;
    }
    
    /** 
     * @param p Point
     * @param d Directipn 
     * @return l'entité présente à coté du Point p en fonction de la direction donnée
     */
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

    /**
     *
     * @return l'instance d'ordonnanceur
     */
    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
    
    /**
     *
     * @return l'attribut GameOver
     */
    public boolean getGameOver() {
        return GameOver;
    }
    
    /**
     * Permet de changer la valeur de GameOver
     * @param b true si il y a GameOver, false sinon
     */
    public void setGameOver(boolean b) {
        GameOver = b;
    }
    
    /**
     *
     * @return l'attribut HerosDead
     */
    public boolean getHerosDead() {
        return HerosDead;
    }
    
    /**
     * Vérifie si le jeu est terminé
     */
    public void ConditionFin() {
        if (NbBombe == 0 || HerosDead) {
            reset();
        }
    }
}
