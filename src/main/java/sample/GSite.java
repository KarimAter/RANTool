package sample;

import Helpers.Constants;
import Helpers.Utils;

public class GSite {
    private String siteName, siteCode, region, siteBSCName, siteBSCId, siteBCFId, siteVersion, lac, rac, uniqueName;
    private int siteNumberOfBCFs, siteNumberOfTRXs, siteNumberOfSectors, siteNumberOfCells, siteNumberOfDcsCells, siteNumberOfGsmCells,
            siteNumberOfE1s, siteNumberOfOnAirCells, siteNumberOfGTRXs;
    private Constants.gTxMode gSiteTxMode;
    private GHardware gHardware;
    double trxIdentifier, gTrxIdentifier;
    private int uniqueId;

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

    public String getSiteBSCId() {
        return siteBSCId;
    }

    public void setSiteBSCId(String siteBSCId) {
        this.siteBSCId = siteBSCId;
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

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getRac() {
        return rac;
    }

    public void setRac(String rac) {
        this.rac = rac;
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
        this.siteNumberOfGsmCells = siteNumberOfCells - siteNumberOfDcsCells;
    }

    public void finalizeProperties() {
        this.setSiteCode();
        this.setRegion();
        this.setSiteNumberOfGsmCells();
        this.setUniqueName();
        setIdentifiers();
    }

    public double getTrxIdentifier() {
        return trxIdentifier;
    }

    public double getgTrxIdentifier() {
        return gTrxIdentifier;
    }

    private void setIdentifiers() {
        trxIdentifier = siteNumberOfTRXs;
        gTrxIdentifier = siteNumberOfGTRXs;
    }


    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName() {
        StringBuilder builder = new StringBuilder();
        builder.append("2G");
        builder.append(siteName);
        builder.append(siteCode);
        this.uniqueName = builder.toString();
    }

    public void setGHardware(GHardware gHardware) {
        this.gHardware = gHardware;
    }

    public GHardware getGHardware() {
        return gHardware;
    }

    public static class GHardware {
        int ESMB, ESMC, FIQA, FIQB, FSMF, FTIF, FXDA, FXDB, FXEA, FXEB, FXX;
        double rFModuleIdentifier, systemModuleIdentifier, transmissionModuleIdentifier;
        public String rfString, smString, txString;


        public GHardware(int ESMB, int ESMC, int FIQA, int FIQB, int FSMF, int FTIF,
                         int FXDA, int FXDB, int FXEA, int FXEB, int FXX) {
            this.ESMB = ESMB;
            this.ESMC = ESMC;
            this.FIQA = FIQA;
            this.FIQB = FIQB;
            this.FSMF = FSMF;
            this.FTIF = FTIF;
            this.FXDA = FXDA;
            this.FXDB = FXDB;
            this.FXEA = FXEA;
            this.FXEB = FXEB;
            this.FXX = FXX;
            setIdentifiers();
            buildHWText();
        }

        private void buildHWText() {
            getRfModuleString();
            getSModuleString();
            getTxModString();
        }

        private void getSModuleString() {
            smString = "";
            if (ESMB > 0)
                smString = ESMB + " ESMB ";
            if (ESMC > 0)
                smString = smString + " " + ESMC + "ESMC ";
            if (FSMF > 0)
                smString = smString + " " + FSMF + "FSMF ";
        }

        private void getRfModuleString() {
            rfString = "";
            if (FXDA > 0)
                rfString = FXDA + "FXDA ";
            if (FXDB > 0)
                rfString = rfString + " " + FXDB + "FXDB ";
            if (FXEA > 0)
                rfString = rfString + " " + FXEA + "FXEA ";
            if (FXEB > 0)
                rfString = rfString + " " + FXEB + "FXEB ";
            if (FXX > 0)
                rfString = rfString + " " + FXX + "FXX ";
        }

        private void getTxModString() {
            txString = "";
            if (FIQA > 0)
                txString = FIQA + "FIQA";
            if (FIQB > 0)
                txString = txString + " " + FIQB + "FIQB";
            if (FTIF > 0)
                txString = txString + " " + FTIF + "FTIF";
        }

        public double getrFModuleIdentifier() {
            return rFModuleIdentifier;
        }

        public double getSystemModuleIdentifier() {
            return systemModuleIdentifier;
        }

        public double getTransmissionModuleIdentifier() {
            return transmissionModuleIdentifier;
        }

        private void setIdentifiers() {
            rFModuleIdentifier = 0.01 * FXDA + 0.1 * FXDB + FXEA + 10 * FXEB + 100 * FXX;
            systemModuleIdentifier = 0.1 * ESMB + ESMC + 10 * FSMF;
            transmissionModuleIdentifier = 0.1 * FIQA + FIQB + 10 * FTIF;
        }


    }

}
