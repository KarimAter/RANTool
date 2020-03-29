package sample;

import Helpers.Constants;
import Helpers.Utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SectorConfiguration {

    private String lCellId, antId, rModId, productCode, sModId, posInChain, linkId, firstLinkId, sectorNumber, connectionType, firstRfName, firstRfNumber, sectorId, cellName;
    private String defaultCarrier, note, sectorConnectionString, hardwareConnection;
    private String secondRfName, secondRfNumber, secondLinkId;
    private boolean noSectorId;
    private boolean u9Sector;
    private double power;

    // analyzeConfiguration the sector input to get the needed data
    public void analyzeSector() {
        List<String> rMods = Stream.of(rModId.split(",")).sorted().collect(Collectors.toList());
        int count = antId.length();
        String[] productsCode = productCode.split(",");
        sectorNumber = "S" + sectorId;
        switch (count) {

            case 1: {
                firstRfName = Constants.RF_PRODUCT_CODE.get(productCode);
                firstLinkId = String.valueOf(linkId.charAt(0));
                secondRfName = "";
                secondLinkId = "";
                connectionType = "A";
                note = "(SF)";
                firstRfNumber = "1." + firstLinkId + "." + posInChain;
                secondRfNumber = "";
                break;
            }
            case 3: {
                secondRfName = "";
                secondRfNumber = "";
                secondLinkId = "";
                note = "";
                if (sModId == null) {
                    note = ("Mcr");
                    firstRfNumber = "1.1.1";
                    firstLinkId = "";
                } else {
                    if (sModId.contains("2"))
                        note = "(Shr)";
                    connectionType = connnectionTypeGetter(rModId.split(","), antId.split(","));
                    firstRfName = Constants.RF_PRODUCT_CODE.get(productsCode[0]);
                    firstLinkId = String.valueOf(linkId.charAt(0));
                    firstRfNumber = "1." + firstLinkId + "." + posInChain.charAt(0);
                    if (connectionType.equals("C")) {
                        secondRfName = "-" + Constants.RF_PRODUCT_CODE.get(productsCode[1]);
                        secondLinkId = String.valueOf(linkId.charAt(2));
                        secondRfNumber = "1." + secondLinkId + "." + posInChain.charAt(2);
                    }
                }
                sectorConnectionString = sectorNumber + ":" + connectionType;
                break;
            }
            case 5: {
                highAntCount(productsCode);
                note = "(O)";
                break;
            }
            case 7: {
                highAntCount(productsCode);
                note = "(Shr)";
                break;
            }
        }
        hardwareConnection = " {" + firstRfName + firstRfNumber + secondRfName + secondRfNumber + "}";
        sectorConnectionString = sectorNumber + ":" + connectionType + note + hardwareConnection;

    }

    String extractConnection() {
        return sectorConnectionString;
    }

    // get the configuration for high count concatenation
    private void highAntCount(String[] productsCode) {
        int firstIndex = sModId.indexOf("1");
        int lastIndex = sModId.lastIndexOf("1");
        connectionType = connnectionTypeGetter(new String[]{String.valueOf(rModId.charAt(firstIndex)), String.valueOf(rModId.charAt(lastIndex))},
                new String[]{String.valueOf(antId.charAt(firstIndex)), String.valueOf(antId.charAt(lastIndex))});
        firstRfName = Constants.RF_PRODUCT_CODE.get(productsCode[firstIndex / 2]);
        firstLinkId = String.valueOf(linkId.charAt(firstIndex / 2));
        firstRfNumber = "1." + firstLinkId + "." + posInChain.charAt(firstIndex);
        secondRfName = "";
        secondRfNumber = "";
        secondLinkId = "";
        if (connectionType.equals("C")) {
            secondRfName = "-" + Constants.RF_PRODUCT_CODE.get(productsCode[lastIndex / 2]);
            secondLinkId = String.valueOf(linkId.charAt(lastIndex / 2));
            secondRfNumber = "1." + secondLinkId + "." + posInChain.charAt(lastIndex);
        }
    }

    // extracts connection type
    private String connnectionTypeGetter(String[] rMods, String[] ants) {
        if (!rMods[0].equals(rMods[1]))
            return "C";
        else {
            if (Math.abs(Integer.valueOf(ants[0]) - Integer.valueOf(ants[1])) == 1)
                return "A";
            else return "B";
        }
    }

    // checks if specific link id exists in sector
    static Predicate<SectorConfiguration> isPresentOnLink(String linkId) {

        return sectorConfiguration -> sectorConfiguration.getFirstLinkId().equals(linkId) || sectorConfiguration.getSecondLinkId().equals(linkId);
    }


    static Predicate<SectorConfiguration> isMainSector() {

        return sectorConfiguration -> sectorConfiguration.getDefaultCarrier().equals("10612") || sectorConfiguration.getDefaultCarrier().equals("2988");
    }


    @Override
    public int hashCode() {
        return Objects.hash(firstRfName, firstRfNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SectorConfiguration sectorConfiguration = (SectorConfiguration) obj;
        return Objects.equals(firstRfName, sectorConfiguration.getFirstRfName())
                && Objects.equals(firstRfNumber, sectorConfiguration.getFirstRfNumber());
    }


    public String getConnectionType() {
        return connectionType;
    }

    String getHardwareConnection() {
        return hardwareConnection;
    }

    String getFirstRfName() {
        return firstRfName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    String getSectorConnectionString() {
        return sectorConnectionString;
    }

    public void setlCellId(String lCellId) {
        this.lCellId = lCellId;
    }


    public void setAntId(String antId) {
        this.antId = antId;
    }


    public void setrModId(String rModId) {
        this.rModId = rModId;
    }


    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    public void setsModId(String sModId) {
        this.sModId = sModId;
    }


    public void setPosInChain(String posInChain) {
        this.posInChain = posInChain;
    }

    private String getFirstLinkId() {
        return firstLinkId;
    }

    public void setDefaultCarrier(String defaultCarrier) {
        if (defaultCarrier.equals("2988") || defaultCarrier.equals("3009"))
            u9Sector = true;
        this.defaultCarrier = defaultCarrier;
    }

    String getDefaultCarrier() {
        return defaultCarrier;
    }

    String getSectorId() {
        return sectorId;
    }

    String getLinkId() {
        return linkId == null ? "1" : linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    boolean isNoSectorId() {
        return noSectorId;
    }


    boolean isU9Sector() {
        return u9Sector;
    }

    private String getSecondLinkId() {
        return secondLinkId;
    }

    String getFirstRfNumber() {
        return firstRfNumber;
    }

    String getSecondRfNumber() {
        return secondRfNumber;
    }

    String getSecondRfName() {
        return secondRfName;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {

        this.cellName = cellName;
    }

    public void setSectorId(String sectorId) {

        if (sectorId != null && !sectorId.equals("0")) {
            this.sectorId = sectorId;
            this.noSectorId = false;
        } else {
            this.sectorId = lCellId;
            this.noSectorId = true;
        }
    }

    double getPower() {
        return power;
    }

    public void setPower(int power, int vam) {
        try {
            this.power = Utils.convertPower(power, vam);
        } catch (Exception e) {
            e.printStackTrace();
            this.power = 0;
        }
    }

    //
//    public void setSectorId(String sectorId) {
//
//
//
//        System.out.println(this.cellName);
//        if (sectorId != null) {
//
//            if (!sectorId.equals("0")) {
//                this.sectorId = sectorId;
//                this.noSectorId = false;
//                if (u9Sector) {
//                    this.sectorId = sectorId + "U";
//                }
//
//            } else if (sectorId.equals("0")) {
//                if (cellName != null) {
//                    List<String> collect = Arrays.stream(cellName.split("_")).filter(s -> s.length() == 2 || s.length() == 1).collect(Collectors.toList());
//                    collect.forEach(s -> {
//                        if (s.length() == 1 && Character.isDigit(s.charAt(0))) {
//                            this.sectorId = s;
//                            System.out.println("Case:1" + this.sectorId);
//                        } else if (s.length() == 2) {
//                            if (s.contains("S")) {
//                                this.sectorId = s.substring(1);
//                                System.out.println("Case:2S" + this.sectorId);
//                            }
//                        } else if (s.matches("\\d+")) {
//                            this.sectorId = s;
//                            System.out.println("Case:2-2digit" + this.sectorId);
//                        }
//                    });
//                    this.noSectorId = false;
//                    if (u9Sector) {
//                        this.sectorId = sectorId + "U";
//                        System.out.println("CaseU9" + this.sectorId);
//                    }
//                }
//            }
//        } else {
//            this.sectorId = lCellId;
//            this.noSectorId = true;
//        }
//
//    }

}
//else if (this.cellName != null) {
//            List<String> collect = Stream.of(cellName).filter(s -> s.length() == 2 || s.length() == 1).collect(Collectors.toList());
//            collect.forEach(s -> {
//                if (s.length() == 1 && Character.isDigit(s.charAt(0))) {
//                    this.sectorId = s;
//                    System.out.println("Case:1" + this.sectorId);
//                } else if (s.length() == 2) {
//                    if (s.contains("S")) {
//                        this.sectorId = s.substring(1);
//                        System.out.println("Case:2S" + this.sectorId);
//                    }
//                } else if (s.matches("\\d+")) {
//                    this.sectorId = s;
//                    System.out.println("Case:2-2digit" + this.sectorId);
//                }
//            });
//            if (u9Sector) {
//                this.sectorId = sectorId + "U";
//                System.out.println("CaseU9" + this.sectorId);
//            }
//        } else this.sectorId = lCellId;