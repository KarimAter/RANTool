package sample;

public class SBTS extends Cabinet {
    private static final int TECHNOLOGY = 1;
    private String sbtsId;

    @Override
    public int getTechnology() {
        return TECHNOLOGY;
    }

    @Override
    public void setControllerId(String controllerId) {
        this.sbtsId = controllerId;
    }

    @Override
    public String getControllerId() {
        return this.sbtsId;
    }

    @Override
    public void setNodeId(String nodeId) {
        this.sbtsId = nodeId;
    }

    @Override
    public String getNodeId() {
        return this.sbtsId;
    }

    @Override
    protected void setTxMode(String mode) {
        txMode = " FULL IP";
    }

    @Override
    protected void generateUniqueName() {
        this.uniqueName = "SBTS" +
                "_" +
                this.key;
    }

    @Override
    protected void generateKey() {
        this.key = this.sbtsId;
    }

    @Override
    protected void generateProperties() {
        this.properties = this.version;
    }

    @Override
    public void finishProperties() {
        this.generateKey();
        this.generateUniqueName();
        this.findCodeAndRegion();
        this.generateProperties();
    }

    @Override
    protected void extractProperties() {
//        String[] parts = properties.split("__");
        this.setVersion(this.properties);
    }

    @Override
    public void setConfiguration(NodeConfiguration nodeConfiguration) {

    }

    @Override
    public NodeConfiguration getNodeConfiguration() {
        return null;
    }

    @Override
    public String extractConfiguration() {
        return "";
    }

    @Override
    protected void generateCellIdentifier() {

    }

    @Override
    protected void extractCellsFromIdentifier() {

    }

    @Override
    protected void setNumberOfSectors() {

    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(sample.Hardware hardware) {
        this.hardware = hardware;
    }
}
