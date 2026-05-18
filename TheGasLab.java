import java.util.ArrayList;
import java.util.List;

class TheGasLab {
    //add sa arraylist yung list ng gases
    private List<Gas> gases = new ArrayList<>();

    // para magstart system, method call para mag load yung gases
    public void initializeSystem() {
        loadGases();
    }
//load info for gases in the detail panel
    public void loadGases() {
        // mga details per element, pending
        gases.add(new NonmetalGas("Hydrogen", "H", 1, 1.008, "Lorem ipsum", "Lorem ipsum"));
        gases.add(new NobleGas("Helium", "He", 2, 4.003, "Lorem ipsum", "Lorem ipsum"));
        gases.add(new NonmetalGas("Nitrogen", "N", 7, 14.007, "Lorem ipsum", "Lorem ipsum"));
        gases.add(new NonmetalGas("Oxygen", "O", 8, 15.999,"Lorem ipsum", "Lorem ipsum"));
        gases.add(new NonmetalGas("Fluorine", "F", 9, 18.998, "Lorem ipsum", "Lorem ipsum"));
        gases.add(new NobleGas("Neon", "Ne", 10, 20.180, "Lorem ipsum", "Lorem ipsum"));
        gases.add(new NonmetalGas("Chlorine", "Cl", 17, 34.450, "Lorem ipsum", "Lorem ipsum"));
        gases.add(new NobleGas("Argon", "Ar", 18, 39.948,"Lorem ipsum", "Lorem ipsum"));
        gases.add(new NobleGas("Krypton", "Kr", 36, 83.798,"Lorem ipsum", "Lorem ipsum"));
        gases.add(new NobleGas("Xenon", "Xe", 54, 131.293,"Lorem ipsum", "Lorem ipsum"));
        gases.add(new NobleGas("Radon", "Rn", 86, 222,"Lorem ipsum", "Lorem ipsum"));
    }

    //search element logic
    public Gas findGases(String query) {
        for (Gas g : gases) {
            // iignore yung case if nagsearch, search through symbol or name
            if (g.getName().equalsIgnoreCase(query) || g.getSymbol().equalsIgnoreCase(query)) {
                return g;
            }
        }
        return null;
    }

    //group filter logic
    public List<Gas> filterByGroup(String groupType) {
        // pag naka All Elements dropdown
        if (groupType.equals("All Elements")) return gases;
        
        // kunin elements from arraylist
        List<Gas> filtered = new ArrayList<>();

        // for loop to aid in filtration
        // ano lilitaw pag noble gas or nonmetal
        for (Gas g : gases) {
            if (groupType.equals("Noble Gases") && g instanceof NobleGas) filtered.add(g);
            if (groupType.equals("Nonmetal Gases") && g instanceof NonmetalGas) filtered.add(g);
        }
        return filtered;
    }

    //getter to get all gases
    public List<Gas> getAllGases() {
        return gases;
    }

    // getter and setter for noble gas and nonmetal
    public List<Gas> getGases() {
        return gases;
    }
    public void setGases(List<Gas> gases) {
        this.gases = gases;
    }
}
