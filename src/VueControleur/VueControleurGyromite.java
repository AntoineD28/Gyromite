package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.Controle4Directions;
import modele.deplacements.Colonne;
import modele.deplacements.Direction;
import modele.plateau.*;

/**
 * Cette classe a deux fonctions : (1) Vue : proposer une représentation
 * graphique de l'application (cases graphiques, etc.) (2) Controleur : écouter
 * les évènements clavier et déclencher le traitement adapté sur le modèle
 * (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {

    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;
    private int xSlide;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoBombe;
    private ImageIcon icoColonneHautB;
    private ImageIcon icoColonneBasB;
    private ImageIcon icoColonneMilieuB;
    private ImageIcon icoColonneHautR;
    private ImageIcon icoColonneBasR;
    private ImageIcon icoColonneMilieuR;
    private ImageIcon icoBot;
    private ImageIcon icoBrique;
    private ImageIcon icoCorde;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)

    
    
    
    /**
     * Constructeur de VueControleurGyromite
     * @param _jeu 
     */
    public VueControleurGyromite(Jeu _jeu) {
        //System.out.println(jeu.SIZE_X);
        sizeX = _jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;
        xSlide = 0;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT:
                        Controle4Directions.getInstance().setDirectionCourante(Direction.gauche);
                        break;
                    case KeyEvent.VK_RIGHT:
                        Controle4Directions.getInstance().setDirectionCourante(Direction.droite);
                        break;
                    case KeyEvent.VK_DOWN:
                        Controle4Directions.getInstance().setDirectionCourante(Direction.bas);
                        break;
                    case KeyEvent.VK_UP:
                        Controle4Directions.getInstance().setDirectionCourante(Direction.haut);
                        break;
                    case KeyEvent.VK_SPACE:
                        Colonne.getInstanceR().setDirectionCourante(Direction.changer);
                        Colonne.getInstanceB().setDirectionCourante(Direction.changer);
                        break;
                }
            }
        });
    }

    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/Vector.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoMur = chargerIcone("Images/case2.png");
        icoBombe = chargerIcone("Images/bombe.png");
        icoColonneHautB = chargerIcone("Images/Colonne_haut_bleu.png");
        icoColonneBasB = chargerIcone("Images/Colonne_bas_bleu.png");
        icoColonneMilieuB = chargerIcone("Images/Colonne_milieu_bleu.png");
        icoColonneHautR = chargerIcone("Images/Colonne_haut_rouge.png");
        icoColonneBasR = chargerIcone("Images/Colonne_bas_rouge.png");
        icoColonneMilieuR = chargerIcone("Images/Colonne_milieu_rouge.png");
        icoBot = chargerIcone("Images/Bot.png");
        icoBrique = chargerIcone("Images/brique.png");
        icoCorde = chargerIcone("Images/corde.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX / 2)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX / 2][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX / 2; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du
     * côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        Direction dirHector = jeu.getDirCouranteHector();
        int x1 = 0;

        if (jeu.getNbBombe() == 0) {
            JFrame frame = new JFrame("Fin de jeu");
            JLabel label = new JLabel("WIN, vous avez ramassé toutes les bombes !", JLabel.CENTER);
            frame.add(label);
            frame.pack();
            frame.setSize(400, 250);
            frame.setLocation(200, 150);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        if (jeu.getHerosDead()) { // Si le heros meurt on ne récupère pas sa position et on reinitialise xSlide
            xSlide = 0;
        } 
        else {
            int posX = jeu.getHectorPos().x;
            if ((posX == 10 || posX == 20) && dirHector == Direction.droite) { // A la 10ème et 20ème case on décale la carte
                xSlide += 10;
            } 
            else if (posX == 9 && dirHector == Direction.gauche) {
                xSlide = 0;
            }
            else if (posX == 19 && dirHector == Direction.gauche){
                xSlide -= 10;   
            }
        }
        
        for (int x = 0; x < sizeX / 2; x++) {
            for (int y = 0; y < sizeY; y++) {
                x1 = x + xSlide ;

                if (jeu.getGrille()[x1][y].isEmpty()) {
                    tabJLabel[x][y].setIcon(icoVide);
                }
                
                if (jeu.getGrille()[x1][y].size() == 1) {
                    if (jeu.getGrille()[x1][y].get(0) instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                        // System.out.println("Héros !");
                        tabJLabel[x][y].setIcon(icoHero);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof Mur) {
                        tabJLabel[x][y].setIcon(icoMur);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof Brique) {
                        tabJLabel[x][y].setIcon(icoBrique);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof Bombe) {
                        tabJLabel[x][y].setIcon(icoBombe);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof IA) {
                        tabJLabel[x][y].setIcon(icoBot);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof Corde) {
                        tabJLabel[x][y].setIcon(icoCorde);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof ColonneHautR) {
                        tabJLabel[x][y].setIcon(icoColonneHautR);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof ColonneMilieuR) {
                        tabJLabel[x][y].setIcon(icoColonneMilieuR);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof ColonneBasR) {
                        tabJLabel[x][y].setIcon(icoColonneBasR);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof ColonneHautB) {
                        tabJLabel[x][y].setIcon(icoColonneHautB);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof ColonneMilieuB) {
                        tabJLabel[x][y].setIcon(icoColonneMilieuB);
                    } else if (jeu.getGrille()[x1][y].get(0) instanceof ColonneBasB) {
                        tabJLabel[x][y].setIcon(icoColonneBasB);
                    }
                }

                if (jeu.getGrille()[x1][y].size() == 2) {
                    if (jeu.getGrille()[x1][y].get(1) instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                        // System.out.println("Héros !");
                        tabJLabel[x][y].setIcon(icoHero);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof Mur) {
                        tabJLabel[x][y].setIcon(icoMur);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof Bombe) {
                        tabJLabel[x][y].setIcon(icoBombe);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof IA) {
                        tabJLabel[x][y].setIcon(icoBot);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof Corde) {
                        tabJLabel[x][y].setIcon(icoCorde);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof ColonneHautR) {
                        tabJLabel[x][y].setIcon(icoColonneHautR);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof ColonneMilieuR) {
                        tabJLabel[x][y].setIcon(icoColonneMilieuR);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof ColonneBasR) {
                        tabJLabel[x][y].setIcon(icoColonneBasR);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof ColonneHautB) {
                        tabJLabel[x][y].setIcon(icoColonneHautB);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof ColonneMilieuB) {
                        tabJLabel[x][y].setIcon(icoColonneMilieuB);
                    } else if (jeu.getGrille()[x1][y].get(1) instanceof ColonneBasB) {
                        tabJLabel[x][y].setIcon(icoColonneBasB);
                    }
                }
            }
        }
    }

    /**
     * Méthode update qui met à jour l'affichage
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
    }
    
}
