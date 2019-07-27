package Helpers;

import java.util.LinkedHashMap;

public class Constants {
    public enum uTxMode {ATM, DUAL_STACK, FULL_IP}

    public enum gTxMode {ATM, PACKET_ABIS}

    public static String rfIdentifier = "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0";
    public String sMIdentifier = "0.0.0.0.0.0.0.0";
    public String txIdentifier = "0.0.0.0.0.0";

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
}
