package sample;


import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnodeB extends Cabinet {
    private static final int TECHNOLOGY = 4;
    private String mrbtsId;
    private int bw, mimo;
    private Hardware hardware;
    private String tac, ipIdentifier, manIp, s1Ip, secIp, secGw, manVlan, secVlan;
    private boolean carrierAggregation, sRan;

    @Override
    public String getSbtsId() {
        return sRan ? mrbtsId : "null";
    }

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
                        this.carrierAggregation +
                        "__" +
                        this.sRan;
    }

    @Override
    protected void extractProperties() {
        String[] parts = properties.split("__");
        this.setCellIdentifier(parts[0]);
        this.ipIdentifier = parts[1];
        this.setBw(Integer.valueOf(parts[2]));
        this.setMimo(Integer.valueOf(parts[3]));
        this.setTac(parts[4]);
        this.setVersion(parts[5]);
        this.carrierAggregation = Boolean.valueOf((parts[6]));
        this.sRan = Boolean.valueOf((parts[7]));
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
//        this.generateIpIdentifier();
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

    public void setIpData(String ips, String vlans, String ifs, String secIf, String remote, String s1If, String ifvflink, String ipvlan) {

        // concatenation with ;
        this.ipIdentifier = Stream.of(ipvlan, ifs, secIf, remote, s1If)
                .map(String::valueOf).collect(Collectors.joining(";"));
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(sample.Hardware hardware) {
        this.hardware = hardware;
    }

    String getManIp() {
        return manIp;
    }


    String getS1Ip() {
        return s1Ip;
    }


    String getSecIp() {
        return secIp;
    }


    String getSecGw() {
        return secGw;
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

    public boolean isSran() {
        return sRan;
    }

    public void setSran(int sRan) {
        this.sRan = sRan == 10;
    }

    public String getManVlan() {
        return manVlan;
    }


    public String getSecVlan() {
        return secVlan;
    }


    public String getIpIdentifier() {
        return ipIdentifier;
    }


}
