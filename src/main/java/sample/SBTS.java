package sample;

import Helpers.Utils;

public class SBTS extends Cabinet {
    private static final int TECHNOLOGY = 1;
    private String sbtsId;
    private String bscName;
    private String bscId;
    private String bcfId;
    private String rncId;
    private String wbtsId;
    private int numberOfCCCHs;
    private int numberOfR99;
    private int numberOfLCGs;
    private int numberOfLTECells;
    private int numberOfOnAirLTECells;
    private String uPower, u9Power;


    @Override
    public String getSbtsId() {
        return sbtsId;
    }

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
        this.properties = this.version
                + "__" +
                this.bscName
                + "__" +
                this.bscId
                + "__" +
                this.bcfId
                + "__" +
                this.rncId
                + "__" +
                this.wbtsId
                + "__" +
                this.numberOfR99
                + "__" +
                this.numberOfCCCHs
                + "__" +
                this.uPower
                + "__" +
                this.u9Power
                + "__" +
                this.numberOfLCGs
                + "__" +
                this.cellIdentifier

        ;
    }

    @Override
    public void finishProperties() {
        this.generateKey();
        this.generateUniqueName();
        this.findCodeAndRegion();
        this.generateCellIdentifier();
        this.generateProperties();
    }

    @Override
    protected void extractProperties() {
        String[] parts = properties.split("__");
        this.setVersion(parts[0]);
        this.setBscName(parts[1]);
        this.setBscId(parts[2]);
        this.setBcfId(parts[3]);
        this.setRncId(parts[4]);
        this.setWbtsId(parts[5]);
        this.setNumberOfR99(Integer.parseInt(parts[6]));
        this.setNumberOfCCCHs(Integer.parseInt(parts[7]));
        this.uPower = (parts[8]);
        this.u9Power = (parts[9]);
        this.setNumberOfLCGs(Integer.parseInt(parts[10]));
        this.setCellIdentifier(parts[11]);
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
        this.cellIdentifier = this.numberOfLTECells +
                "." +
                this.numberOfOnAirLTECells;
    }

    @Override
    protected void extractCellsFromIdentifier() {
        String[] parts = cellIdentifier.split("\\.");
        this.numberOfLTECells = Integer.valueOf(parts[0]);
        this.numberOfOnAirLTECells = Integer.valueOf(parts[1]);
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

    public String getBscName() {
        return bscName;
    }

    public void setBscName(String bscName) {
        this.bscName = bscName;
    }

    public String getBscId() {
        return bscId;
    }

    public void setBscId(String bscId) {
        this.bscId = bscId;
    }

    public String getBcfId() {
        return bcfId;
    }

    public void setBcfId(String bcfId) {
        this.bcfId = bcfId;
    }

    public String getRncId() {
        return rncId;
    }

    public void setRncId(String rncId) {

        try {
            this.rncId = rncId.substring(rncId.length() - 2);
        } catch (NullPointerException e) {
            this.rncId = null;
            System.out.println("No RNC ID for mrbts " + sbtsId);
        } catch (StringIndexOutOfBoundsException s) {
            this.rncId = rncId;
        }
    }

    public String getWbtsId() {
        return wbtsId;
    }

    public void setWbtsId(String wbtsId) {
        this.wbtsId = wbtsId;
    }

    public int getNumberOfCCCHs() {
        return numberOfCCCHs;
    }

    public void setNumberOfCCCHs(int numberOfCCCHs) {
        this.numberOfCCCHs = numberOfCCCHs;
    }

    public int getNumberOfR99() {
        return numberOfR99;
    }

    public void setNumberOfR99(int numberOfR99) {
        this.numberOfR99 = numberOfR99;
    }

    String getU9Power() {
        return u9Power;
    }

    String getuPower() {
        return uPower;
    }

    public void setU9Power(int u900Power, int vam) {
        try {
            this.u9Power = String.valueOf(Utils.convertPower(u900Power, vam));
        } catch (Exception e) {
            e.printStackTrace();
            this.u9Power = "0";
        }
    }


    public void setuPower(int power, int vam) {
        try {
            this.uPower = String.valueOf(Utils.convertPower(power, vam));
        } catch (Exception e) {
            e.printStackTrace();
            this.uPower = "0";
        }
    }

    public int getNumberOfLCGs() {
        return numberOfLCGs;
    }

    public void setNumberOfLCGs(int numberOfLCGs) {
        this.numberOfLCGs = numberOfLCGs;
    }

    public int getNumberOfLTECells() {
        return numberOfLTECells;
    }

    public void setNumberOfLTECells(int numberOfLTECells) {
        this.numberOfLTECells = numberOfLTECells;
    }

    public int getNumberOfOnAirLTECells() {
        return numberOfOnAirLTECells;
    }

    public void setNumberOfOnAirLTECells(int numberOfOnAirCells, int onAirSuperCells) {
        this.numberOfOnAirLTECells = (3 * (this.numberOfLTECells - onAirSuperCells) - numberOfOnAirCells) / 2 + onAirSuperCells;
    }
}
