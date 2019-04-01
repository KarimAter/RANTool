package sample;

import java.util.ArrayList;

public class BtsHW {
    private String BscId, BcfId;
    private ArrayList<HwItem> hwItems = new ArrayList<>();

    public ArrayList<HwItem> getHwItems() {
        return hwItems;
    }

    void addHwItem(HwItem hwItem) {
        hwItems.add(hwItem);
    }

    String getBscId() {
        return BscId;
    }

    void setBscId(String bscId) {
        BscId = bscId;
    }

    String getBcfId() {
        return BcfId;
    }

    void setBcfId(String bcfId) {
        BcfId = bcfId;
    }
}
