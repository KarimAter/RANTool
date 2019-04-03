package sample;

import Helpers.Constants;
import Helpers.PropertiesExtractor;

import java.util.ArrayList;


public class NodeB implements PropertiesExtractor {

    public static final String TAG = "NodeBLogger";
    private String nodeBName, nodeBCode, nodeBRncId, nodeBWbtsId, nodeBVersion, nodeBIP;
    private int nodeBId, nodeBNumberOfSectors, nodeBNumberOfCells, nodeBNumberOfFirstCarriersCells, nodeBNumberOfOnAirFirstCarriersCells,
            nodeBNumberOfSecondCarriersCells, nodeBNumberOfOnAirSecondCarriersCells, nodeBNumberOfThirdCarriersCells, nodeBNumberOfOnAirThirdCarriersCells,
            nodeBNumberOfU900CarriersCells, nodeBNumberOfOnAirU900CarriersCells, nodeBNumberOfCarriers, nodeBNumberOfE1s, nodeBNumberOfOnAirCells, nodeBNumberOfOffAirCells,
            numberOfHSDPASet1, numberOfHSDPASet2, numberOfHSDPASet3, numberOfHSUPASet1, numberOfChannelElements, lac, rac;

    private boolean firstCarrier, secondCarrier, thirdCarrier, u900;
    double power, u900Power;
    private boolean rfSharing;

    private Constants.uTxMode nodeBTxMode;
    private boolean standAloneU900;
    private String region;
    private USite.UHardware uHardware;

    public NodeB() {

    }


    private enum onOff {onAir, offAir}

//    private String siteName, rncId, wbtsId, siteCode;
//    boolean multiNodeB, firstNodeB, secondNodeB, offAir;

    USite.UHardware getUHardware() {
        return uHardware;
    }

    void setUHardware(NodeBHW nodeBHW) {
        USite.UHardware uHardware = new USite.UHardware();
        if (nodeBHW != null) {
            ArrayList<HwItem> hwItems = nodeBHW.getHwItems();
            for (HwItem hwItem : hwItems) {
                switch (hwItem.getUserLabel()) {
                    case "FBBA":
                        uHardware.FBBA++;
                        break;
                    case "FRGC":
                        uHardware.FRGC++;
                        break;
                    case "FRGD":
                        uHardware.FRGD++;
                        break;
                    case "FRGF":
                        uHardware.FRGF++;
                        break;
                    case "FRGL":
                        uHardware.FRGL++;
                        break;
                    case "FRGM":
                        uHardware.FRGM++;
                        break;
                    case "FRGP":
                        uHardware.FRGP++;
                        break;
                    case "FRGT":
                        uHardware.FRGT++;
                        break;
                    case "FRGU":
                        uHardware.FRGU++;
                        break;
                    case "FRGX":
                        uHardware.FRGX++;
                        break;
                    case "FSMB":
                        uHardware.FSMB++;
                        break;
                    case "FSMD":
                        uHardware.FSMD++;
                        break;
                    case "FSME":
                        uHardware.FSME++;
                        break;
                    case "FSMF":
                        uHardware.FSMF++;
                        break;
                    case "FTIA":
                        uHardware.FTIA++;
                        break;
                    case "FTIB":
                        uHardware.FTIB++;
                        break;
                    case "FTIF":
                        uHardware.FTIF++;
                        break;
                    case "FTPB":
                        uHardware.FTPB++;
                        break;
                    case "FXDA":
                        uHardware.FXDA++;
                        break;
                    case "FXDB":
                        uHardware.FXDB++;
                        break;
                }
            }
        }
        this.uHardware = uHardware;
        this.uHardware.setIdentifiers();
        this.uHardware.buildHWText();
    }

    public void finalizeProperties() {
        this.setNumberOfSectors();
        this.setStandAloneU900();
        this.setRegion();
//        this.setUniqueName();
//        setIdentifiers();
    }

    public String getNodeBIP() {
        return nodeBIP;
    }

    public void setNodeBIP(String nodeBIP) {
        this.nodeBIP = nodeBIP;
    }

    public String getRegion() {
        return region;
    }

    private void setRegion() {
        if (nodeBCode != null) {
            try {
                String region = nodeBCode.substring(nodeBCode.length() - 2);
                if (region.equalsIgnoreCase("UP") || region.equalsIgnoreCase("SI") || region.equalsIgnoreCase("RE")
                        || region.equalsIgnoreCase("DE") || region.equalsIgnoreCase("AL"))
                    this.region = region;
                else {
                    switch (this.nodeBRncId) {
                        case "30":
                        case "22":
                        case "38":
                        case "48":
                        case "64":
                            this.region = "AL";
                        case "36":
                        case "54":
                        case "28":
                        case "46":
                        case "52":
                        case "44":
                        case "24":
                        case "4":
                        case "56":
                            this.region = "DE";
                        case "26":
                        case "18":
                            this.region = "RE";
                        case "14":
                        case "34":
                        case "50":
                        case "58":
                        case "60":
                        case "62":
                            this.region = "UP";
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println(this.nodeBCode);
                this.nodeBCode = "";
            }

        }
    }

    private void setNumberOfSectors() {
        if (firstCarrier)
            this.nodeBNumberOfSectors = this.nodeBNumberOfOnAirFirstCarriersCells;
        else if (u900) {
            this.nodeBNumberOfSectors = this.nodeBNumberOfOnAirU900CarriersCells;
        }
    }

    public boolean isStandAloneU900() {
        return standAloneU900;
    }

    public void setStandAloneU900() {
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

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getRac() {
        return rac;
    }

    public void setRac(int rac) {
        this.rac = rac;
    }

    private int noOfSectors;

    public double getPower() {
        return power;
    }

    public void setU900Power(int u900Power, int vam) {
        int vamCoeff = 1;
        if (vam > 0)
            vamCoeff = 2;
        if (u900Power == 430)
            this.u900Power = vamCoeff * 20;
        else if (u900Power == 442)
            this.u900Power = vamCoeff * 26.65;
        else if (u900Power == 448 || u900Power == 450)
            this.u900Power = vamCoeff * 30;
        else if (u900Power == 460)
            this.u900Power = vamCoeff * 40;
        else if (u900Power == 478)
            this.u900Power = 60;
        else if (u900Power == 490)
            this.u900Power = 80;
        else if (u900Power == 65535 || u900Power == 0)
            this.u900Power = 0;
    }

    public double getU900Power() {
        return u900Power;
    }

    public void setPower(int power, int vam) {
        int vamCoeff = 1;
        if (vam > 0)
            vamCoeff = 2;
        if (power == 210)
            this.power = vamCoeff * .125;
        else if (power == 240)
            this.power = vamCoeff * 0.25;
        else if (power == 400)
            this.power = vamCoeff * 10;
        else if (power == 418)
            this.power = vamCoeff * 15;
        else if (power == 430)
            this.power = vamCoeff * 20;
        else if (power == 442)
            this.power = vamCoeff * 26.65;
        else if (power == 448 || power == 450)
            this.power = vamCoeff * 30;
        else if (power == 460)
            this.power = vamCoeff * 40;
        else if (power == 478)
            this.power = 60;
        else if (power == 490)
            this.power = 80;
        else if (power == 65535 || power == 0)
            this.power = 0;
    }

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

    public void setNodeBNumberOfOnAirFirstCarriersCells(int nodeBNumberOfOnAirFirstCarriersCells) {
        if (nodeBNumberOfFirstCarriersCells > 0) {
            this.firstCarrier = true;
            this.nodeBNumberOfCarriers++;
        }
        this.nodeBNumberOfOnAirFirstCarriersCells = nodeBNumberOfOnAirFirstCarriersCells;
    }

    public void setNodeBNumberOfOnAirSecondCarriersCells(int nodeBNumberOfOnAirSecondCarriersCells) {
        if (nodeBNumberOfOnAirSecondCarriersCells > 0) {
            this.secondCarrier = true;
            this.nodeBNumberOfCarriers++;
        }
        this.nodeBNumberOfOnAirSecondCarriersCells = nodeBNumberOfOnAirSecondCarriersCells;
    }

    public void setNodeBNumberOfOnAirThirdCarriersCells(int nodeBNumberOfOnAirThirdCarriersCells) {
        if (nodeBNumberOfOnAirThirdCarriersCells > 0) {
            this.thirdCarrier = true;
            this.nodeBNumberOfCarriers++;
        }
        this.nodeBNumberOfOnAirThirdCarriersCells = nodeBNumberOfOnAirThirdCarriersCells;
    }

    public void setNodeBNumberOfOnAirU900CarriersCells(int nodeBNumberOfOnAirU900CarriersCells) {
        if (nodeBNumberOfOnAirU900CarriersCells > 0) {
            this.u900 = true;
            this.nodeBNumberOfCarriers++;
        }
        this.nodeBNumberOfOnAirU900CarriersCells = nodeBNumberOfOnAirU900CarriersCells;
    }

    public int getNodeBNumberOfCarriers() {
        return nodeBNumberOfCarriers;
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
                case "70":
                    nodeBTxMode = Constants.uTxMode.DUAL_STACK;
                    break;
                default:
                    nodeBTxMode = Constants.uTxMode.FULL_IP;
            }
        } catch (NullPointerException e) {
            nodeBTxMode = Constants.uTxMode.FULL_IP;
        }
    }
}
