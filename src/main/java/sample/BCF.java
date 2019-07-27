package sample;

public class BCF extends Cabinet {
    private static final int TECHNOLOGY = 2;
    private String bscId;
    private String bcfId;
    private String ctrlIp, manIp, usedETP;
    private String lac;
    private String rac;
    private String gConf;
    private String dConf;
    private int numberOfTRXs;
    private String BSCName;
    private int numberOfE1s;
    private int numberOfGTRXs;
    private int numberOfDcsCells;
    private int numberOfGsmCells;

    @Override
    public void finishProperties() {
        this.generateKey();
        this.generateUniqueName();
        this.findCodeAndRegion();
        this.setNumberOfGsmCells();
        this.generateCellIdentifier();
        this.generateProperties();
    }

    @Override
    protected void generateKey() {
        this.key = this.bscId + "_" + this.bcfId;
    }

    @Override
    public int getTechnology() {
        return TECHNOLOGY;
    }

    @Override
    public void setControllerId(String controllerId) {
        this.setBscId(controllerId);
    }

    @Override
    public String getControllerId() {
        return getBscId();
    }

    @Override
    public void setNodeId(String nodeId) {
        this.setBcfId(nodeId);
    }

    @Override
    public String getNodeId() {
        return getBcfId();
    }

    @Override
    protected void generateUniqueName() {
        this.uniqueName = "2G" +
                "_" +
                this.key;
    }


    private void setNumberOfGsmCells() {
        this.numberOfGsmCells = numberOfCells - numberOfDcsCells;
    }

    @Override
    protected void generateCellIdentifier() {
        this.cellIdentifier = String.valueOf(this.numberOfCells) +
                "." +
                this.numberOfOnAirCells +
                "." +
                this.numberOfGsmCells +
                "." +
                this.numberOfDcsCells +
                "." +
                this.onAir;
    }

    @Override
    protected void generateProperties() {
        this.properties =
                this.BSCName.replace("_", " ") +
                        "_" +
                        this.numberOfTRXs +
                        "_" +
                        this.cellIdentifier +
                        "_" +
                        this.txMode +
                        "_" +
                        this.numberOfE1s +
                        "_" +
                        this.numberOfGTRXs +
                        "_" +
                        this.lac +
                        "_" +
                        this.rac +
                        "_" +
                        this.version +
                        "_" +
                        this.ctrlIp +
                        "_" +
                        this.manIp +
                        "_" +
                        this.usedETP +
                        "_" +
                        this.gConf +
                        "_" +
                        this.dConf;
    }


    @Override
    protected void extractProperties() {
        String[] parts = properties.split("_");
        this.setBSCName(parts[0]);
        this.setNumberOfTRXs(Integer.parseInt(parts[1]));
        this.setCellIdentifier(parts[2]);
        this.setTxMode(parts[3]);
        this.setNumberOfE1s(Integer.parseInt(parts[4]));
        this.setNumberOfGTRXs(Integer.parseInt(parts[5]));
        this.setLac(parts[6]);
        this.setRac(parts[7]);
        this.version = parts[8];
        this.ctrlIp = parts[9];
        this.manIp = parts[10];
        this.usedETP = parts[11];
        this.gConf = parts[12];
        this.dConf = parts[13];
    }


    @Override
    protected void extractCellsFromIdentifier() {
        String[] parts = cellIdentifier.split("\\.");
        this.numberOfCells = Integer.valueOf(parts[0]);
        this.numberOfOnAirCells = Integer.valueOf(parts[1]);
        this.numberOfGsmCells = Integer.valueOf(parts[2]);
        this.numberOfDcsCells = Integer.valueOf(parts[3]);
        this.onAir = Integer.valueOf(parts[4]);
    }


    @Override
    public void setNumberOfOnAirCells(int numberOfOnAirCells) {
        this.numberOfOnAirCells = numberOfCells - (numberOfOnAirCells - numberOfCells) / 2;
    }

    @Override
    public void setTxMode(String txMode) {
        if (txMode.equals("65535") || txMode.contains("PACKET"))
            this.txMode = "PACKET ABIS";
        else this.txMode = "ATM";
    }

    public void setKey(String controllerId, String nodeId) {
        this.bscId = controllerId;
        this.bcfId = nodeId;
    }


    public void setOnAir(int onAir) {
        this.onAir = (onAir != 3) & (this.numberOfOnAirCells > 0) ? 1 : 0;
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


    public void setNumberOfE1s(int numberOfE1s) {
        this.numberOfE1s = numberOfE1s;
    }

    int getNumberOfE1s() {
        return numberOfE1s;
    }

    public void setNumberOfGTRXs(int numberOfGTRXs) {
        this.numberOfGTRXs = numberOfGTRXs;
    }

    int getNumberOfGTRXs() {
        return numberOfGTRXs;
    }

    public void setNumberOfDcsCells(int numberOfDcsCells) {
        this.numberOfDcsCells = numberOfDcsCells;
    }

    int getNumberOfDcsCells() {
        return numberOfDcsCells;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    String getLac() {
        return lac;
    }

    public void setRac(String rac) {
        this.rac = rac;
    }

    String getRac() {
        return rac;
    }


    public String getgConf() {
        return gConf;
    }

    public void setgConf(String gConf) {
        this.gConf = gConf;
    }

    public String getdConf() {
        return dConf;
    }

    public void setdConf(String dConf) {
        this.dConf = dConf;
    }


    public String getCtrlIp() {
        return ctrlIp;
    }

    public void setCtrlIp(String ctrlIp) {
        this.ctrlIp = ctrlIp;
    }

    public String getManIp() {
        return manIp;
    }

    public void setManIp(String manIp) {
        this.manIp = manIp;
    }

    public String getUsedETP() {
        return usedETP;
    }

    public void setUsedETP(String usedETP) {
        this.usedETP = usedETP;
    }


    public void setNumberOfTRXs(int numberOfTRXs) {
        this.numberOfTRXs = numberOfTRXs;
    }

    public int getNumberOfTRXs() {
        return numberOfTRXs;
    }

    public void setBSCName(String bscName) {
        this.BSCName = bscName;
    }

    public String getBSCName() {
        return BSCName;
    }

    public int getNumberOfGsmCells() {
        return numberOfGsmCells;
    }

    public void setNumberOfGsmCells(int numberOfGsmCells) {
        this.numberOfGsmCells = numberOfGsmCells;
    }
}
