package sample;


public class EnodeB extends Cabinet {
    private static final int TECHNOLOGY = 4;
    private String mrbtsId;
    private int bw, mimo;
    private Hardware hardware;
    private String tac, ipIdentifier, manIp, s1Ip, secIp, secGw;
    private boolean carrierAggregation;

    @Override
    public int getTechnology() {
        return TECHNOLOGY;
    }

    @Override
    public void setControllerId(String controllerId) {
        this.mrbtsId = controllerId;
    }

    @Override
    public String getControllerId() {
        return this.mrbtsId;
    }

    @Override
    public void setNodeId(String nodeId) {
        this.mrbtsId = nodeId;
    }

    @Override
    public String getNodeId() {
        return this.mrbtsId;
    }

    @Override
    protected void setTxMode(String mode) {
        txMode = " FULL IP";
    }

    @Override
    protected void generateUniqueName() {
        this.uniqueName = "4G" +
                "_" +
                this.key;
    }

    @Override
    protected void generateKey() {
        this.key = this.mrbtsId;
    }

    @Override
    protected void generateProperties() {
        this.properties =
                this.cellIdentifier +
                        "__" +
                        this.ipIdentifier +
                        "__" +
                        this.bw +
                        "__" +
                        this.mimo +
                        "__" +
                        this.tac +
                        "__" +
                        this.version.replace("_", "-") +
                        "__" +
                        this.carrierAggregation;
    }

    @Override
    protected void extractProperties() {
        String[] parts = properties.split("__");
        this.setCellIdentifier(parts[0]);
        this.setIpIdentifier(parts[1]);
        this.setBw(Integer.valueOf(parts[2]));
        this.setMimo(Integer.valueOf(parts[3]));
        this.setTac(parts[4]);
        this.setVersion(parts[5]);
        this.carrierAggregation = Boolean.valueOf((parts[6]));
        this.setTxMode("Full IP");
    }

    @Override
    public void setConfiguration(NodeConfiguration sectorNodeConfiguration) {
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
    public void finishProperties() {
        this.generateKey();
        this.generateUniqueName();
        this.findCodeAndRegion();
        this.generateCellIdentifier();
        this.generateIpIdentifier();
        this.generateProperties();
    }

    @Override
    protected void generateCellIdentifier() {
        this.cellIdentifier = this.numberOfCells +
                "." +
                this.numberOfOnAirCells;
    }

    @Override
    protected void extractCellsFromIdentifier() {
        String[] parts = cellIdentifier.split("\\.");
        this.numberOfCells = Integer.valueOf(parts[0]);
        this.numberOfOnAirCells = Integer.valueOf(parts[1]);
    }

    @Override
    protected void setNumberOfSectors() {
        numberOfSectors = this.numberOfCells;
    }

    private void generateIpIdentifier() {
        this.ipIdentifier = this.manIp +
                "-" +
                this.s1Ip +
                "-" +
                this.secIp +
                "-" +
                this.secGw;
    }

    private void extractIpsFromIdentifier() {
        String[] parts = ipIdentifier.split("-");
        manIp = parts[0];
        s1Ip = parts[1];
        secIp = parts[2];
        secGw = parts[3];
    }


    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(sample.Hardware hardware) {
        this.hardware = hardware;
    }

    public void setIpIdentifier(String ipIdentifier) {
        this.ipIdentifier = ipIdentifier;
        extractIpsFromIdentifier();
    }

    String getManIp() {
        return manIp;
    }

    public void setManIp(String manIp) {
        this.manIp = manIp;
    }

    String getS1Ip() {
        return s1Ip;
    }

    public void setS1Ip(String s1Ip) {
        this.s1Ip = s1Ip;
    }

    String getSecIp() {
        return secIp;
    }

    public void setSecIp(String secIp) {
        this.secIp = secIp;
    }

    String getSecGw() {
        return secGw;
    }

    public void setSecGw(String secGw) {
        this.secGw = secGw;
    }

    public void setENodeBId(String eNodeBId) {
        this.mrbtsId = eNodeBId;
    }

    public void setNumberOfOnAirCells(int numberOfOnAirCells, int onAirSuperCells) {
        this.numberOfOnAirCells = (3 * (this.numberOfCells - onAirSuperCells) - numberOfOnAirCells) / 2 + onAirSuperCells;
    }

    String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }

    public String getCellIdentifier() {
        return cellIdentifier;
    }

    int getBw() {
        return bw;
    }

    public void setBw(int bw) {
        this.bw = bw;
    }

    String getMrbtsId() {
        return mrbtsId;
    }

    public int getMimo() {
        return mimo;
    }

    public void setMimo(int mimo) {
        this.mimo = mimo;
    }

    public boolean isCarrierAggregation() {
        return carrierAggregation;
    }

    public void setCarrierAggregation(int carrierAggregation) {
        this.carrierAggregation = carrierAggregation == 1;
    }
}
