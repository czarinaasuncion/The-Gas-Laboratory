
// base class
class Gas {
    private final String name, symbol, electronConfig, realWorldUse;
    private final int atomicNumber;
    private final double atomicMass;

    public Gas(String name, String symbol, int atomicNumber, double atomicMass, String electronConfig, String realWorldUse) {
        this.name = name;
        this.symbol = symbol;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.electronConfig = electronConfig;
        this.realWorldUse = realWorldUse;
    }

    // Getters
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public int getAtomicNumber() { return atomicNumber; }
    public double getAtomicMass() { return atomicMass; }
    public String getElectronConfig() { return electronConfig; }
    public String getRealWorldUse() { return realWorldUse; }
}
