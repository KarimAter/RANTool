package sample;

import Helpers.Constants;
import Helpers.PropertiesExtractor;


public class NodeB implements PropertiesExtractor {

    public static final String TAG = "NodeBLogger";
    private String nodeBName, nodeBCode, nodeBRncId, nodeBWbtsId, nodeBVersion;
    private int nodeBId, nodeBNumberOfSectors, nodeBNumberOfCells, nodeBNumberOfFirstCarriersCells, nodeBNumberOfOnAirFirstCarriersCells,
            nodeBNumberOfSecondCarriersCells, nodeBNumberOfOnAirSecondCarriersCells, nodeBNumberOfThirdCarriersCells, nodeBNumberOfOnAirThirdCarriersCells,
            nodeBNumberOfU900CarriersCells, nodeBNumberOfOnAirU900CarriersCells, nodeBNumberOfCarriers, nodeBNumberOfE1s, nodeBNumberOfOnAirCells, nodeBNumberOfOffAirCells,
            numberOfHSDPASet1, numberOfHSDPASet2, numberOfHSDPASet3, numberOfHSUPASet1, numberOfChannelElements;

    private boolean firstCarrier, secondCarrier, thirdCarrier, u900;

    private Constants.uTxMode nodeBTxMode;

    public NodeB() {

    }


    private enum onOff {onAir, offAir}

//    private String siteName, rncId, wbtsId, siteCode;
//    boolean multiNodeB, firstNodeB, secondNodeB, offAir;

    private int noOfSectors;


    @Override
    public void calculateNumberOfSectors() {

    }

    private void createNodeB() {
//        nodeBCode = extractSideCode(nodeBName);

    }

    //
//    public NodeB(String nodeBRncId, String nodeBWbtsId,String txMode,
//                 String nodeBVersion, String nodeBName) {
//        this.nodeBName = nodeBName;
//        this.nodeBRncId = nodeBRncId;
//        this.nodeBWbtsId = nodeBWbtsId;
//        this.nodeBVersion = nodeBVersion;
//        createNodeB();
//    }
    public NodeB(String nodeBRncId, String nodeBWbtsId, int nodeBNumberOfCells, int nodeBNumberOfOnAirCells,
                 int nodeBNumberOfFirstCarriersCells, int nodeBNumberOfSecondCarriersCells,
                 int nodeBNumberOfThirdCarriersCells, int nodeBNumberOfU900CarriersCells, String nodeBName, String tx, String nodeBVersion) {

        this.nodeBRncId = nodeBRncId;
        this.nodeBWbtsId = nodeBWbtsId;
        this.nodeBNumberOfCells = nodeBNumberOfCells;
        this.nodeBNumberOfOnAirCells = nodeBNumberOfOnAirCells;
        this.nodeBNumberOfFirstCarriersCells = nodeBNumberOfFirstCarriersCells;
        this.nodeBNumberOfSecondCarriersCells = nodeBNumberOfSecondCarriersCells;
        this.nodeBNumberOfThirdCarriersCells = nodeBNumberOfThirdCarriersCells;
        this.nodeBNumberOfU900CarriersCells = nodeBNumberOfU900CarriersCells;
        this.nodeBName = nodeBName;
        this.nodeBVersion = nodeBVersion;
    }

    public String getNodeBName() {
        return nodeBName;
    }

    public String getNodeBCode() {
        return nodeBCode;
    }

    public String getNodeBRncId() {
        return nodeBRncId;
    }

    public String getNodeBWbtsId() {
        return nodeBWbtsId;
    }

    public String getNodeBVersion() {
        return nodeBVersion;
    }

    public int getNodeBNumberOfSectors() {
        return nodeBNumberOfSectors;
    }

    public int getNodeBNumberOfCells() {
        return nodeBNumberOfCells;
    }

    public int getNodeBNumberOfOnAirCells() {
        return nodeBNumberOfOnAirCells;
    }

    public int getNodeBNumberOfFirstCarriersCells() {
        return nodeBNumberOfFirstCarriersCells;
    }

    public int getNodeBNumberOfSecondCarriersCells() {
        return nodeBNumberOfSecondCarriersCells;
    }

    public int getNodeBNumberOfThirdCarriersCells() {
        return nodeBNumberOfThirdCarriersCells;
    }

    public int getNodeBNumberOfU900CarriersCells() {
        return nodeBNumberOfU900CarriersCells;
    }


    public int getNodeBNumberOfOnAirFirstCarriersCells() {
        return nodeBNumberOfOnAirFirstCarriersCells;
    }

    public int getNodeBNumberOfOnAirSecondCarriersCells() {
        return nodeBNumberOfOnAirSecondCarriersCells;
    }

    public int getNodeBNumberOfOnAirThirdCarriersCells() {
        return nodeBNumberOfOnAirThirdCarriersCells;
    }

    public int getNodeBNumberOfOnAirU900CarriersCells() {
        return nodeBNumberOfOnAirU900CarriersCells;
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

    public int getNodeBNumberOfE1s() {
        return nodeBNumberOfE1s;
    }

    public void setNodeBName(String nodeBName) {
        this.nodeBName = nodeBName;
    }

    public void setNodeBCode(String nodeBCode) {
        this.nodeBCode = nodeBCode;
    }

    public void setNodeBRncId(String nodeBRncId) {
        this.nodeBRncId = nodeBRncId;
    }

    public void setNodeBWbtsId(String nodeBWbtsId) {
        this.nodeBWbtsId = nodeBWbtsId;
    }

    public void setNodeBVersion(String nodeBVersion) {
        this.nodeBVersion = nodeBVersion;
    }

    public void setNodeBId(int nodeBId) {
        this.nodeBId = nodeBId;
    }

    public void setNodeBNumberOfSectors(int nodeBNumberOfSectors) {
        this.nodeBNumberOfSectors = nodeBNumberOfSectors;
    }

    public void setNodeBNumberOfCells(int nodeBNumberOfCells) {
        this.nodeBNumberOfCells = nodeBNumberOfCells;
    }

    public void setNodeBNumberOfFirstCarriersCells(int nodeBNumberOfFirstCarriersCells) {
        this.nodeBNumberOfFirstCarriersCells = nodeBNumberOfFirstCarriersCells;
    }

    public void setNodeBNumberOfSecondCarriersCells(int nodeBNumberOfSecondCarriersCells) {
        this.nodeBNumberOfSecondCarriersCells = nodeBNumberOfSecondCarriersCells;
    }

    public void setNodeBNumberOfThirdCarriersCells(int nodeBNumberOfThirdCarriersCells) {
        this.nodeBNumberOfThirdCarriersCells = nodeBNumberOfThirdCarriersCells;
    }

    public void setNodeBNumberOfU900CarriersCells(int nodeBNumberOfU900CarriersCells) {
        this.nodeBNumberOfU900CarriersCells = nodeBNumberOfU900CarriersCells;
    }

    public void setNodeBNumberOfCarriers(int nodeBNumberOfCarriers) {
        this.nodeBNumberOfCarriers = nodeBNumberOfCarriers;
    }

    public void setNodeBNumberOfE1s(int nodeBNumberOfE1s) {
        this.nodeBNumberOfE1s = nodeBNumberOfE1s;
    }

    public void setNodeBNumberOfOnAirCells(int nodeBNumberOfOnAirCells) {
        this.nodeBNumberOfOnAirCells = nodeBNumberOfOnAirCells;
    }

    public void setNodeBNumberOfOffAirCells(int nodeBNumberOfOffAirCells) {
        this.nodeBNumberOfOffAirCells = nodeBNumberOfOffAirCells;
    }

    public void setNumberOfHSDPASet1(int numberOfHSDPASet1) {
        this.numberOfHSDPASet1 = numberOfHSDPASet1;
    }

    public void setNumberOfHSDPASet2(int numberOfHSDPASet2) {
        this.numberOfHSDPASet2 = numberOfHSDPASet2;
    }

    public void setNumberOfHSDPASet3(int numberOfHSDPASet3) {
        this.numberOfHSDPASet3 = numberOfHSDPASet3;
    }

    public void setNumberOfHSUPASet1(int numberOfHSUPASet1) {
        this.numberOfHSUPASet1 = numberOfHSUPASet1;
    }

    public void setNumberOfChannelElements(int numberOfChannelElements) {
        this.numberOfChannelElements = numberOfChannelElements;
    }

    public void setFirstCarrier(boolean firstCarrier) {
        this.firstCarrier = firstCarrier;
    }

    public void setSecondCarrier(boolean secondCarrier) {
        this.secondCarrier = secondCarrier;
    }

    public void setThirdCarrier(boolean thirdCarrier) {
        this.thirdCarrier = thirdCarrier;
    }

    public void setU900(boolean u900) {
        this.u900 = u900;
    }

    public void setNoOfSectors(int noOfSectors) {
        this.noOfSectors = noOfSectors;
    }

    public void setNodeBNumberOfOnAirFirstCarriersCells(int nodeBNumberOfOnAirFirstCarriersCells) {
        this.nodeBNumberOfOnAirFirstCarriersCells = nodeBNumberOfOnAirFirstCarriersCells;
    }

    public void setNodeBNumberOfOnAirSecondCarriersCells(int nodeBNumberOfOnAirSecondCarriersCells) {
        this.nodeBNumberOfOnAirSecondCarriersCells = nodeBNumberOfOnAirSecondCarriersCells;
    }

    public void setNodeBNumberOfOnAirThirdCarriersCells(int nodeBNumberOfOnAirThirdCarriersCells) {
        this.nodeBNumberOfOnAirThirdCarriersCells = nodeBNumberOfOnAirThirdCarriersCells;
    }

    public void setNodeBNumberOfOnAirU900CarriersCells(int nodeBNumberOfOnAirU900CarriersCells) {
        this.nodeBNumberOfOnAirU900CarriersCells = nodeBNumberOfOnAirU900CarriersCells;
    }

    public String getNodeBTxMode() {
        return nodeBTxMode.toString();
    }

    public void setNodeBTxMode(String value) {

        try {


        switch (value) {
            case "0":
                nodeBTxMode = Constants.uTxMode.ATM;
                break;
            case "38":
                nodeBTxMode = Constants.uTxMode.DUAL_STACK;
                break;
            default:
                nodeBTxMode = Constants.uTxMode.FULL_IP;
        }}
        catch (NullPointerException e){
            nodeBTxMode=Constants.uTxMode.FULL_IP;
        }
    }
}


//    @Override
//    public boolean equals(Object obj) {
//        return this.siteCode.equals(((NodeB) obj).getSiteCode());
//    }

//    @Override
//    public int hashCode() {
//
//        return Objects.hash(siteCode, rncId, wbtsId);
//    }