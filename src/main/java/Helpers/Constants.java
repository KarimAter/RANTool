package Helpers;

import java.util.LinkedHashMap;

public class Constants {
    public enum uTxMode {ATM, DUAL_STACK, FULL_IP}

    public enum gTxMode {ATM, PACKET_ABIS}

    static final LinkedHashMap<Integer, Double> POWER_MAP = new LinkedHashMap<Integer, Double>() {
        {
            put(0, 0.0);
            put(210, 0.125);
            put(240, 0.25);
            put(390, 9.0);
            put(400, 10.0);
            put(418, 15.0);
            put(430, 20.0);
            put(431, 20.0);
            put(435, 22.3);
            put(436, 22.3);
            put(442, 26.65);
            put(444, 26.65);
            put(448, 30.0);
            put(447, 30.0);
            put(449, 30.0);
            put(450, 30.0);
            put(451, 30.0);
            put(457, 37.0);
            put(460, 40.0);
            put(461, 40.0);
            put(462, 40.0);
            put(463, 42.6);
            put(464, 43.6);
            put(471, 53.3);
            put(472, 53.3);
            put(473, 53.3);
            put(477, 60.0);
            put(478, 60.0);
            put(486, 72.4);
            put(490, 80.0);
            put(65535, 0.0);
        }
    };

    static final LinkedHashMap<Integer, String> ARFCN_MAP = new LinkedHashMap<Integer, String>() {
        {
            put(1634, "L1800");
            put(1635, "L1800");
            put(3622, "L900");
            put(224, "L2100-5M");
            put(199, "L2100-10M");
        }
    };
    public static final LinkedHashMap<String, String> RF_PRODUCT_CODE = new LinkedHashMap<String, String>() {
        {
            put("472100B", "FRGP");
            put("471483A", "FRGF");
            put("472573A.104", "FXDB");
            put("472810A.102", "FRGT");
            put("473440A.101", "FRGX");
            put("472100A.101", "FRGP");
            put("472573A.102", "FXDB");
            put("472573A.103", "FXDB");
            put("472956A.101", "FRGU");
            put("472083A.101", "FXDA");
            put("472083A.203", "FXDA");
            put("FWGM_FRGM", "FRGM");
            put("471232A", "FRGD");
            put("471834A", "FRGL");
            put("471835A", "FRGM");
            put("471231A", "FRGC");
            put("472956A.102", "FRGU");
            put("474198A.101", "AREA");
            put("474800A.102", "ARGA");
            put("474840A", "ARDA");
            put("475122A", "ARDB");
            put("474257A", "AHDB");
            put("475432A", "AZHL");
            put("473941A", "AZHA");
        }
    };


    public static final LinkedHashMap<Integer, String> rfMap = new LinkedHashMap<Integer, String>() {
        {
            put(0, "FXDA");
            put(1, "FXDB");
            put(2, "FXEA");
            put(3, "FXEB");
            put(4, "FXX");
            put(5, "FXED");
            put(6, "FRGC");
            put(7, "FRGD");
            put(8, "FRGF");
            put(9, "FRGL");
            put(10, "FRGM");
            put(11, "FRGP");
            put(12, "FRGT");
            put(13, "FRGU");
            put(14, "FRGX");
            put(15, "FXEF");
            put(16, "AREA");
            put(17, "ARGA");
            put(18, "ARDA");
            put(19, "ARDB");
            put(20, "AHDB");
            put(21, "AZHL");
            put(22, "AZHA");
        }
    };

    public static final LinkedHashMap<Integer, String> smMap = new LinkedHashMap<Integer, String>() {
        {
            put(0, "ESMB");
            put(1, "ESMC");
            put(2, "FSMF");
            put(3, "FSMB");
            put(4, "FSMD");
            put(5, "FSME");
            put(6, "FBBA");
            put(7, "FBBC");
            put(8, "ASIA");
            put(9, "ASIB");
            put(10, "ABIA");
            put(11, "ABIC");
            put(12, "ABIO");

        }
    };

    public static final LinkedHashMap<Integer, String> txMap = new LinkedHashMap<Integer, String>() {
        {
            put(0, "FIQA");
            put(1, "FIQB");
            put(2, "FTIF");
            put(3, "FTIA");
            put(4, "FTIB");
            put(5, "FTPB");
        }
    };


    public static final LinkedHashMap<String, String> hwShortNameConversionMap = new LinkedHashMap<String, String>() {
        {
            put("FPFD-ODM", "FPFD");
            put("BB Extension Outdoor Sub-Module FBBA", "FBBA");
            put("FXDB", "FXDB");
            put("FXED", "FXED");
            put("FXEB", "FXEB");
            put("FTIF", "FTIF");
            put("FPFD", "FPFD");
            put("Flexi Baseband Sub-Module FBBC", "FBBC");
            put("Flexi System Module Outdoor FSMF", "FSMF");
            put("FRGT", "FRGT");
            put("RET", "RET");
            put("SingleAntennaDevice", "SAD");
            put("FRGP", "FRGP");
            put("FRGX", "FRGX");
            put("FXEA", "FXEA");
            put("AREA", "AREA");
            put("FRGU", "FRGU");
            put("LNA", "LNA");
            put("FXEF", "FXEF");
            put("ARGA", "ARGA");
            put("CABINET", "CABINET");
            put("ABIA AirScale Capacity", "ABIA");
            put("WMHD", "WMHD");
            put("Other", "Other");
            put("MultiAntennaDevice", "MAD");
            put("ASIA AirScale Common", "ASIA");
            put("FXDA", "FXDA");
            put("AMIA AirScale Indoor Subrack", "AMIA");
            put("AMOB AirScale Outdoor Subrack", "AMOB");
            put("CORE_ASIA AirScale Common", "ASIA");

        }
    };
}
