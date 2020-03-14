package sample;

import Helpers.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class StatBox {

    private String controllerId, code, region, name;
    private ArrayList<Integer> param;

    String getControllerId() {
        return controllerId;
    }

    void setControllerId(String controllerId) {
        this.controllerId = controllerId;
        this.setRegion(Utils.mapToRegion(this.controllerId));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Integer> getParam() {
        return param;
    }

    public void setParam(ArrayList<Integer> param) {
        this.param = param;
    }

    public String getRegion() {
        return region;
    }

    private void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatBox statBox = (StatBox) o;
        return Objects.equals(code, statBox.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code);
    }
}
