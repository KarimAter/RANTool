package sample;

import Helpers.Constants;

public class GSite {
    private String siteName, siteCode, region, siteBSCName, siteBCFId, siteVersion;
    private int siteNumberOfBCFs, siteNumberOfTRXs, siteNumberOfSectors, siteNumberOfCells,siteNumberOfDcsCells,siteNumberOfGsmCells,
            siteNumberOfE1s, siteNumberOfOnAirCells, siteNumberOfGTRXs;
    private Constants.gTxMode gSiteTxMode;
    private GHardware gHardware;


    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    private void setSiteCode() {
        if (siteName != null) {
            int siteNameLength = this.siteName.length();
            String siteCode;
            if (siteName.contains("_X")) {
                siteCode = siteName.substring(siteNameLength - 8, siteNameLength - 2);
            } else
                siteCode = siteName.substring(siteNameLength - 6);
            this.siteCode = siteCode;
        }
    }

    public String getSiteBSCName() {
        return siteBSCName;
    }

    public void setSiteBSCName(String siteBSCName) {
        this.siteBSCName = siteBSCName;
    }

    public String getSiteBCFId() {
        return siteBCFId;
    }

    public void setSiteBCFId(String siteBCFId) {
        this.siteBCFId = siteBCFId;
    }

    public String getSiteVersion() {
        return siteVersion;
    }

    public void setSiteVersion(String siteVersion) {
        this.siteVersion = siteVersion;
    }

    public int getSiteNumberOfBCFs() {
        return siteNumberOfBCFs;
    }

    public void setSiteNumberOfBCFs(int siteNumberOfBCFs) {
        this.siteNumberOfBCFs = siteNumberOfBCFs;
    }

    public int getSiteNumberOfTRXs() {
        return siteNumberOfTRXs;
    }

    public void setSiteNumberOfTRXs(int siteNumberOfTRXs) {
        this.siteNumberOfTRXs = siteNumberOfTRXs;
    }

    public int getSiteNumberOfSectors() {
        return siteNumberOfSectors;
    }

    public void setSiteNumberOfSectors(int siteNumberOfSectors) {
        this.siteNumberOfSectors = siteNumberOfSectors;
    }

    public int getSiteNumberOfCells() {
        return siteNumberOfCells;
    }

    public void setSiteNumberOfCells(int siteNumberOfCells) {
        this.siteNumberOfCells = siteNumberOfCells;
    }

    public int getSiteNumberOfE1s() {
        return siteNumberOfE1s;
    }

    public void setSiteNumberOfE1s(int siteNumberOfE1s) {
        this.siteNumberOfE1s = siteNumberOfE1s;
    }

    public int getSiteNumberOfOnAirCells() {
        return siteNumberOfOnAirCells;
    }

    public void setSiteNumberOfOnAirCells(int siteNumberOfOnAirCells) {
        this.siteNumberOfOnAirCells = siteNumberOfCells - (siteNumberOfOnAirCells - siteNumberOfCells) / 2;
    }

    public String getSiteTxMode() {
        return gSiteTxMode.toString();
    }

    public void setSiteTxMode(String value) {
        if (value.equals("65535"))
            gSiteTxMode = Constants.gTxMode.PACKET_ABIS;
        else gSiteTxMode = Constants.gTxMode.ATM;
    }

    public int getSiteNumberOfGTRXs() {
        return siteNumberOfGTRXs;
    }

    public void setSiteNumberOfGTRXs(int siteNumberOfGTRXs) {
        this.siteNumberOfGTRXs = siteNumberOfGTRXs;
    }

    public String getRegion() {
        return region;
    }

    private void setRegion() {
        if (siteCode != null) {
            String region = siteCode.substring(4);
            if (region.equalsIgnoreCase("UP") || region.equalsIgnoreCase("SI") || region.equalsIgnoreCase("RE")
                    || region.equalsIgnoreCase("DE") || region.equalsIgnoreCase("AL"))
                this.region = region;
        }
    }

    public int getSiteNumberOfDcsCells() {
        return siteNumberOfDcsCells;
    }

    public void setSiteNumberOfDcsCells(int siteNumberOfDcsCells) {
        this.siteNumberOfDcsCells = siteNumberOfDcsCells;
    }

    public int getSiteNumberOfGsmCells() {
        return siteNumberOfGsmCells;
    }

    public void setSiteNumberOfGsmCells() {
        this.siteNumberOfGsmCells = siteNumberOfCells-siteNumberOfDcsCells;
    }

    public void finalizeProperties() {
        this.setSiteCode();
        this.setRegion();
        this.setSiteNumberOfGsmCells();
    }

    public void setGHardware(GHardware gHardware) {
        this.gHardware=gHardware;
    }

    public GHardware getGHardware() {
        return gHardware;
    }
}
