package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class SiteMapper {

    static List<StatBox> getGConfigMap(ArrayList<Cabinet> gCabinets) {
        Map<String, List<StatBox>> uStats = gCabinets.stream().map(cabinet -> {
            StatBox statBox = new StatBox();
            statBox.setControllerId(((BCF) cabinet).getBSCName());
            statBox.setCode(cabinet.getCode());
            ArrayList<Integer> params = new ArrayList<>();
            params.add(((BCF) cabinet).getNewCellCount());
            params.add(((BCF) cabinet).getNewOnAirCount());
            params.add(((BCF) cabinet).getNumberOfTRXs());
            statBox.setParam(params);
            return statBox;
        }).collect(Collectors.groupingBy(StatBox::getControllerId));

        return uStats.entrySet().stream().map(stringListEntry -> {
            StatBox statBox = new StatBox();

            ArrayList<Integer> outtt = stringListEntry.getValue().stream().map(StatBox::getParam).reduce(
                    (integers, integers2) -> {
                        ArrayList<Integer> output = new ArrayList<>();
                        for (int i = 0; i < integers.size(); i++) {
                            output.add(integers.get(i) + integers2.get(i));
                        }
                        return output;
                    }).get();
            statBox.setControllerId(stringListEntry.getKey());
            statBox.setParam(outtt);
            return statBox;

        }).collect(Collectors.toList());
    }

    static Map<String, List<Integer>> getGSitesMap(ArrayList<Cabinet> gCabinets) {
        List<StatBox> sites = gCabinets.stream().map(cabinet -> {
            StatBox statBox = new StatBox();
            statBox.setControllerId(((BCF) cabinet).getBSCName());
            statBox.setCode(cabinet.getCode());
            ArrayList<Integer> params = new ArrayList<>();
            params.add(cabinet.getOnAir() == 1 &&
                    cabinet.getTxMode().equalsIgnoreCase("PACKET ABIS") ? 1 : 0);
            statBox.setParam(params);
            return statBox;
        }).collect(Collectors.toList());

        Map<String, List<StatBox>> sitesCount = sites.stream()
                .collect(Collectors.groupingBy(StatBox::getControllerId));


        Map<String, List<Integer>> gSitesTable = new HashMap<>();
        sitesCount.forEach((s, statBoxes) -> {
            List<Integer> counts = new ArrayList<>();
            counts.add(statBoxes.stream().distinct().collect(Collectors.toList()).size());
            counts.add(statBoxes.stream().filter(statBox -> statBox.getParam().get(0) == 1).distinct().collect(Collectors.toList()).size());
            gSitesTable.put(s, counts);
        });
        return gSitesTable;
    }


    static List<StatBox> getUConfigMap(ArrayList<Cabinet> uCabinets) {
        Map<String, List<StatBox>> uStats = uCabinets.stream().map(cabinet -> {
            StatBox statBox = new StatBox();
            statBox.setControllerId(cabinet.getControllerId());
            statBox.setCode(cabinet.getCode());
            ArrayList<Integer> params = new ArrayList<>();
            params.add(cabinet.getNumberOfCells());
            params.add(cabinet.getNumberOfOnAirCells());
            Map<String, Integer> r99CountMap = ((NodeB) cabinet).getR99CountMap();
            r99CountMap.forEach((key, value) -> params.add(value));
            statBox.setParam(params);
            return statBox;
        }).collect(Collectors.groupingBy(StatBox::getControllerId));


        return uStats.entrySet().stream().map(stringListEntry -> {
            StatBox statBox = new StatBox();

            ArrayList<Integer> outtt = stringListEntry.getValue().stream().map(StatBox::getParam).reduce(
                    (integers, integers2) -> {
                        ArrayList<Integer> output = new ArrayList<>();
                        for (int i = 0; i < integers.size(); i++) {
                            output.add(integers.get(i) + integers2.get(i));
                        }
                        return output;
                    }).get();
            statBox.setControllerId(stringListEntry.getKey());
            statBox.setParam(outtt);
            return statBox;

        }).collect(Collectors.toList());
    }

    static Map<String, List<Integer>> getUSitesMap(ArrayList<Cabinet> uCabinets) {
        List<StatBox> sites = uCabinets.stream().map(cabinet -> {
            StatBox statBox = new StatBox();
            statBox.setControllerId(cabinet.getControllerId());
            statBox.setCode(cabinet.getCode());
            ArrayList<Integer> params = new ArrayList<>();
            params.add(((NodeB) cabinet).isU900() ? 1 : 0);
            params.add(((NodeB) cabinet).isThirdCarrier() ? 1 : 0);
            params.add(cabinet.getNumberOfOnAirCells() > 0 &&
                    cabinet.getTxMode().equalsIgnoreCase("FULL IP") ? 1 : 0);
            params.add(((NodeB) cabinet).isRfSharing() ? 1 : 0);
            statBox.setParam(params);
            return statBox;
        }).collect(Collectors.toList());


        Map<String, List<StatBox>> sitesCount = sites.stream()
                .collect(Collectors.groupingBy(StatBox::getControllerId));

        Map<String, List<Integer>> uSitesTable = new HashMap<>();
        sitesCount.forEach((s, statBoxes) -> {
            List<Integer> counts = new ArrayList<>();
            counts.add(statBoxes.stream().distinct().collect(Collectors.toList()).size());
            counts.add(statBoxes.stream().filter(statBox -> statBox.getParam().get(0) == 1).distinct().collect(Collectors.toList()).size());
            counts.add(statBoxes.stream().filter(statBox -> statBox.getParam().get(1) == 1).distinct().collect(Collectors.toList()).size());
            counts.add(statBoxes.stream().filter(statBox -> statBox.getParam().get(2) == 1).distinct().collect(Collectors.toList()).size());
            counts.add(statBoxes.stream().filter(statBox -> statBox.getParam().get(3) == 1).distinct().collect(Collectors.toList()).size());
            uSitesTable.put(s, counts);
        });
        return uSitesTable;
    }


    static List<StatBox> getLConfigMap(ArrayList<Cabinet> lCabinets) {
        Map<String, List<Cabinet>> regions = lCabinets.stream().collect(Collectors.groupingBy(Cabinet::getRegion));
        return regions.entrySet().stream().map(stringListEntry -> {

            List<Cabinet> values = stringListEntry.getValue();
            StatBox statBox = new StatBox();
            statBox.setControllerId(stringListEntry.getKey());
            ArrayList<Integer> params = new ArrayList<>();
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).getMimo() == 0).collect(Collectors.toList()).size());
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).getMimo() == 30 || ((EnodeB) cabinet).getMimo() == 40).collect(Collectors.toList()).size());
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).getMimo() == 43).collect(Collectors.toList()).size());
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).getBw() == 50).collect(Collectors.toList()).size());
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).getBw() == 100).collect(Collectors.toList()).size());
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).getBw() == 150).collect(Collectors.toList()).size());
            params.add(values.stream().filter(cabinet -> ((EnodeB) cabinet).isCarrierAggregation()).collect(Collectors.toList()).size());
            statBox.setParam(params);
            return statBox;
        }).collect(Collectors.toList());


    }

    static List<StatBox> getCarriersMap(ArrayList<Cabinet> uCabinets, Predicate<Cabinet> carrierType) {

        List<Cabinet> collect = uCabinets.stream().filter(carrierType).collect(Collectors.toList());
        return collect.stream().map(cabinet -> {
            StatBox statBox = new StatBox();
            statBox.setControllerId(cabinet.getControllerId());
            statBox.setCode(cabinet.getCode());
            statBox.setName(cabinet.getName());
            return statBox;
        }).distinct().collect(Collectors.toList());

    }

}
