package sample;

public class LSite {
    String eNodeBName, eNodeBCode, eNodeBRegion, eNodeBId, eNodeBVersion;
    private int eNodeBNumberOfSectors, eNodeBNumberOfCells, eNodeBNumberOfOnAirCells,eNodeBBW,eNodeBMimo;

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
        if (eNodeBCode != null) {
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

    public void setENodeBNumberOfOnAirCells(int eNodeBNumberOfOnAirCells,int onAirSuperCells) {
        this.eNodeBNumberOfOnAirCells = (3*(this.eNodeBNumberOfCells-onAirSuperCells)- eNodeBNumberOfOnAirCells)/2 + onAirSuperCells;
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

    public void finalizeProperties() {
        this.setENodeBNumberOfSectors();
        this.setENodeBCode();
        this.setENodeBRegion();
    }


}
