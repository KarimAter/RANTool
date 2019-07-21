package sample;

public class GSite {
//    private String siteName, siteCode, siteBSCId, region, siteBSCName, siteBCFId, siteVersion, uniqueName;
//    private int siteNumberOfBCFs;
//    private int siteNumberOfTRXs;
//    private int siteNumberOfSectors;
//    private int siteNumberOfCells;
//    private int siteNumberOfDcsCells;
//    private int siteNumberOfGsmCells;
//    private int siteNumberOfE1s;
//    private int siteNumberOfOnAirCells;
//    private int siteNumberOfGTRXs;
//    private int lac;
//    private int rac;
//    private boolean onAir;
//    private Constants.gTxMode gSiteTxMode;
//    private GHardware gHardware;
//    int trxIdentifier, gTrxIdentifier, txModeIdentifier;
//
//
//    public String getSiteName() {
//        return siteName;
//    }
//
//    public void setSiteName(String siteName) {
//        this.siteName = siteName;
//    }
//
//    public String getSiteCode() {
//        return siteCode;
//    }
//
//    private void setSiteCode() {
//        if (siteName != null) {
//            int siteNameLength = this.siteName.length();
//            String siteCode;
//            if (siteName.contains("_X")) {
//                siteCode = siteName.substring(siteNameLength - 8, siteNameLength - 2);
//            } else
//                siteCode = siteName.substring(siteNameLength - 6);
//            this.siteCode = siteCode;
//        }
//    }
//
//    public String getSiteBSCId() {
//        return siteBSCId;
//    }
//
//    public void setSiteBSCId(String siteBSCId) {
//        this.siteBSCId = siteBSCId;
//    }
//
//    public String getSiteBSCName() {
//        return siteBSCName;
//    }
//
//    public void setSiteBSCName(String siteBSCName) {
//        this.siteBSCName = siteBSCName;
//    }
//
//    public String getSiteBCFId() {
//        return siteBCFId;
//    }
//
//    public void setSiteBCFId(String siteBCFId) {
//        this.siteBCFId = siteBCFId;
//    }
//
//    public String getSiteVersion() {
//        return siteVersion;
//    }
//
//    public void setSiteVersion(String siteVersion) {
//        this.siteVersion = siteVersion;
//    }
//
//    public int getSiteNumberOfBCFs() {
//        return siteNumberOfBCFs;
//    }
//
//    public void setSiteNumberOfBCFs(int siteNumberOfBCFs) {
//        this.siteNumberOfBCFs = siteNumberOfBCFs;
//    }
//
//    public int getSiteNumberOfTRXs() {
//        return siteNumberOfTRXs;
//    }
//
//    public void setSiteNumberOfTRXs(int siteNumberOfTRXs) {
//        this.siteNumberOfTRXs = siteNumberOfTRXs;
//    }
//
//    public int getSiteNumberOfSectors() {
//        return siteNumberOfSectors;
//    }
//
//    public void setSiteNumberOfSectors(int siteNumberOfSectors) {
//        this.siteNumberOfSectors = siteNumberOfSectors;
//    }
//
//    public int getSiteNumberOfCells() {
//        return siteNumberOfCells;
//    }
//
//    public void setSiteNumberOfCells(int siteNumberOfCells) {
//        this.siteNumberOfCells = siteNumberOfCells;
//    }
//
//    public int getSiteNumberOfE1s() {
//        return siteNumberOfE1s;
//    }
//
//    public void setSiteNumberOfE1s(int siteNumberOfE1s) {
//        this.siteNumberOfE1s = siteNumberOfE1s;
//    }
//
//    public int getSiteNumberOfOnAirCells() {
//        return siteNumberOfOnAirCells;
//    }
//
//    public void setSiteNumberOfOnAirCells(int siteNumberOfOnAirCells) {
//        this.siteNumberOfOnAirCells = siteNumberOfCells - (siteNumberOfOnAirCells - siteNumberOfCells) / 2;
//    }
//
//    public int getLac() {
//        return lac;
//    }
//
//    public void setLac(int lac) {
//        this.lac = lac;
//    }
//
//    public int getRac() {
//        return rac;
//    }
//
//    public void setRac(int rac) {
//        this.rac = rac;
//    }
//
//    public String getSiteTxMode() {
//        return gSiteTxMode.toString();
//    }
//
//    public void setSiteTxMode(String value) {
//        if (value.equals("65535"))
//            gSiteTxMode = Constants.gTxMode.PACKET_ABIS;
//        else gSiteTxMode = Constants.gTxMode.ATM;
//    }
//
//    public boolean isOnAir() {
//        return onAir;
//    }
//
//    public void setOnAir(int onAir) {
//        if (onAir == 1 && siteNumberOfOnAirCells > 0)
//            this.onAir = true;
//    }
//
//    public int getSiteNumberOfGTRXs() {
//        return siteNumberOfGTRXs;
//    }
//
//    public void setSiteNumberOfGTRXs(int siteNumberOfGTRXs) {
//        this.siteNumberOfGTRXs = siteNumberOfGTRXs;
//    }
//
//    public String getRegion() {
//        return region;
//    }
//
//    private void setRegion() {
//        if (siteCode != null) {
//            String region = siteCode.substring(4);
//            if (region.equalsIgnoreCase("UP") || region.equalsIgnoreCase("SI") || region.equalsIgnoreCase("RE")
//                    || region.equalsIgnoreCase("DE") || region.equalsIgnoreCase("AL"))
//                this.region = region;
//        }
//    }
//
//    public int getSiteNumberOfDcsCells() {
//        return siteNumberOfDcsCells;
//    }
//
//    public void setSiteNumberOfDcsCells(int siteNumberOfDcsCells) {
//        this.siteNumberOfDcsCells = siteNumberOfDcsCells;
//    }
//
//    public int getSiteNumberOfGsmCells() {
//        return siteNumberOfGsmCells;
//    }
//
//    public void setSiteNumberOfGsmCells() {
//        this.siteNumberOfGsmCells = siteNumberOfCells - siteNumberOfDcsCells;
//    }
//
//    public void finalizeProperties() {
//        this.setSiteCode();
//        this.setRegion();
//        this.setSiteNumberOfGsmCells();
//        this.setUniqueName();
//        setIdentifiers();
//    }
//
//    public int getTrxIdentifier() {
//        return trxIdentifier;
//    }
//
//    public int getgTrxIdentifier() {
//        return gTrxIdentifier;
//    }
//
//    public int getTxModeIdentifier() {
//        return txModeIdentifier;
//    }
//
//    private void setIdentifiers() {
//        trxIdentifier = siteNumberOfTRXs;
//        gTrxIdentifier = siteNumberOfGTRXs;
//        txModeIdentifier = setTxModeIdentifier();
//    }
//
//    private int setTxModeIdentifier() {
//        if (gSiteTxMode.toString().equals("ATM"))
//            return 0;
//        else return 1;
//    }
//
//
//    public String getUniqueName() {
//        return uniqueName;
//    }
//
//    public void setUniqueName() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("2G");
//        builder.append(siteName);
//        builder.append(siteCode);
//        this.uniqueName = builder.toString();
//    }
//
//
//    public GHardware getGHardware() {
//        return gHardware;
//    }
//
//    void setGHardware(ArrayList<BCF> bcfs) {
//        GHardware gHardware = new GHardware();
//        for (BCF bcf : bcfs) {
//            ArrayList<HwItem> hwItems = bcf.getHwItems();
//            for (HwItem hwItem : hwItems) {
//                switch (hwItem.getUserLabel()) {
//                    case "ESMB":
//                        gHardware.ESMB++;
//                        break;
//                    case "ESMC":
//                        gHardware.ESMC++;
//                        break;
//                    case "FIQA":
//                        gHardware.FIQA++;
//                        break;
//                    case "FIQB":
//                        gHardware.FIQB++;
//                        break;
//                    case "FSMF":
//                        gHardware.FSMF++;
//                        break;
//                    case "FTIF":
//                        gHardware.FTIF++;
//                        break;
//                    case "FXDA":
//                        gHardware.FXDA++;
//                        break;
//                    case "FXDB":
//                        gHardware.FXDB++;
//                        break;
//                    case "FXEA":
//                        gHardware.FXEA++;
//                        break;
//                    case "FXEB":
//                        gHardware.FXEB++;
//                        break;
//                    case "FXX":
//                        gHardware.FXX++;
//                        break;
//                    case "FXED":
//                        gHardware.FXED++;
//                        break;
//                    case "FXEF":
//                        gHardware.FXEF++;
//                        break;
//                }
//            }
//        }
//
//        this.gHardware = gHardware;
//        gHardware.setIdentifiers();
//        gHardware.buildHWText();
//    }
//
//    public static class GHardware {
//        int ESMB, ESMC, FIQA, FIQB, FSMF, FTIF, FXDA, FXDB, FXEA, FXEB, FXX, FXED, FXEF;
//
//        String rFModuleIdentifier, systemModuleIdentifier, txModuleIdentifier;
//        public String rfString, smString, txString;
//        private String week;
//        ArrayList<HwItem> hwItems=new ArrayList<>();
//
//        public GHardware() {
//        }
//
//        public void buildHWText() {
//            extractRfModuleString();
//            extractSModuleString();
//            extractTxModString();
//        }
//
//        private void extractSModuleString() {
//            smString = "";
//            if (ESMB > 0)
//                smString = ESMB + " ESMB ";
//            if (ESMC > 0)
//                smString = smString + " " + ESMC + "ESMC ";
//            if (FSMF > 0)
//                smString = smString + " " + FSMF + "FSMF ";
//        }
//
//        private void extractRfModuleString() {
//            rfString = "";
//            if (FXDA > 0)
//                rfString = FXDA + "FXDA ";
//            if (FXDB > 0)
//                rfString = rfString + " " + FXDB + "FXDB ";
//            if (FXEA > 0)
//                rfString = rfString + " " + FXEA + "FXEA ";
//            if (FXEB > 0)
//                rfString = rfString + " " + FXEB + "FXEB ";
//            if (FXX > 0)
//                rfString = rfString + " " + FXX + "FXX ";
//            if (FXED > 0)
//                rfString = rfString + " " + FXED + "FXED ";
//        }
//
//        private void extractTxModString() {
//            txString = "";
//            if (FIQA > 0)
//                txString = FIQA + "FIQA";
//            if (FIQB > 0)
//                txString = txString + " " + FIQB + "FIQB";
//            if (FTIF > 0)
//                txString = txString + " " + FTIF + "FTIF";
//        }
//
//        public String getRfModuleIdentifier() {
//            return rFModuleIdentifier;
//        }
//
//        public String getSystemModuleIdentifier() {
//            return systemModuleIdentifier;
//        }
//
//        public String getTxModuleIdentifier() {
//            return txModuleIdentifier;
//        }
//
//        public void setIdentifiers() {
//            setRfModuleIdentifier();
//            setSmIdentifier();
//            setTxModuleIdentifier();
//        }
//
//        public void setRfModuleIdentifier() {
//            StringBuilder builder = new StringBuilder();
//            builder.append(FXDA);
//            builder.append(".");
//            builder.append(FXDB);
//            builder.append(".");
//            builder.append(FXEA);
//            builder.append(".");
//            builder.append(FXEB);
//            builder.append(".");
//            builder.append(FXX);
//            builder.append(".");
//            builder.append(FXED);
//            rFModuleIdentifier = builder.toString();
//
//        }
//
//        public void setRfModuleIdentifier(String rfModuleIdentifier) {
//            this.rFModuleIdentifier = rfModuleIdentifier;
//            extractHwFromRfIdentifier(rFModuleIdentifier);
//        }
//
//        public void extractHwFromRfIdentifier(String rFModuleIdentifier) {
//            String[] parts = rFModuleIdentifier.split("\\.");
//            FXDA = Integer.valueOf(parts[0]);
//            FXDB = Integer.valueOf(parts[1]);
//            FXEA = Integer.valueOf(parts[2]);
//            FXEB = Integer.valueOf(parts[3]);
//            FXX = Integer.valueOf(parts[4]);
//            FXED = Integer.valueOf(parts[5]);
//            extractRfModuleString();
//            hwItems.add(new HwItem(rfString));
//        }
//
//        public void setSmIdentifier() {
//            StringBuilder builder = new StringBuilder();
//            builder.append(ESMB);
//            builder.append(".");
//            builder.append(ESMC);
//            builder.append(".");
//            builder.append(FSMF);
//            systemModuleIdentifier = builder.toString();
//        }
//
//        public void setSmIdentifier(String systemModuleIdentifier) {
//            this.systemModuleIdentifier = systemModuleIdentifier;
//            extractHwFromSmIdentifier(systemModuleIdentifier);
//
//        }
//        public void extractHwFromSmIdentifier(String sMIdentifier) {
//            String[] parts = sMIdentifier.split("\\.");
//            ESMB = Integer.valueOf(parts[0]);
//            ESMC = Integer.valueOf(parts[1]);
//            FSMF = Integer.valueOf(parts[2]);
//            extractSModuleString();
//            hwItems.add(new HwItem(smString));
//        }
//
//        public void setTxModuleIdentifier() {
//            StringBuilder builder = new StringBuilder();
//            builder.append(FIQA);
//            builder.append(".");
//            builder.append(FIQB);
//            builder.append(".");
//            builder.append(FTIF);
//            txModuleIdentifier = builder.toString();
//        }
//
//
//        public void setTxModuleIdentifier(String txModuleIdentifier) {
//            this.txModuleIdentifier = txModuleIdentifier;
//            extractHwFromTxIdentifier(txModuleIdentifier);
//        }
//
//        private void extractHwFromTxIdentifier(String txModuleIdentifier) {
//
//            String[] parts = txModuleIdentifier.split("\\.");
//            FIQA = Integer.valueOf(parts[0]);
//            FIQB = Integer.valueOf(parts[1]);
//            FTIF = Integer.valueOf(parts[2]);
//            extractTxModString();
//            hwItems.add(new HwItem(txString));
//        }
//
//
//        public void setWeek(String week) {
//            this.week = week;
//        }
//
//        public String getWeek() {
//            return week;
//        }
//
//
//    }
}
