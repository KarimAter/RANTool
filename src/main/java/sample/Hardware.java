package sample;

import Helpers.Constants;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Hardware {
    private int tech;
    private ArrayList<HwItem> hwItems = new ArrayList<>();
    private Map<String, Long> modules = new LinkedHashMap<>();
    private String uniqueName, rfString, smString, txString;
    private String rfIdentifier = "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0";
    private String smIdentifier = "0.0.0.0.0.0.0.0";
    private String txIdentifier = "0.0.0.0.0.0";
    private String week = "W0";
    private String code;
    private String name;

    // Constructor: generate hardware from hardware items
    public Hardware(ArrayList<HwItem> hwItems) {
        this.hwItems = hwItems;
        generate();
    }

    // Constructor: generate hardware from identifiers
    public Hardware(String rfIdentifier, String smIdentifier, String txIdentifier) {
        extractHwFromIdentifiers(Constants.rfMap, rfIdentifier);
        extractHwFromIdentifiers(Constants.smMap, smIdentifier);
        extractHwFromIdentifiers(Constants.txMap, txIdentifier);
        this.rfIdentifier = rfIdentifier;
        this.smIdentifier = smIdentifier;
        this.txIdentifier = txIdentifier;

    }


    // Constructor: generate hardware from identifiers
    public Hardware(List<String> rfIdentifier, List<String> smIdentifier, List<String> txIdentifier) {
        for (int i = 0; i < rfIdentifier.size(); i++) {
            extractHwFromIdentifiers(Constants.rfMap, rfIdentifier.get(i));
            extractHwFromIdentifiers(Constants.smMap, smIdentifier.get(i));
            extractHwFromIdentifiers(Constants.txMap, txIdentifier.get(i));
        }
        this.rfIdentifier = generateIdentifier(modules, Constants.rfMap);
        this.smIdentifier = generateIdentifier(modules, Constants.smMap);
        this.txIdentifier = generateIdentifier(modules, Constants.txMap);
    }


    public Hardware(String week) {
        this.week = week;
    }

    // get hardware from identifiers, and generate module map and hw strings
    private void extractHwFromIdentifiers(LinkedHashMap<Integer, String> hwTypeMap, @NotNull String identifier) {
        try {
            List<Integer> typeMapIndices = Stream.of(identifier.split("\\.")).
                    map(Integer::valueOf).
                    collect(Collectors.toList());
            hwTypeMap.forEach((key, value) -> {
                for (int i = 0; i < typeMapIndices.get(key); i++) {
                    hwItems.add(new HwItem(value));
                }
            });
            generateHwStructure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // generate Hardware structure and identifiers
    private void generate() {
        generateHwStructure();
        // generate Identifiers
        rfIdentifier = generateIdentifier(modules, Constants.rfMap);
        smIdentifier = generateIdentifier(modules, Constants.smMap);
        txIdentifier = generateIdentifier(modules, Constants.txMap);
    }

    // create site module map, and hardware strings
    private void generateHwStructure() {
        // Generate count of each module
        modules = countModules(hwItems);
        // generate hardware Strings
        rfString = concatenateHwString(filterHw(modules, "FR", "FX"));
        smString = concatenateHwString(filterHw(modules, "ES", "FS", "FB"));
        txString = concatenateHwString(filterHw(modules, "FT", "FI"));
    }


    // generate identifier according to module type
    private String generateIdentifier(Map<String, Long> siteHwMap, Map<Integer, String> hwTypeMap) {
        return hwTypeMap.entrySet().stream()
                .map(longStringEntry -> String.valueOf(siteHwMap.get(longStringEntry.getValue())))
                .collect(joining(".")).replace("null", "0");
    }

    // counts each hwItem and returns them to sortedMap by hwItemName
    private Map<String, Long> countModules(ArrayList<HwItem> hwItems) {
        return hwItems.stream()
                .collect(Collectors.groupingBy(HwItem::getUserLabel, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    // filterHw modules according to hardware type
    private Map<String, Long> filterHw(Map<String, Long> modules, String prefix1, String prefix2) {
        return modules.entrySet().stream()
                .filter(name -> name.getKey().contains(prefix1) || name.getKey().contains(prefix2))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, LinkedHashMap::new));

    }

    private Map<String, Long> filterHw(Map<String, Long> modules, String prefix1, String prefix2, String prefix3) {
        return modules.entrySet().stream()
                .filter(name -> name.getKey().contains(prefix1) || name.getKey().contains(prefix2) || name.getKey().contains(prefix3))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, LinkedHashMap::new));
    }

    // creates the hardware String
    private String concatenateHwString(Map<String, Long> filteredHw) {
        return filteredHw.entrySet().stream()
                .map(stringLongEntry -> stringLongEntry.getValue() + stringLongEntry.getKey())
                .collect(joining(" "));
    }


    public static Function<Map.Entry<String, List<Cabinet>>, Hardware> HwItemsAdder(int tech) {
        return cabins -> {
            ArrayList<HwItem> hwItems = new ArrayList<>();
            cabins.getValue().forEach(cabinet -> {
                hwItems.addAll(cabinet.getHardware().getHwItems());
            });
            Hardware hardware = new Hardware(hwItems);
            hardware.setCode(cabins.getKey());
            hardware.setName(cabins.getValue().get(0).getName());
            hardware.setTech(tech);
            return hardware;
        };
    }




    void addHwItem(HwItem hwItem) {
        hwItems.add(hwItem);
    }

    private void addModule(HwItem hwItem) {
        modules.put(hwItem.getUserLabel(), modules.get(hwItem.getUserLabel()) + 1);
    }

    public int getTech() {
        return tech;
    }

    public void setTech(int tech) {
        this.tech = tech;
    }

    public Map<String, Long> getModules() {
        return modules;
    }

    public void setModules(Map<String, Long> modules) {
        this.modules = modules;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }


    public String getRfString() {
        return rfString;
    }

    public void setRfString(String rfString) {
        this.rfString = rfString;
    }

    public String getSmString() {
        return smString;
    }

    public void setSmString(String smString) {
        this.smString = smString;
    }

    public String getTxString() {
        return txString;
    }

    public void setTxString(String txString) {
        this.txString = txString;
    }

    public String getRfIdentifier() {
//        return rfIdentifier==null ? "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0":rfIdentifier;
        return rfIdentifier;
    }

    public void setRfIdentifier(String rfIdentifier) {
        this.rfIdentifier = rfIdentifier;
    }

    public String getSmIdentifier() {
//        return smIdentifier==null ? "0.0.0.0.0.0.0.0":smIdentifier;
        return smIdentifier;
    }

    public void setSmIdentifier(String smIdentifier) {
        this.smIdentifier = smIdentifier;
    }

    public String getTxIdentifier() {
//        return txIdentifier==null ? "0.0.0.0.0.0":txIdentifier;
        return txIdentifier;
    }

    public void setTxIdentifier(String txIdentifier) {
        this.txIdentifier = txIdentifier;
    }

    public ArrayList<HwItem> getHwItems() {
        return hwItems;
    }

    public void setHwItems(ArrayList<HwItem> hwItems) {
        this.hwItems = hwItems;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }


    public int getModuleValue(String moduleName) {
        return this.modules.get(moduleName) == null ? 0 : this.modules.get(moduleName).intValue();

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


