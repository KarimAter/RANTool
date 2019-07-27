package sample;

import java.util.ArrayList;

public class NetworkImage {
    private ArrayList<GNode> gNodes;
    private ArrayList<UNode> uNodes;
    private ArrayList<LNode> lNodes;

    public NetworkImage() {
        gNodes = new ArrayList<>();
        uNodes = new ArrayList<>();
        lNodes = new ArrayList<>();
    }

    public ArrayList<GNode> getgNodes() {
        return gNodes;
    }

    public ArrayList<UNode> getuNodes() {
        return uNodes;
    }

    public ArrayList<LNode> getlNodes() {
        return lNodes;
    }

    public static class GNode {
        public GNode() {
        }

        private String siteName, siteCode;
        private double trxIdentifier, gTrxIdentifier;
        private double gRfModuleIdentifier, gSystemModuleIdentifier, gTransmissionModuleIdentifier;

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public double getTrxIdentifier() {
            return trxIdentifier;
        }

        public void setTrxIdentifier(double trxIdentifier) {
            this.trxIdentifier = trxIdentifier;
        }

        public double getgTrxIdentifier() {
            return gTrxIdentifier;
        }

        public void setgTrxIdentifier(double gTrxIdentifier) {
            this.gTrxIdentifier = gTrxIdentifier;
        }

        public double getgRfModuleIdentifier() {
            return gRfModuleIdentifier;
        }

        public void setgRfModuleIdentifier(double gRfModuleIdentifier) {
            this.gRfModuleIdentifier = gRfModuleIdentifier;
        }

        public double getgSystemModuleIdentifier() {
            return gSystemModuleIdentifier;
        }

        public void setgSystemModuleIdentifier(double gSystemModuleIdentifier) {
            this.gSystemModuleIdentifier = gSystemModuleIdentifier;
        }

        public double getgTransmissionModuleIdentifier() {
            return gTransmissionModuleIdentifier;
        }

        public void setgTransmissionModuleIdentifier(double gTransmissionModuleIdentifier) {
            this.gTransmissionModuleIdentifier = gTransmissionModuleIdentifier;
        }
    }

    class UNode {
        private String siteName, siteCode;
        private double processingSetsIdentifier, channelElementsIdentifier, carriersIdentifier, powerIdentifier;
        private double uRfModuleIdentifier, uSystemModuleIdentifier, uSystemModuleExtentionIdentifier, uTransmissionModuleIdentifier;

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public double getProcessingSetsIdentifier() {
            return processingSetsIdentifier;
        }

        public void setProcessingSetsIdentifier(double processingSetsIdentifier) {
            this.processingSetsIdentifier = processingSetsIdentifier;
        }

        public double getChannelElementsIdentifier() {
            return channelElementsIdentifier;
        }

        public void setChannelElementsIdentifier(double channelElementsIdentifier) {
            this.channelElementsIdentifier = channelElementsIdentifier;
        }

        public double getCarriersIdentifier() {
            return carriersIdentifier;
        }

        public void setCarriersIdentifier(double carriersIdentifier) {
            this.carriersIdentifier = carriersIdentifier;
        }

        public double getPowerIdentifier() {
            return powerIdentifier;
        }

        public void setPowerIdentifier(double powerIdentifier) {
            this.powerIdentifier = powerIdentifier;
        }

        public double getuRfModuleIdentifier() {
            return uRfModuleIdentifier;
        }

        public void setuRfModuleIdentifier(double uRfModuleIdentifier) {
            this.uRfModuleIdentifier = uRfModuleIdentifier;
        }

        public double getuSystemModuleIdentifier() {
            return uSystemModuleIdentifier;
        }

        public void setuSystemModuleIdentifier(double uSystemModuleIdentifier) {
            this.uSystemModuleIdentifier = uSystemModuleIdentifier;
        }

        public double getuSystemModuleExtentionIdentifier() {
            return uSystemModuleExtentionIdentifier;
        }

        public void setuSystemModuleExtentionIdentifier(double uSystemModuleExtentionIdentifier) {
            this.uSystemModuleExtentionIdentifier = uSystemModuleExtentionIdentifier;
        }

        public double getuTransmissionModuleIdentifier() {
            return uTransmissionModuleIdentifier;
        }

        public void setuTransmissionModuleIdentifier(double uTransmissionModuleIdentifier) {
            this.uTransmissionModuleIdentifier = uTransmissionModuleIdentifier;
        }
    }

    class LNode {
        private String siteName, siteCode;
        private double bwIdentifier;
        private double lRfModuleIdentifier, lSystemModuleIdentifier, gTransmissionModuleIdentifier;

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public double getBwIdentifier() {
            return bwIdentifier;
        }

        public void setBwIdentifier(double bwIdentifier) {
            this.bwIdentifier = bwIdentifier;
        }

        public double getlRfModuleIdentifier() {
            return lRfModuleIdentifier;
        }

        public void setlRfModuleIdentifier(double lRfModuleIdentifier) {
            this.lRfModuleIdentifier = lRfModuleIdentifier;
        }

        public double getlSystemModuleIdentifier() {
            return lSystemModuleIdentifier;
        }

        public void setlSystemModuleIdentifier(double lSystemModuleIdentifier) {
            this.lSystemModuleIdentifier = lSystemModuleIdentifier;
        }

        public double getgTransmissionModuleIdentifier() {
            return gTransmissionModuleIdentifier;
        }

        public void setgTransmissionModuleIdentifier(double gTransmissionModuleIdentifier) {
            this.gTransmissionModuleIdentifier = gTransmissionModuleIdentifier;
        }
    }
}
