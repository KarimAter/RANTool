package sample;

import java.util.ArrayList;

public class ENodeBHW {
    private String mrbtsId;
    private ArrayList<HwItem> hwItems = new ArrayList<>();

    public String getMrbtsId() {
        return mrbtsId;
    }

    public void setMrbtsId(String mrbtsId) {
        this.mrbtsId = mrbtsId;
    }

    ArrayList<HwItem> getHwItems() {
        return hwItems;
    }

    void addHwItem(HwItem hwItem) {
        hwItems.add(hwItem);
    }
}
