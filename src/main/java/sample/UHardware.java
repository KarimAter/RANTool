package sample;


import java.util.ArrayList;

public class UHardware {
    int FBBA, FRGC, FRGD, FRGF, FRGL, FRGM, FRGP, FRGT, FRGU, FRGX,
            FSMB, FSMD, FSME, FSMF, FTIA, FTIB, FTIF, FTPB, FXDA, FXDB;
    String rfModIdentifier, sysModIdentifier, txModIdentifier;
    public String rfString, smString, txString;
    private String week;
    ArrayList<HwItem> hwItems = new ArrayList<>();

    public UHardware() {
        set3GIdentifiers();
        buildHWText();
    }


    void buildHWText() {
        extractRfModuleString();
        extractSModuleString();
        extractTxModString();
    }

    private void extractTxModString() {
        txString = "";
        if (FTIA > 0)
            txString = FTIA + "FTIA";
        if (FTIB > 0)
            txString = txString + " " + FTIB + "FTIB";
        if (FTIF > 0)
            txString = txString + " " + FTIF + "FTIF";
        if (FTPB > 0)
            txString = txString + " " + FTPB + "FTPB";

    }

    private void extractSModuleString() {
        smString = "";
        if (FSMB > 0)
            smString = FSMB + " FSMB ";
        if (FSMD > 0)
            smString = smString + " " + FSMD + "FSMD ";
        if (FSME > 0)
            smString = smString + " " + FSME + "FSME ";
        if (FSMF > 0)
            smString = smString + " " + FSMF + "FSMF ";
        if (FBBA > 0)
            smString = smString + " " + FBBA + "FBBA ";
    }

    private void extractRfModuleString() {
        rfString = "";
        if (FRGC > 0)
            rfString = FRGC + "FRGC ";
        if (FRGD > 0)
            rfString = rfString + " " + FRGD + "FRGD ";
        if (FRGF > 0)
            rfString = rfString + " " + FRGF + "FRGF ";
        if (FRGL > 0)
            rfString = rfString + " " + FRGL + "FRGL ";
        if (FRGM > 0)
            rfString = rfString + " " + FRGM + "FRGM ";
        if (FRGP > 0)
            rfString = rfString + " " + FRGP + "FRGP ";
        if (FRGT > 0)
            rfString = rfString + " " + FRGT + "FRGT ";
        if (FRGU > 0)
            rfString = rfString + " " + FRGU + "FRGU ";
        if (FRGX > 0)
            rfString = rfString + " " + FRGX + "FRGX ";
        if (FXDA > 0)
            rfString = rfString + " " + FXDA + "FXDA ";
        if (FXDB > 0)
            rfString = rfString + " " + FXDB + "FXDB ";
    }

    public String getRfModIdentifier() {
        return rfModIdentifier;
    }

    public String getSysModIdentifier() {
        return sysModIdentifier;
    }

//        public double getsModExtIdentifier() {
//            return sModExtIdentifier;
//        }

    public String getTransmissionModuleIdentifier() {
        return txModIdentifier;
    }

    void set3GIdentifiers() {
        setRfModuleIdentifier();
        setSmoduleIdentifier();
        setTxModuleIdentifier();
    }

    private void setRfModuleIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(FRGC);
        builder.append(".");
        builder.append(FRGD);
        builder.append(".");
        builder.append(FRGF);
        builder.append(".");
        builder.append(FRGL);
        builder.append(".");
        builder.append(FRGM);
        builder.append(".");
        builder.append(FRGP);
        builder.append(".");
        builder.append(FRGT);
        builder.append(".");
        builder.append(FRGU);
        builder.append(".");
        builder.append(FRGX);
        builder.append(".");
        builder.append(FXDA);
        builder.append(".");
        builder.append(FXDB);
        rfModIdentifier = builder.toString();
    }

    public void extractHwFromRfIdentifier() {
        String[] parts = this.rfModIdentifier.split("\\.");
        FRGC = Integer.valueOf(parts[0]);
        FRGD = Integer.valueOf(parts[1]);
        FRGF = Integer.valueOf(parts[2]);
        FRGL = Integer.valueOf(parts[3]);
        FRGM = Integer.valueOf(parts[4]);
        FRGP = Integer.valueOf(parts[5]);
        FRGT = Integer.valueOf(parts[6]);
        FRGU = Integer.valueOf(parts[7]);
        FRGX = Integer.valueOf(parts[8]);
        FXDA = Integer.valueOf(parts[9]);
        FXDB = Integer.valueOf(parts[10]);
        extractRfModuleString();
        hwItems.add(new HwItem(rfString));

    }

    private void setSmoduleIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(FSMB);
        builder.append(".");
        builder.append(FSMD);
        builder.append(".");
        builder.append(FSME);
        builder.append(".");
        builder.append(FSMF);
        builder.append(".");
        builder.append(FBBA);
        sysModIdentifier = builder.toString();
    }

    public void extractHwFromSmIdentifier() {
        String[] parts = this.sysModIdentifier.split("\\.");
        FSMB = Integer.valueOf(parts[0]);
        FSMD = Integer.valueOf(parts[1]);
        FSME = Integer.valueOf(parts[2]);
        FSMF = Integer.valueOf(parts[3]);
        FBBA = Integer.valueOf(parts[4]);
        extractSModuleString();
        hwItems.add(new HwItem(smString));

    }

    private void setTxModuleIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(FTPB);
        builder.append(".");
        builder.append(FTIA);
        builder.append(".");
        builder.append(FTIB);
        builder.append(".");
        builder.append(FTIF);
        txModIdentifier = builder.toString();
    }

    public void extractHwFromTxIdentifier() {
        String[] parts = this.txModIdentifier.split("\\.");
        FTPB = Integer.valueOf(parts[0]);
        FTIA = Integer.valueOf(parts[1]);
        FTIB = Integer.valueOf(parts[2]);
        FTIF = Integer.valueOf(parts[3]);
        extractTxModString();
        hwItems.add(new HwItem(txString));

    }

    public void setRfModIdentifier(String rFModIdentifier) {
        this.rfModIdentifier = rFModIdentifier;
        extractHwFromRfIdentifier();
    }

    public void setSysModIdentifier(String sysModIdentifier) {
        this.sysModIdentifier = sysModIdentifier;
        extractHwFromSmIdentifier();
    }

    public String getTxModIdentifier() {
        return txModIdentifier;
    }

    public void setTxModIdentifier(String txModIdentifier) {
        this.txModIdentifier = txModIdentifier;
        extractHwFromTxIdentifier();
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}

