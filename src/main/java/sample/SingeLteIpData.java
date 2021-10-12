package sample;

public class SingeLteIpData {

    String ipInterfaceId, vLanInterfaceId, ipAddress, vLanId;
    String service = "other";

    public String getIpInterfaceId() {
        return ipInterfaceId;
    }

    public void setIpInterfaceId(String ipInterfaceId) {
        this.ipInterfaceId = ipInterfaceId;
    }

    public String getvLanInterfaceId() {
        return vLanInterfaceId;
    }

    public void setvLanInterfaceId(String vLanInterfaceId) {
        this.vLanInterfaceId = vLanInterfaceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getvLanId() {
        return vLanId;
    }

    public void setvLanId(String vLanId) {
        this.vLanId = vLanId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "SingeLteIpData{" +
                "ipInterfaceId='" + ipInterfaceId + '\'' +
                ", vLanInterfaceId='" + vLanInterfaceId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", vLanId='" + vLanId + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
