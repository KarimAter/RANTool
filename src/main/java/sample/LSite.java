package sample;

import Helpers.Utils;

public class LSite {
    String eNodeBRegion, eNodeBId, eNodeBVersion, tac, uniqueName;
    private String eNodeBCode = "";
    private String eNodeBName = "";
    private int eNodeBNumberOfSectors, eNodeBNumberOfCells, eNodeBNumberOfOnAirCells, eNodeBBW, eNodeBMimo, uniqueId;
    private LHardware lHardware;
    double bwIdentifier;

    public String getENodeBName() {
        return eNodeBName;
    }

    public void setENodeBName(String eNodeBName) {
        this.eNodeBName = eNodeBName;
    }

    public String getENodeBCode() {
        return eNodeBCode;
    }

    public void setENodeBCode() {
        if (eNodeBName != null) {
            int eNodeBNameLength = this.eNodeBName.length();
            String code;
            if (eNodeBName.contains("_X")) {
                code = eNodeBName.substring(eNodeBNameLength - 8, eNodeBNameLength - 2);
            } else
                code = eNodeBName.substring(eNodeBNameLength - 6);
            this.eNodeBCode = code;
        }
        this.eNodeBCode = eNodeBCode;
    }

    public String getENodeBRegion() {
        return eNodeBRegion;
    }

    public void setENodeBRegion() {
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

    public int getUniqueId() {
        return uniqueId;
    }

    private void setUniqueId() {
        int codeValue = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(44);
        try {
            codeValue = Integer.valueOf(eNodeBCode.substring(0, eNodeBCode.length() - 2));
        } catch (NumberFormatException ex) {
            char[] letters = eNodeBCode.toCharArray();
            for (char ch : letters) {
                codeValue += (int) ch;
            }
            codeValue += Integer.valueOf(eNodeBId);
            System.out.println(codeValue);
        } catch (NullPointerException e) {
            codeValue = Integer.valueOf(eNodeBId);
        } finally {
            builder.append(codeValue);
        }
        builder.append(Utils.extractRegionId(eNodeBRegion));
        this.uniqueId = Integer.valueOf(builder.toString());
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName() {
        StringBuilder builder = new StringBuilder();
        builder.append("4G");
        builder.append(eNodeBId);
        builder.append(eNodeBCode);
        this.uniqueName = builder.toString();
    }

    public void finalizeProperties() {
        this.setENodeBNumberOfSectors();
        this.setENodeBCode();
        this.setENodeBRegion();
        this.setUniqueName();
        setIdentifiers();
    }

    public double getBwIdentifier() {
        return bwIdentifier;
    }

    private void setIdentifiers() {
        bwIdentifier = this.eNodeBBW;
    }

    public LHardware getLHardware() {
        return lHardware;
    }

    public void setLHardware(LHardware lHardware) {
        this.lHardware = lHardware;
    }

    public static class LHardware {
        int FBBA, FBBC, FRGT, FSMF, FSPD, FTIF, FXEB, FXED;
        double rFModuleIdentifier, systemModuleIdentifier, systemModuleExtentionIdentifier, transmissionModuleIdentifier;
     public    String rfString, smString;

        public LHardware(int FBBA, int FBBC, int FRGT, int FSMF, int FSPD, int FTIF, int FXEB, int FXED) {
            this.FBBA = FBBA;
            this.FBBC = FBBC;
            this.FRGT = FRGT;
            this.FSMF = FSMF;
            this.FSPD = FSPD;
            this.FTIF = FTIF;
            this.FXEB = FXEB;
            this.FXED = FXED;
            setIdentifiers();
            buildHWText();
        }

        private void buildHWText() {
            getRfModuleString();
            getSModuleString();
        }

        private void getSModuleString() {
            smString = "";
            if (FSMF > 0)
                smString = FSMF + " FSMF ";
            if (FBBA > 0)
                smString = smString + " " + FBBA + "FBBA ";
            if (FBBC > 0)
                smString = smString + " " + FBBC + "FBBC ";
        }

        private void getRfModuleString() {
            rfString = "";
            if (FRGT > 0)
                rfString = FRGT + "FRGT ";
            if (FXEB > 0)
                rfString = rfString + " " + FXEB + "FXEB ";
            if (FXED > 0)
                rfString = rfString + " " + FXED + "FXED ";
        }

        public double getrFModuleIdentifier() {
            return rFModuleIdentifier;
        }

        public double getSystemModuleIdentifier() {
            return systemModuleIdentifier;
        }

        public double getSystemModuleExtentionIdentifier() {
            return systemModuleExtentionIdentifier;
        }

        public double getTransmissionModuleIdentifier() {
            return transmissionModuleIdentifier;
        }

        private void setIdentifiers() {
            rFModuleIdentifier = 0.1 * FRGT + FXEB + 10 * FXED;
            systemModuleIdentifier = FSMF;
            systemModuleExtentionIdentifier = 0.1 * FBBA + FBBC;
        }
    }
}
