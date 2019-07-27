package sample;

import Helpers.Utils;

public abstract class Cabinet {


    public Cabinet() {
    }

    public static Cabinet nodeProvider(int tech) {
        Cabinet cabinet = null;
        switch (tech) {
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
    protected String uniqueName, code, name, region, controllerId, nodeId, key, properties,
            version, cellIdentifier, txMode;
    protected int onAir, numberOfCells, numberOfOnAirCells;
    protected Hardware hardware;

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

    abstract protected void generateCellIdentifier();

    abstract protected void extractCellsFromIdentifier();

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

}
