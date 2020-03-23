package sample;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NodeConfiguration {

    private String key, sectorsMapping, linksMappingString;
    private ArrayList<SectorConfiguration> sectorsConfiguration;

    public NodeConfiguration(String key) {
        this.key = key;
        sectorsConfiguration = new ArrayList<>();
    }

    // generate the connection type mapping string per sector
    private static Function<Map.Entry<String, List<SectorConfiguration>>, String> formatConnectionFormat() {

        return stringListEntry -> {

            String connectionTypeForAllCarriers = stringListEntry.getValue().stream()
                    .map(SectorConfiguration::getConnectionType).distinct().collect(Collectors.joining(""));

            String note = stringListEntry.getValue().stream()
                    .map(SectorConfiguration::getNote).distinct().collect(Collectors.joining(""));

            String finalHardwareStrings = stringListEntry.getValue().stream()
                    .map(SectorConfiguration::getHardwareConnection).distinct().collect(Collectors.joining("")).replaceAll(" ", "");

            return "S" + stringListEntry.getKey() + ":" + connectionTypeForAllCarriers + note + finalHardwareStrings;
        };
    }

    void extractSectorsMapping() {

        String connectionType2100;
        if (key.equals("28_88") || key.equals("14_175") || key.equals("58_50") || key.equals("62_12") || key.equals("48_289")) {
            int x = 1;
        }

        // extract map of 2100 sectors with sectors id available
        Map<String, List<SectorConfiguration>> sectorsMap2100 = sectorsConfiguration.stream().filter(s -> !s.isU9Sector() && !s.isNoSectorId())
                .collect(Collectors.groupingBy(SectorConfiguration::getSectorId));
        connectionType2100 = sectorsMap2100.entrySet().stream().map(formatConnectionFormat()).collect(Collectors.joining(", "));


        // for missing sector Ids
        if (sectorsMap2100.size() == 0) {
            connectionType2100 = sectorsConfiguration.stream().filter(s -> s.getDefaultCarrier().equals("10612"))
                    .map(SectorConfiguration::getSectorConnectionString).collect(Collectors.joining(", "));
        }

        // extract map of U900 sectors with first U9 carrier only
        String connectionTypeU9 = sectorsConfiguration.stream().filter(s -> s.isU9Sector() && s.getDefaultCarrier().equals("2988"))
                .map(SectorConfiguration::extractConnection).collect(Collectors.joining(", "));
        sectorsMapping = connectionType2100 + " \n" + connectionTypeU9;

    }

    void extractLinksMapping() {

        // get the maximum link number
        Integer maxLink = sectorsConfiguration.stream().map(sectorConfiguration -> Arrays.stream(sectorConfiguration.getLinkId().split(","))
                .mapToInt(Integer::valueOf).max().getAsInt()).reduce(Integer::max).get();

        // get a string list for links from 1 to maximum link number
        List<String> linkNumbers = IntStream.rangeClosed(1, maxLink).boxed().map(String::valueOf).collect(Collectors.toList());

        Map<String, List<SectorConfiguration>> linksMap = new HashMap<>();

        // getting all sectors having the link Id

        linkNumbers.forEach(linkId -> {
            linksMap.put(linkId, sectorsConfiguration.stream().filter(SectorConfiguration.isPresentOnLink(linkId))
                    .collect(Collectors.toList()));
        });

        // getting number of cells per link
        Map<String, Long> numberOfCellsOnLink = new HashMap<>();

        linksMap.forEach((linkId, list) -> numberOfCellsOnLink.put(linkId, sectorsConfiguration.stream().filter(SectorConfiguration.isPresentOnLink(linkId)).count()));

        // getting Rfs per link
        Map<String, List<String>> linksRfsMap = linksMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().stream().map(sectorConfiguration -> {
                    if (sectorConfiguration.getFirstRfNumber().contains("1." + entry.getKey() + "."))
                        return sectorConfiguration.getFirstRfName() + sectorConfiguration.getFirstRfNumber();
                    else
                        return sectorConfiguration.getSecondRfName().replace("-", "") + sectorConfiguration.getSecondRfNumber();
                }).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList())));

        // getting sectors per Rf
        Map<String, String> linksSectors = linksMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().stream()
                        .map(sectorName -> "S" + sectorName.getSectorId()).collect(Collectors.toList())
                        .stream().distinct().collect(Collectors.joining(","))));

        // TODO: 3/22/2020 check the above i can get all sector as U and first and second

        // generates the link configuration string
        linksMappingString = linksMap.entrySet().stream().map(stringListEntry -> {
            String linkNumber = stringListEntry.getKey();
            return "Link" +
                    linkNumber + ":[" +
                    linksRfsMap.get(linkNumber) +
                    "(" +
                    linksSectors.get(linkNumber) +
                    ")(" +
                    numberOfCellsOnLink.get(linkNumber) +
                    ")]";
        }).collect(Collectors.joining(", "));
        int x = 2;
    }

    public void addSectorConfiguration(SectorConfiguration sectorConfiguration) {
        sectorsConfiguration.add(sectorConfiguration);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return sectorsMapping + "___" + linksMappingString;
    }
}
