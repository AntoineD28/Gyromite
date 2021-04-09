package modele.deplacements;

import modele.plateau.EntiteDynamique;

import java.util.ArrayList;

/**
Tous les déplacement sont déclenchés par cette classe (gravité, controle clavier, IA, etc.)
 */
public abstract class RealisateurDeDeplacement {

    protected ArrayList<EntiteDynamique> lstEntitesDynamiques = new ArrayList<EntiteDynamique>();

    protected abstract boolean realiserDeplacement();

    /**
     * Permet d'ajouter une Entitée Dynamique à la liste
     * @param ed Entitée Dynamique
     */
    public void addEntiteDynamique(EntiteDynamique ed) {lstEntitesDynamiques.add(ed);};

    /**
     * Permet de supprimer une Entitée Dynamique de la liste
     * @param ed Entitée Dynamique
     */
    public void RemEntiteDynamique(EntiteDynamique ed) {lstEntitesDynamiques.remove(ed);};

    /**
     * On vide la liste des Entitées Dynamiques
     */
    public void reset() {lstEntitesDynamiques.clear();};
    
    /**
     * @param ed Entitée Dynamique
     * @return la position de ed dans la liste 
     */
    public int GetEntiteDynamique(EntiteDynamique ed) {return lstEntitesDynamiques.indexOf(ed);};
    
    /**
     *
     * @return la taille de la liste d'éntitée Dynamique
     */
    public int GetLengthListe() {return lstEntitesDynamiques.size();};
    
    /**
     * @return la liste d'entitée Dynamique inversée
     */
    public ArrayList<EntiteDynamique> reverseLst(){
        ArrayList<EntiteDynamique> tmp = new ArrayList<EntiteDynamique>();
        for (int i=lstEntitesDynamiques.size()-1; i>=0; i--){
            tmp.add(lstEntitesDynamiques.get(i));
        }
        return tmp;
    }
    
}
