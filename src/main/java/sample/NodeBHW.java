package sample;

import java.util.ArrayList;

class NodeBHW {

    private String MOID, RncID, WBTSId;
    private ArrayList<HwItem> hwItems = new ArrayList<>();

    ArrayList<HwItem> getHwItems() {
        return hwItems;
    }

    void addHwItem(HwItem hwItem) {
        hwItems.add(hwItem);
    }

    String getRncID() {
        return RncID;
    }

    void setRncID(String rncID) {
        RncID = rncID;
    }

    String getWBTSId() {
        return WBTSId;
    }

    void setWBTSId(String WBTSId) {
        this.WBTSId = WBTSId;
    }


}
