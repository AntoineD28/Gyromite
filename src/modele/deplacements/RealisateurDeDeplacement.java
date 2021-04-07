package modele.deplacements;

import modele.plateau.EntiteDynamique;

import java.util.ArrayList;

/**
Tous les déplacement sont déclenchés par cette classe (gravité, controle clavier, IA, etc.)
 */
public abstract class RealisateurDeDeplacement {
    protected ArrayList<EntiteDynamique> lstEntitesDynamiques = new ArrayList<EntiteDynamique>();
    protected abstract boolean realiserDeplacement();

    public void addEntiteDynamique(EntiteDynamique ed) {lstEntitesDynamiques.add(ed);};
    public void RemEntiteDynamique(EntiteDynamique ed) {lstEntitesDynamiques.remove(ed);};
     public void reset() {lstEntitesDynamiques.clear();};
    
    public int GetEntiteDynamique(EntiteDynamique ed) {return lstEntitesDynamiques.indexOf(ed);};
    
    public int GetLengthListe() {return lstEntitesDynamiques.size();};
}
