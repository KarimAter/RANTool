package sample;

public class NodeB extends Cabinet {

    private static final int TECHNOLOGY = 3;
    private String rncId;
    private String wbtsId;
    private String nodeBIP;
    private String lac;
    private String rac;
    private String sfp;
    private int sectors, numberOfCells, numberOfFirstCarriersCells, numberOfOnAirFirstCarriersCells,
            numberOfSecondCarriersCells, numberOfOnAirSecondCarriersCells, numberOfThirdCarriersCells, numberOfOnAirThirdCarriersCells,
            numberOfFirstU900Cells, numberOfSecondU900Cells, numberOfOnAirFirstU900Cells, numberOfOnAirSecondU900Cells,
            numberOfCarriers, numberOfE1s, numberOfOnAirCells,
            numberOfHSDPASet1, numberOfHSDPASet2, numberOfHSDPASet3, numberOfHSUPASet1, numberOfChannelElements, numberOfLCGs;

    private boolean firstCarrier, u900, thirdCarrier;
    private String power, u900Power;
    private boolean rfSharing;
    private boolean standAloneU900;
    private String r99Identifier;
    private String u9Identifier;
    private String powerIdentifier;
    private int numberOfChains;


    @Override
    public int getTechnology() {
        return TECHNOLOGY;
    }

    @Override
    public void setControllerId(String controllerId) {
        this.setRncId(controllerId);
    }

    @Override
    public String getControllerId() {
        return getRncId();
    }

    @Override
    public void setNodeId(String nodeId) {
        this.setWbtsId(nodeId);
    }

    @Override
    public String getNodeId() {
        return getWbtsId();
    }

    @Override
    public void setTxMode(String mode) {
        try {
            switch (mode) {
                case "0":
                case "ATM":
                    txMode = "ATM";
                    break;
                case "38":
                case "DUAL_STACK":
                    txMode = "DUAL STACK";
                    break;
                case "70":
                case "FULL_IP":
                    txMode = "FULL IP";
                    break;
                default:
                    txMode = "FULL IP";
            }
        } catch (NullPointerException e) {
            txMode = "FULL IP";
        }
    }

    @Override
    protected void generateUniqueName() {
        this.uniqueName = "3G" +
                "_" +
                this.key;
    }

    @Override
    protected void generateKey() {
        this.key = this.rncId + "_" + this.wbtsId;
    }

    @Override
    protected void generateProperties() {
        this.properties =
                this.cellIdentifier +
                        "__" +
                        this.r99Identifier +
                        "__" +
                        this.u9Identifier +
                        "__" +
                        this.powerIdentifier +
                        "__" +
                        this.txMode +
                        "__" +
                        this.numberOfE1s +
                        "__" +
                        this.lac +
                        "__" +
                        this.rac +
                        "__" +
                        this.version.replace("_I", "-I") +
                        "__" +
                        this.nodeBIP +
                        "__" +
                        this.sfp +
                        "__" +
                        this.numberOfLCGs +
                        "__" +
                        this.numberOfChains;
    }

    @Override
    public void finishProperties() {
        this.generateKey();
        this.generateUniqueName();
        this.findCodeAndRegion();
        this.setNumberOfSectors();
        this.setStandAloneU900();
        this.generateCellIdentifier();
        this.generateR99Identifier();
        this.generateU9Identifier();
        this.generatePowerIdentifier();
        this.generateProperties();
    }

    @Override
    protected void extractProperties() {
        String[] parts = properties.split("__");
        this.setCellIdentifier(parts[0]);
        this.setR99Identifier(parts[1]);
        this.setU9Identifier(parts[2]);
        this.setPowerIdentifier(parts[3]);
        this.setTxMode(parts[4]);
        this.setNumberOfE1s(Integer.parseInt(parts[5]));
        this.setLac(parts[6]);
        this.setRac(parts[7]);
        this.setVersion(parts[8]);
        this.setNodeBIP(parts[9]);
        this.setSfp(parts[10]);
        try {
            this.setNumberOfLCGs(Integer.parseInt(parts[11]));
        } catch (NumberFormatException e) {
            this.setNumberOfLCGs(0);
        }
        try {
            this.setNumberOfChains(Integer.parseInt(parts[12]));
        } catch (NumberFormatException e) {
            this.setNumberOfChains(0);
        }
    }

    @Override
    public void setConfiguration(NodeConfiguration nodeConfiguration) {
        this.nodeConfiguration = nodeConfiguration;
    }

    @Override
    public NodeConfiguration getNodeConfiguration() {
        return this.nodeConfiguration;
    }

    public void analyzeConfiguration() {
        if (nodeConfiguration != null) {
            nodeConfiguration.extractSectorsMapping();
            nodeConfiguration.extractLinksMapping();
            if (!this.u900Power.equals("0"))
                this.u900Power = nodeConfiguration.checkPower(SectorConfiguration::isU9Sector, u900Power);
            if (!this.power.equals("0"))
                this.power = nodeConfiguration.checkPower(sectorConfiguration -> !sectorConfiguration.isU9Sector(), power);
        }
    }

    @Override
    public String extractConfiguration() {
        if (this.nodeConfiguration != null)
            return this.nodeConfiguration.toString();
        else return "";
    }

    @Override
    protected void generateCellIdentifier() {
        this.cellIdentifier = String.valueOf(this.numberOfFirstCarriersCells) +
                "." +
                this.numberOfSecondCarriersCells +
                "." +
                this.numberOfThirdCarriersCells +
                "." +
                this.numberOfFirstU900Cells +
                "." +
                this.numberOfSecondU900Cells +
                "." +
                this.numberOfOnAirFirstCarriersCells +
                "." +
                this.numberOfOnAirSecondCarriersCells +
                "." +
                this.numberOfOnAirThirdCarriersCells +
                "." +
                this.numberOfOnAirFirstU900Cells +
                "." +
                this.numberOfOnAirSecondU900Cells;
    }


    private void generateR99Identifier() {
        this.r99Identifier = String.valueOf(this.numberOfHSDPASet1) +
                "." +
                this.numberOfHSDPASet2 +
                "." +
                this.numberOfHSDPASet3 +
                "." +
                this.numberOfHSUPASet1 +
                "." +
                this.numberOfChannelElements;
    }

    private void generateU9Identifier() {
        this.u9Identifier = String.valueOf(this.standAloneU900) +
                "." +
                this.rfSharing;
    }

    private void generatePowerIdentifier() {
        this.powerIdentifier = String.valueOf(this.power) +
                "-" +
                this.u900Power;
    }

    public void setProperties(String properties) {
        this.properties = properties;
        extractProperties();
    }

    public void extractCellsFromIdentifier() {
        String[] parts = cellIdentifier.split("\\.");
        numberOfFirstCarriersCells = Integer.valueOf(parts[0]);
        numberOfSecondCarriersCells = Integer.valueOf(parts[1]);
        numberOfThirdCarriersCells = Integer.valueOf(parts[2]);
        numberOfFirstU900Cells = Integer.valueOf(parts[3]);
        numberOfSecondU900Cells = Integer.valueOf(parts[4]);
        this.setNumberOfOnAirFirstCarriersCells(Integer.valueOf(parts[5]));
        this.setNumberOfOnAirSecondCarriersCells(Integer.valueOf(parts[6]));
        this.setNumberOfOnAirThirdCarriersCells(Integer.valueOf(parts[7]));
        this.setNumberOfOnAirFirstU900Cells(Integer.valueOf(parts[8]));
        this.setNumberOfOnAirSecondU900Cells(Integer.valueOf(parts[9]));
        numberOfCells = numberOfFirstCarriersCells + numberOfSecondCarriersCells + numberOfThirdCarriersCells + numberOfFirstU900Cells + numberOfSecondU900Cells;
        numberOfOnAirCells = numberOfOnAirFirstCarriersCells + numberOfOnAirSecondCarriersCells + numberOfOnAirThirdCarriersCells + numberOfOnAirFirstU900Cells + numberOfOnAirSecondU900Cells;
        this.setNumberOfSectors();
    }


    private void extractR99FromIdentifier() {
        String[] parts = r99Identifier.split("\\.");
        numberOfHSDPASet1 = Integer.valueOf(parts[0]);
        numberOfHSDPASet2 = Integer.valueOf(parts[1]);
        numberOfHSDPASet3 = Integer.valueOf(parts[2]);
        numberOfHSUPASet1 = Integer.valueOf(parts[3]);
        numberOfChannelElements = Integer.valueOf(parts[4]);
    }


    private void extractU9FromIdentifier() {
        String[] parts = u9Identifier.split("\\.");
        standAloneU900 = Boolean.valueOf(parts[0]);
        rfSharing = Boolean.valueOf(parts[1]);
    }


    private void extractPowerFromIdentifier() {
        String[] parts = powerIdentifier.split("-");
        power = parts[0];
        u900Power = parts[1];
    }

    public void setNodeBName(String name) {
        this.name = name;
    }

    public void setRncId(String rncId) {
        this.rncId = rncId;
    }

    public void setWbtsId(String wbtsId) {
        this.wbtsId = wbtsId;
    }


    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public void setNumberOfFirstCarriersCells(int numberOfFirstCarriersCells) {

        this.numberOfFirstCarriersCells = numberOfFirstCarriersCells;
    }

    public void setNumberOfSecondCarriersCells(int numberOfSecondCarriersCells) {
        this.numberOfSecondCarriersCells = numberOfSecondCarriersCells;
    }

    public void setNumberOfThirdCarriersCells(int numberOfThirdCarriersCells) {
        this.numberOfThirdCarriersCells = numberOfThirdCarriersCells;
    }


    public void setNumberOfE1s(int numberOfE1s) {
        this.numberOfE1s = numberOfE1s;
    }

    public void setNumberOfHSDPASet1(int numberOfHSDPASet1) {
        if (numberOfHSDPASet1 < 0)
            this.numberOfHSDPASet1 = 0;
        else
            this.numberOfHSDPASet1 = numberOfHSDPASet1;
    }

    public void setNumberOfHSDPASet2(int numberOfHSDPASet2) {
        if (numberOfHSDPASet2 < 0)
            this.numberOfHSDPASet2 = 0;
        else
            this.numberOfHSDPASet2 = numberOfHSDPASet2;
    }

    public void setNumberOfHSDPASet3(int numberOfHSDPASet3) {
        if (numberOfHSDPASet3 < 0)
            this.numberOfHSDPASet3 = 0;
        else
            this.numberOfHSDPASet3 = numberOfHSDPASet3;
    }

    public void setNumberOfHSUPASet1(int numberOfHSUPASet1) {
        if (numberOfHSUPASet1 < 0)
            this.numberOfHSUPASet1 = 0;
        else
            this.numberOfHSUPASet1 = numberOfHSUPASet1;
    }

    public void setNumberOfChannelElements(int numberOfChannelElements) {
        this.numberOfChannelElements = numberOfChannelElements;
    }

    public void setNumberOfOnAirFirstCarriersCells(int numberOfOnAirFirstCarriersCells) {
        if (numberOfOnAirFirstCarriersCells > 0) {
            this.firstCarrier = true;
            this.numberOfCarriers++;
        }
        this.numberOfOnAirFirstCarriersCells = numberOfOnAirFirstCarriersCells;
    }

    public void setNumberOfOnAirSecondCarriersCells(int numberOfOnAirSecondCarriersCells) {
        if (numberOfOnAirSecondCarriersCells > 0) {
            this.numberOfCarriers++;
        }
        this.numberOfOnAirSecondCarriersCells = numberOfOnAirSecondCarriersCells;
    }

    public void setNumberOfOnAirThirdCarriersCells(int numberOfOnAirThirdCarriersCells) {
        if (numberOfOnAirThirdCarriersCells > 0) {
            this.thirdCarrier = true;
            this.numberOfCarriers++;
        }
        this.numberOfOnAirThirdCarriersCells = numberOfOnAirThirdCarriersCells;
    }


    public int getNumberOfCarriers() {
        return numberOfCarriers;
    }


    public String getCellIdentifier() {
        return cellIdentifier;
    }

    public void setCellIdentifier(String cellIdentifier) {
        this.cellIdentifier = cellIdentifier;
        extractCellsFromIdentifier();
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getNumberOfFirstU900Cells() {
        return numberOfFirstU900Cells;
    }

    public int getNumberOfSecondU900Cells() {
        return numberOfSecondU900Cells;
    }

    public int getNumberOfOnAirFirstU900Cells() {
        return numberOfOnAirFirstU900Cells;
    }

    public int getNumberOfOnAirSecondU900Cells() {
        return numberOfOnAirSecondU900Cells;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public Hardware getHardware() {
        return hardware;
    }


    String getSfp() {
        return sfp;
    }

    public void setSfp(String sfp) {
        this.sfp = sfp;
    }

    public void setNumberOfFirstU900Cells(int numberOfFirstU900Cells) {
        this.numberOfFirstU900Cells = numberOfFirstU900Cells;
    }

    public void setNumberOfSecondU900Cells(int numberOfSecondU900Cells) {
        this.numberOfSecondU900Cells = numberOfSecondU900Cells;
    }

    public void setNumberOfOnAirFirstU900Cells(int numberOfOnAirFirstU900Cells) {
        if (numberOfOnAirFirstU900Cells > 0) {
            this.u900 = true;
            this.numberOfCarriers++;
        }
        this.numberOfOnAirFirstU900Cells = numberOfOnAirFirstU900Cells;
    }

    public void setNumberOfOnAirSecondU900Cells(int numberOfOnAirSecondU900Cells) {
        if (numberOfOnAirSecondU900Cells > 0) {
            this.u900 = true;
            this.numberOfCarriers++;
        }
        this.numberOfOnAirSecondU900Cells = numberOfOnAirSecondU900Cells;
    }

    public String getR99Identifier() {
        return r99Identifier;
    }

    public void setR99Identifier(String r99Identifier) {
        this.r99Identifier = r99Identifier;
        extractR99FromIdentifier();
    }

    public String getU9Identifier() {
        return u9Identifier;
    }

    public void setU9Identifier(String u9Identifier) {
        this.u9Identifier = u9Identifier;
        extractU9FromIdentifier();
    }

    public String getPowerIdentifier() {
        return powerIdentifier;
    }

    public void setPowerIdentifier(String powerIdentifier) {
        this.powerIdentifier = powerIdentifier;
        extractPowerFromIdentifier();
    }

    public String getNodeBIP() {
        return nodeBIP;
    }

    public void setNodeBIP(String nodeBIP) {
        this.nodeBIP = nodeBIP;
    }

    public int getNumberOfLCGs() {
        return numberOfLCGs;
    }

    public void setNumberOfLCGs(int numberOfLCGs) {
        this.numberOfLCGs = numberOfLCGs;
    }

    public void setNumberOfChains(int numberOfChains) {
        this.numberOfChains = numberOfChains;
    }

    public int getNumberOfChains() {
        return numberOfChains;
    }

    public String getRegion() {
        return region;
    }

    private void setNumberOfSectors() {
        if (firstCarrier)
            this.sectors = this.numberOfOnAirFirstCarriersCells;
        else if (u900) {
            this.sectors = this.numberOfOnAirFirstU900Cells;
        }
    }

    boolean isStandAloneU900() {
        return standAloneU900;
    }

    private void setStandAloneU900() {
        if (!firstCarrier && u900)
            this.standAloneU900 = true;
    }

    public boolean isRfSharing() {
        return rfSharing;
    }

    public void setRfSharing(int cSharing) {
        if (cSharing > 0)
            this.rfSharing = true;
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

    String getPower() {
        return power;
    }

    public void setU900Power(int u900Power, int vam) {
        int vamCoeff = 1;
        if (vam > 0)
            vamCoeff = 2;
        if (u900Power == 430)
            this.u900Power = String.valueOf(vamCoeff * 20);
        else if (u900Power == 442)
            this.u900Power = String.valueOf(vamCoeff * 26.65);
        else if (u900Power == 448 || u900Power == 450)
            this.u900Power = String.valueOf(vamCoeff * 30);
        else if (u900Power == 460)
            this.u900Power = String.valueOf(vamCoeff * 40);
        else if (u900Power == 478)
            this.u900Power = String.valueOf(60);
        else if (u900Power == 490)
            this.u900Power = String.valueOf(80);
        else if (u900Power == 65535 || u900Power == 0)
            this.u900Power = String.valueOf(0);
    }

    String getU900Power() {
        return u900Power;
    }

    public void setPower(int power, int vam) {
        int vamCoeff = 1;
        if (vam > 0)
            vamCoeff = 2;
        if (power == 210)
            this.power = String.valueOf(vamCoeff * .125);
        else if (power == 240)
            this.power = String.valueOf(vamCoeff * 0.25);
        else if (power == 400)
            this.power = String.valueOf(vamCoeff * 10);
        else if (power == 418)
            this.power = String.valueOf(vamCoeff * 15);
        else if (power == 430)
            this.power = String.valueOf(vamCoeff * 20);
        else if (power == 442)
            this.power = String.valueOf(vamCoeff * 26.65);
        else if (power == 448 || power == 450)
            this.power = String.valueOf(vamCoeff * 30);
        else if (power == 460)
            this.power = String.valueOf(vamCoeff * 40);
        else if (power == 478)
            this.power = String.valueOf(60);
        else if (power == 490)
            this.power = String.valueOf(80);
        else if (power == 65535 || power == 0)
            this.power = String.valueOf(0);
    }


    public String getRncId() {
        return rncId;
    }

    public String getWbtsId() {
        return wbtsId;
    }

    public int getSectors() {
        return sectors;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public int getNumberOfOnAirCells() {
        return numberOfOnAirCells;
    }

    public int getNumberOfFirstCarriersCells() {
        return numberOfFirstCarriersCells;
    }

    public int getNumberOfSecondCarriersCells() {
        return numberOfSecondCarriersCells;
    }

    public int getNumberOfThirdCarriersCells() {
        return numberOfThirdCarriersCells;
    }

    public int getNumberOfOnAirFirstCarriersCells() {
        return numberOfOnAirFirstCarriersCells;
    }

    public int getNumberOfOnAirSecondCarriersCells() {
        return numberOfOnAirSecondCarriersCells;
    }

    public int getNumberOfOnAirThirdCarriersCells() {
        return numberOfOnAirThirdCarriersCells;
    }

    public int getNumberOfHSDPASet1() {
        return numberOfHSDPASet1;
    }

    public int getNumberOfHSDPASet2() {
        return numberOfHSDPASet2;
    }

    public int getNumberOfHSDPASet3() {
        return numberOfHSDPASet3;
    }

    public int getNumberOfHSUPASet1() {
        return numberOfHSUPASet1;
    }

    public int getNumberOfChannelElements() {
        return numberOfChannelElements;
    }

    public int getNumberOfE1s() {
        return numberOfE1s;
    }

    public boolean isU900() {
        return u900;
    }

    public void setU900(boolean u900) {
        this.u900 = u900;
    }

    public boolean isThirdCarrier() {
        return thirdCarrier;
    }

    public void setThirdCarrier(boolean thirdCarrier) {
        this.thirdCarrier = thirdCarrier;
    }


}
