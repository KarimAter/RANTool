package sample;

import Helpers.Utils;

public class EnodeB {
    String eNodeBRegion, eNodeBId, eNodeBVersion, uniqueName;
    private String eNodeBCode = "";
    private String eNodeBName = "";
    private int eNodeBNumberOfSectors, eNodeBNumberOfCells, eNodeBNumberOfOnAirCells, eNodeBBW, eNodeBMimo, uniqueId;
    private LHardware lHardware;
    private Hardware Hardware;
    private String cellIdentifier, tac, ipIdentifier, manIp, s1Ip, secIp, secGw;


    public sample.Hardware getHardware() {
        return Hardware;
    }

    public void setHardware(sample.Hardware hardware) {
        Hardware = hardware;
    }

    public String getIpIdentifier() {
        return ipIdentifier;
    }

    public void setIpIdentifier(String ipIdentifier) {
        this.ipIdentifier = ipIdentifier;
        extractIpsFromIdentifier();
    }

    public String getManIp() {
        return manIp;
    }

    public void setManIp(String manIp) {
        this.manIp = manIp;
    }

    public String getS1Ip() {
        return s1Ip;
    }

    public void setS1Ip(String s1Ip) {
        this.s1Ip = s1Ip;
    }

    public String getSecIp() {
        return secIp;
    }

    public void setSecIp(String secIp) {
        this.secIp = secIp;
    }

    public String getSecGw() {
        return secGw;
    }

    public void setSecGw(String secGw) {
        this.secGw = secGw;
    }

    public String getENodeBName() {
        return eNodeBName;
    }

    public void setENodeBName(String eNodeBName) {
        this.eNodeBName = eNodeBName;
    }

    public String getENodeBCode() {
        return eNodeBCode;
    }

    public void extractENodeBCode() {
        if (eNodeBName != null) {
            this.eNodeBCode = Utils.extractSiteCode(this.eNodeBName);
        }

    }

    public void setENodeBCode(String eNodeBCode) {
        if (eNodeBCode != null)
            this.eNodeBCode = eNodeBCode;
        else this.eNodeBCode = Utils.extractSiteCode(this.eNodeBName);
    }

    public String getENodeBRegion() {
        return eNodeBRegion;
    }

    public void seteNodeBRegion(String eNodeBRegion) {
        this.eNodeBRegion = eNodeBRegion;
    }

    public void extractENodeBRegion() {
        if (!eNodeBCode.equals("")) {
            String region = eNodeBCode.substring(4);
            if (region.equalsIgnoreCase("UP") || region.equalsIgnoreCase("SI") || region.equalsIgnoreCase("RE")
                    || region.equalsIgnoreCase("DE") || region.equalsIgnoreCase("AL"))
                this.eNodeBRegion = region;
        }
    }

    public String getENodeBId() {
        return eNodeBId;
    }

    public void setENodeBId(String eNodeBId) {
        this.eNodeBId = eNodeBId;
    }

    public String getENodeBVersion() {
        return eNodeBVersion;
    }

    public void setENodeBVersion(String eNodeBVersion) {
        this.eNodeBVersion = eNodeBVersion;
    }

    public int getENodeBNumberOfSectors() {
        return eNodeBNumberOfSectors;
    }

    public void setENodeBNumberOfSectors() {
        this.eNodeBNumberOfSectors = this.eNodeBNumberOfCells;
    }

    public int getENodeBNumberOfCells() {
        return eNodeBNumberOfCells;
    }

    public void setENodeBNumberOfCells(int eNodeBNumberOfCells) {
        this.eNodeBNumberOfCells = eNodeBNumberOfCells;
    }

    public int getENodeBNumberOfOnAirCells() {
        return eNodeBNumberOfOnAirCells;
    }

    public void setENodeBNumberOfOnAirCells(int eNodeBNumberOfOnAirCells, int onAirSuperCells) {
        this.eNodeBNumberOfOnAirCells = (3 * (this.eNodeBNumberOfCells - onAirSuperCells) - eNodeBNumberOfOnAirCells) / 2 + onAirSuperCells;
    }

    public int getENodeBBW() {
        return eNodeBBW;
    }

    public void setENodeBBW(int eNodeBBW) {
        this.eNodeBBW = eNodeBBW;
    }

    public int getENodeBMimo() {
        return eNodeBMimo;
    }

    public void setENodeBMimo(int eNodeBMimo) {
        this.eNodeBMimo = eNodeBMimo;
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac;
    }


    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public void createUniqueName() {
        StringBuilder builder = new StringBuilder();
        builder.append("4G");
        builder.append("_");
        builder.append(eNodeBId);
        this.uniqueName = builder.toString();
    }

    public void finalizeProperties(String week) {
        this.setENodeBNumberOfSectors();
        this.extractENodeBCode();
        this.extractENodeBRegion();
        this.createUniqueName();
//        this.setLHardware(new ENodeBHW(), week);
        this.createCellIdentifier();
        this.createIpIdentifier();

    }

    private void createIpIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.manIp);
        builder.append("_");
        builder.append(this.s1Ip);
        builder.append("_");
        builder.append(this.secIp);
        builder.append("_");
        builder.append(this.secGw);
        this.ipIdentifier = builder.toString();
    }

    private void extractIpsFromIdentifier() {
        String[] parts = ipIdentifier.split("_");
        manIp = parts[0];
        s1Ip = parts[1];
        secIp = parts[2];
        secGw = parts[3];
    }

    private void createCellIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.eNodeBNumberOfCells);
        builder.append(".");
        builder.append(this.eNodeBNumberOfOnAirCells);
        this.cellIdentifier = builder.toString();
    }

    public void extractCellsFromIdentifier() {
        String[] parts = cellIdentifier.split("\\.");
        eNodeBNumberOfCells = Integer.valueOf(parts[0]);
        eNodeBNumberOfOnAirCells = Integer.valueOf(parts[1]);
    }

    public String getCellIdentifier() {
        return cellIdentifier;
    }

    public void setCellIdentifier(String cellIdentifier) {
        this.cellIdentifier = cellIdentifier;
        extractCellsFromIdentifier();
    }

    public int geteNodeBBW() {
        return eNodeBBW;
    }

    public void seteNodeBBW(int eNodeBBW) {
        this.eNodeBBW = eNodeBBW;
    }

    public LHardware getLHardware() {
        return lHardware;
    }

    void setLHardware(ENodeBHW enodeBHW, String weekName) {
        LHardware lHardware = new LHardware();
        if (enodeBHW != null) {
            lHardware.hwItems = enodeBHW.getHwItems();
            for (HwItem hwItem : lHardware.hwItems) {
                switch (hwItem.getUserLabel()) {
                    case "FBBA":
                        lHardware.FBBA++;
                        break;
                    case "FBBC":
                        lHardware.FBBC++;
                        break;
                    case "FRGT":
                        lHardware.FRGT++;
                        break;
                    case "FSMF":
                        lHardware.FSMF++;
                        break;
                    case "FSPD":
                        lHardware.FSPD++;
                        break;
                    case "FTIF":
                        lHardware.FTIF++;
                        break;
                    case "FXEB":
                        lHardware.FXEB++;
                        break;
                    case "FXED":
                        lHardware.FXED++;
                        break;
                }
            }
        }
        this.lHardware = lHardware;
        this.lHardware.setWeek(weekName);
        this.lHardware.set4GIdentifiers();
        this.lHardware.buildHWText();
    }


    public void setLHardware(LHardware lHardware) {
        this.lHardware = lHardware;
    }

}
