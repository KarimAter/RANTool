package sample;

import java.util.HashMap;
import java.util.Map;

public class LteIps {

    String ipVlanString, ifstring, secIf, remoteSec, s1If, ifvflink;
    Map<String, SingeLteIpData> map = new HashMap<>();

    public LteIps(String ipIdentfier) {
        String[] split = ipIdentfier.split(";");
        this.ipVlanString = split[0];
        this.ifstring = split[1];
        this.secIf = split[2];
        this.remoteSec = split[3];
        this.s1If = split[4];

        process();
    }

    private void process() {
        String[] ifs = ifstring.split(",");
        String[] ipVlans = ipVlanString.split(",");
//        String[] vLans = vlanString.split(",");
//        String[] iFvFs = ifvflink.split(",");
//        Map<String, String> iFivMap = Stream.of(ifvflink.split(",")).map(s -> s.split("/")).collect(Collectors.toMap(a -> a[0], b -> b[1]));

        for (int i = 0; i < ifs.length; i++) {
            String iF = ifs[i];
            String[] split = ipVlans[i].split("&&");
            String iP = split[0];

//            String[] singeRelation = iFvFs[i].split("/");
            String vLan = split.length == 2 ? split[1] : "N/A";
            SingeLteIpData singeLteIpData = new SingeLteIpData();
            singeLteIpData.setIpInterfaceId(iF);
            singeLteIpData.setIpAddress(iP);
            singeLteIpData.setvLanId(vLan);

            if (iF.equals(secIf))
                singeLteIpData.setService("SecIp");
            else if (iF.equals(s1If))
                singeLteIpData.setService("S1Ip");
            map.put(iF, singeLteIpData);
        }

    }

    public String getRemoteSec() {
        return remoteSec;
    }

    public String getSecIf() {
        return secIf;
    }

    public String getS1If() {
        return s1If;
    }

    public Map<String, SingeLteIpData> getMap() {
        return map;
    }

    @Override
    public String toString() {


        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((iF, singeLteIpData) -> {
            stringBuilder.append("ipInterfaceId: ").append(iF)
                    .append(singeLteIpData.toString());
        });
        return stringBuilder.toString();
    }
}
