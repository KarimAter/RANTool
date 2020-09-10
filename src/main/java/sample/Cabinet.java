package sample;

import Helpers.Utils;

public abstract class Cabinet {


    Cabinet() {
    }

    public static Cabinet nodeProvider(int tech) {
        Cabinet cabinet = null;
        switch (tech) {
            case 1:
                cabinet = new SBTS();
                break;
            case 2:
                cabinet = new BCF();
                break;

            case 3:
                cabinet = new NodeB();
                break;

            case 4:
                cabinet = new EnodeB();
                break;
        }
        return cabinet;
    }

    // Todo: seperate package for cabinets
    // Todo:generate uniqueName here
    protected String uniqueName, code, name, region, key, properties,
            version, cellIdentifier, txMode;
    protected int onAir, numberOfCells, numberOfOnAirCells, numberOfE1s, numberOfSectors;
    protected Hardware hardware;
    protected NodeConfiguration nodeConfiguration;

    abstract public int getTechnology();

    abstract public void setControllerId(String controllerId);

    abstract public String getControllerId();

    abstract public void setNodeId(String nodeId);

    abstract public String getNodeId();

    abstract protected void setTxMode(String mode);

    abstract protected void generateUniqueName();

    abstract protected void generateKey();

    abstract protected void generateProperties();

    public abstract void finishProperties();

    abstract protected void extractProperties();

    abstract public void setConfiguration(NodeConfiguration nodeConfiguration);

    abstract public NodeConfiguration getNodeConfiguration();

    abstract public String extractConfiguration();

    abstract protected void generateCellIdentifier();

    abstract protected void extractCellsFromIdentifier();

    abstract protected void setNumberOfSectors();

    protected void findCodeAndRegion() {
        this.code = Utils.extractSiteCode(this.name);
        this.region = Utils.extractRegion(this.code);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
        extractProperties();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version != null ? version : " ";
    }

    public int getOnAir() {
        return onAir;
    }

    public void setOnAir(int onAir) {
        this.onAir = onAir;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public int getNumberOfOnAirCells() {
        return numberOfOnAirCells;
    }

    public void setNumberOfOnAirCells(int numberOfOnAirCells) {
        this.numberOfOnAirCells = numberOfOnAirCells;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public int getNumberOfE1s() {
        return numberOfE1s;
    }

    public void setNumberOfE1s(int numberOfE1s) {
        this.numberOfE1s = numberOfE1s;
    }

    public String getCellIdentifier() {
        return cellIdentifier;
    }

    public void setCellIdentifier(String cellIdentifier) {
        this.cellIdentifier = cellIdentifier;
        extractCellsFromIdentifier();
    }

    public String getTxMode() {
        return txMode;
    }

    public int getNumberOfSectors() {
        return numberOfSectors;
    }
}
