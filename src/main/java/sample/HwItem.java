package sample;


public class HwItem {
    private String userLabel, serialNumber, identificationCode;

    public HwItem(String hwString) {
        this.userLabel = hwString;
    }

    public HwItem(String userLabel, String serialNumber, String identificationCode) {
        this.userLabel = userLabel;
        this.serialNumber = serialNumber;
        this.identificationCode = identificationCode;
    }

    public HwItem() {
    }

    public void setUserLabel(String userLabel) {
        if (userLabel.contains("CORE_"))
            this.userLabel = userLabel.replace("CORE_", "");
        else this.userLabel = userLabel;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    public String getUserLabel() {
        return userLabel;
    }

    @Override
    public boolean equals(Object obj) {
        return userLabel.equals(((HwItem) obj).getUserLabel());
    }
}
