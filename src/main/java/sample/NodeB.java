package sample;

import Helpers.Utils;

import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class NodeB extends Cabinet {
    private static final int TECHNOLOGY = 3;
    private String rncId, wbtsId, nodeBIP, lac, rac, sfp;
    private int numberOfCarriers, numberOfLCGs, numberOfChains;
    private boolean firstCarrier, u900, thirdCarrier, rfSharing, standAloneU900;
    private String power, u900Power;
    private String r99Identifier;
    private String u9Identifier;
    private String powerIdentifier;

    public static final String[] cellsCountNames = {"numberOfFirstCarriersCells", "numberOfOnAirFirstCarriersCells", "numberOfSecondCarriersCells",
            "numberOfOnAirSecondCarriersCells", "numberOfThirdCarriersCells", "numberOfOnAirThirdCarriersCells",
            "numberOfFirstU900Cells", "numberOfOnAirFirstU900Cells", "numberOfSecondU900Cells", "numberOfOnAirSecondU900Cells"};
    public static final String[] r99ParametersNames = {"numberOfHSDPASet1", "numberOfHSDPASet2", "numberOfHSDPASet3", "numberOfHSUPASet1", "numberOfChannelElements"};

    private Map<String, Integer> cellsCountMap = new LinkedHashMap<>();
    private Map<String, Integer> r99CountMap = new LinkedHashMap<>();
    private Map<String, Integer> powerValueMap = new LinkedHashMap<>();

    private void setCellsCountMap(Map<String, Integer> cellsCountMap) {
        this.cellsCountMap = cellsCountMap;
        setNumberOfCells(cellsCountMap.entrySet().stream().filter(stringIntegerEntry ->
                !stringIntegerEntry.getKey().contains("OnAir")).mapToInt(Map.Entry::getValue).sum());
        IntSummaryStatistics onAirSummary = cellsCountMap.entrySet().stream().filter(stringIntegerEntry ->
                stringIntegerEntry.getKey().contains("OnAir"))
                .mapToInt(Map.Entry::getValue)
                .filter(value -> value > 0)
                .summaryStatistics();
        setNumberOfOnAirCells((int) onAirSummary.getSum());
        numberOfCarriers = (int) onAirSummary.getCount();
        firstCarrier = carrierTest(1);
        u900 = carrierTest(7) || carrierTest(9);
        thirdCarrier = carrierTest(5);
        this.setNumberOfSectors();
    }

    public Map<String, Integer> createCellsCountMap(String[] keys, int[] values) {
        LinkedHashMap<String, Integer> cellMap = mapCreator(keys, values);
        this.setCellsCountMap(cellMap);
        return cellMap;
    }

    private LinkedHashMap<String, Integer> mapCreator(String[] keys, int[] values) {
        return IntStream.range(0, keys.length)
                .boxed().collect(Collectors.toMap(i -> keys[i], i -> values[i], (x, y) -> y, LinkedHashMap::new));
    }

    public Map<String, Integer> createR99CountMap(String[] keys, int[] values) {
        LinkedHashMap<String, Integer> r99Map = mapCreator(keys, values);
        this.setR99CountMap(r99Map);
        return r99CountMap;
    }

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
                case "DUAL STACK":
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
        this.properties = Stream.of(cellIdentifier, r99Identifier, u9Identifier, powerIdentifier, txMode,
                numberOfE1s, lac, rac, version, nodeBIP, sfp, numberOfLCGs, numberOfChains)
                .map(String::valueOf).collect(Collectors.joining("__"));
    }

    @Override
    public void finishProperties() {
        this.generateKey();
        this.generateUniqueName();
        this.findCodeAndRegion();
//        this.setNumberOfSectors();
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
        setNumberOfE1s(Integer.parseInt(parts[5]));
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
        this.cellIdentifier = cellsCountMap.values().stream().map(String::valueOf).collect(Collectors.joining("."));
    }

    public void setR99CountMap(Map<String, Integer> r99CountMap) {
        this.r99CountMap = r99CountMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> {
                    int value = entry.getValue();
                    return value < 0 ? 0 : value;
                }, (x, y) -> y, LinkedHashMap::new));
    }

    private void generateR99Identifier() {
        this.r99Identifier = r99CountMap.values().stream().map(String::valueOf).collect(Collectors.joining("."));
    }

    private void generateU9Identifier() {
        this.u9Identifier = this.standAloneU900 + "." + this.rfSharing;
    }

    private void generatePowerIdentifier() {
        this.powerIdentifier = this.power + "-" + this.u900Power;
    }

    public void setProperties(String properties) {
        this.properties = properties;
        extractProperties();
    }

    public void extractCellsFromIdentifier() {
        int[] ints = Stream.of(cellIdentifier.split("\\.")).mapToInt(Integer::valueOf).toArray();
        setCellsCountMap(createCellsCountMap(cellsCountNames, ints));
    }

    @Override
    protected void setNumberOfSectors() {
        if (firstCarrier)
            numberOfSectors = this.cellsCountMap.get(cellsCountNames[1]);
        else if (u900) {
            numberOfSectors = this.cellsCountMap.get(cellsCountNames[7]);
            if (numberOfSectors == 0)
                numberOfSectors = this.cellsCountMap.get(cellsCountNames[9]);
        }
    }

    private boolean carrierTest(int cellMapIndex) {
        return cellsCountMap.entrySet().stream().filter(stringIntegerEntry -> stringIntegerEntry.getKey().contains(cellsCountNames[cellMapIndex]))
                .anyMatch(stringIntegerEntry -> stringIntegerEntry.getValue() > 0);
    }


    private void extractR99FromIdentifier() {
        int[] ints = Stream.of(r99Identifier.split("\\.")).mapToInt(Integer::valueOf).toArray();
        this.r99CountMap = createR99CountMap(r99ParametersNames, ints);
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

    public void setRncId(String rncId) {
        this.rncId = rncId;
    }

    public void setWbtsId(String wbtsId) {
        this.wbtsId = wbtsId;
    }

    int getNumberOfCarriers() {
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

    private void setR99Identifier(String r99Identifier) {
        this.r99Identifier = r99Identifier;
        extractR99FromIdentifier();
    }

    private void setU9Identifier(String u9Identifier) {
        this.u9Identifier = u9Identifier;
        extractU9FromIdentifier();
    }

    private void setPowerIdentifier(String powerIdentifier) {
        this.powerIdentifier = powerIdentifier;
        extractPowerFromIdentifier();
    }

    String getNodeBIP() {
        return nodeBIP;
    }

    public void setNodeBIP(String nodeBIP) {
        this.nodeBIP = nodeBIP;
    }

    int getNumberOfLCGs() {
        return numberOfLCGs;
    }

    public void setNumberOfLCGs(int numberOfLCGs) {
        this.numberOfLCGs = numberOfLCGs;
    }

    public void setNumberOfChains(int numberOfChains) {
        this.numberOfChains = numberOfChains;
    }

    int getNumberOfChains() {
        return numberOfChains;
    }

    public String getRegion() {
        return region;
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
        this.rfSharing = cSharing == 1;
    }

    String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    String getRac() {
        return rac;
    }

    public void setRac(String rac) {
        this.rac = rac;
    }

    String getPower() {
        return power;
    }

    public void setU900Power(int u900Power, int vam) {
        try {
            this.u900Power = String.valueOf(Utils.convertPower(u900Power, vam));
        } catch (Exception e) {
            e.printStackTrace();
            this.u900Power = "0";
        }
    }

    String getU900Power() {
        return u900Power;
    }

    public void setPower(int power, int vam) {
        try {
            this.power = String.valueOf(Utils.convertPower(power, vam));
        } catch (Exception e) {
            e.printStackTrace();
            this.power = "0";
        }
    }


    public String getRncId() {
        return rncId;
    }

    public String getWbtsId() {
        return wbtsId;
    }

    public boolean isU900() {
        return u900;
    }


    public boolean isThirdCarrier() {
        return thirdCarrier;
    }

    Map<String, Integer> getCellsCountMap() {
        return cellsCountMap;
    }

    public Map<String, Integer> getR99CountMap() {
        return r99CountMap;
    }

}
