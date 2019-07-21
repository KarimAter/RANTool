package sample;

import java.util.ArrayList;

public class LHardware {
    int FBBA, FBBC, FRGT, FSMF, FSPD, FTIF, FXEB, FXED;
    String rFModuleIdentifier, sModuleIdentifier;
    public String rfString, smString;
    private String week;
    ArrayList<HwItem> hwItems = new ArrayList<>();

    public LHardware() {

    }

    public void buildHWText() {
        extractRfModuleString();
        extractSModuleString();
    }

    private void extractSModuleString() {
        smString = "";
        if (FSMF > 0)
            smString = FSMF + " FSMF ";
        if (FBBA > 0)
            smString = smString + " " + FBBA + "FBBA ";
        if (FBBC > 0)
            smString = smString + " " + FBBC + "FBBC ";
    }

    private void extractRfModuleString() {
        rfString = "";
        if (FRGT > 0)
            rfString = FRGT + "FRGT ";
        if (FXEB > 0)
            rfString = rfString + " " + FXEB + "FXEB ";
        if (FXED > 0)
            rfString = rfString + " " + FXED + "FXED ";
    }

    public String getrFModuleIdentifier() {
        return rFModuleIdentifier;
    }

    public String getsModuleIdentifier() {
        return sModuleIdentifier;
    }

    public void set4GIdentifiers() {
        setRfModuleIdentifier();
        setSmoduleIdentifier();
    }

    private void setRfModuleIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(FRGT);
        builder.append(".");
        builder.append(FXEB);
        builder.append(".");
        builder.append(FXED);
        rFModuleIdentifier = builder.toString();
    }

    private void setSmoduleIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(FSMF);
        builder.append(".");
        builder.append(FBBA);
        builder.append(".");
        builder.append(FBBC);
        sModuleIdentifier = builder.toString();
    }

    public void setRfModIdentifier(String rFModuleIdentifier) {
        this.rFModuleIdentifier = rFModuleIdentifier;
        extractHwFromRfIdentifier();
    }

    private void extractHwFromRfIdentifier() {
        String[] parts = this.rFModuleIdentifier.split("\\.");
        FRGT = Integer.valueOf(parts[0]);
        FXEB = Integer.valueOf(parts[1]);
        FXED = Integer.valueOf(parts[2]);
        extractRfModuleString();
        hwItems.add(new HwItem(rfString));
    }


    public void setSysModIdentifier(String sModuleIdentifier) {
        this.sModuleIdentifier = sModuleIdentifier;
        extractHwFromSmIdentifier();
    }

    private void extractHwFromSmIdentifier() {
        String[] parts = this.sModuleIdentifier.split("\\.");
        FSMF = Integer.valueOf(parts[0]);
        FBBA = Integer.valueOf(parts[1]);
        FBBC = Integer.valueOf(parts[2]);
        extractSModuleString();
        hwItems.add(new HwItem(smString));
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}

