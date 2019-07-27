package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class Tester {

    private static String x1, y1;

    public static void main(String[] args) {
        int tech;
        ArrayList<HwItem> hwItems = new ArrayList<>();
        Map<String, Long> modules = new LinkedHashMap<>();
        String uniqueName;

        hwItems.add(new HwItem("FXDA"));
        hwItems.add(new HwItem("FXDA"));
        hwItems.add(new HwItem("FXDB"));
        hwItems.add(new HwItem("FXDB"));
        hwItems.add(new HwItem("FRGT"));
        hwItems.add(new HwItem("FRGT"));
        hwItems.add(new HwItem("FRGT"));
        hwItems.add(new HwItem("FTIF"));
        hwItems.add(new HwItem("FSMF"));


        String string = "2.1.0.0.0.0.0.0.0.0.0.0.1.0.0.";
        // Generate count of each module
        modules = countModules(hwItems);
        modules.entrySet().forEach(System.out::println);
//        // generate hardware String
//        String rfString = Hardware.concatenateHwString(Hardware.filterHw(modules, "FR", "FX"));
//        String smString = Hardware.concatenateHwString(Hardware.filterHw(modules, "ES", "FS"));
//        String txString = Hardware.concatenateHwString(Hardware.filterHw(modules, "FT", "FI"));
//        ;
//////        // generate Identifier
//        String rfIdentifier = Hardware.generateIdentifier(modules, Constants.rfMap);
//        String smIdentifier = Hardware.generateIdentifier(modules, Constants.smMap);
//        String txIdentifier = Hardware.generateIdentifier(modules, Constants.txMap);


//        System.out.println(rfString);
//        System.out.println(smString);
//        System.out.println(txString);
//        System.out.println(rfIdentifier);
//        System.out.println(smIdentifier);
//        System.out.println(txIdentifier);
////
//        Hardware hardware = new Hardware(rfIdentifier, smIdentifier, txIdentifier);
//        hardware.getHwItems().forEach(x -> System.out.println(x.getUserLabel()));
//        System.out.println(hardware.getRfString());
//        System.out.println(hardware.getSmString());
//        System.out.println(hardware.getTxString());

    }


    private static Map<String, Long> countModules(ArrayList<HwItem> hwItems) {
//        Map<String, Integer> modules = new Lin<>();
//        Consumer<ArrayList<HwItem>> modulesCount = a -> {
//            a.forEach(hwItem -> {
//                int x = Collections.frequency(hwItems, hwItem);
//                modules.put(hwItem.getUserLabel(), x);
//            });
//        };
//        modulesCount.accept(hwItems);
//        hwItems.sort(Comparator.comparing(HwItem::getUserLabel));
        return hwItems.stream()
                .collect(Collectors.groupingBy(HwItem::getUserLabel, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//                .entrySet().stream().sorted(comparingByKey()).
//                        collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2))
//                ;
    }


    private static HashMap<String, Integer> filtererMethod(HashMap<String, Integer> modules, String moduleType) {
        BiFunction<HashMap<String, Integer>, String, HashMap<String, Integer>> filterer = (a, modulePrefix) -> {
            HashMap<String, Integer> b;
            String[] parts = modulePrefix.split("-");
            b = a.entrySet().stream().
                    filter(entry -> entry.getKey().contains(parts[0]) || entry.getKey().contains(parts[1]))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));
            return b;
        };
        return filterer.apply(modules, moduleType);
    }

    private static String concatenaterMethod(HashMap<String, Integer> filteredHw) {
        Function<HashMap<String, Integer>, String> Concatenater = map -> {
            StringBuilder stringBuilder = new StringBuilder();
            map.entrySet()
                    .forEach(entry -> stringBuilder.append(entry.getValue() + entry.getKey() + " "));
            return stringBuilder.toString();
        };
        return Concatenater.apply(filteredHw);
    }

    private static String generateIdentifier(HashMap<String, Integer> siteHwMap, LinkedHashMap<Integer, String> hwTypeMap) {
        BiFunction<HashMap<String, Integer>, LinkedHashMap<Integer, String>, String> identifierGenerator = (siteMap, hwMap) -> {
            StringBuilder identifierString = new StringBuilder();
            hwMap.forEach((moduleCount, moduleName) -> {
                if (siteMap.containsKey(moduleName)) {
                    identifierString.append(siteMap.get(moduleName)).append(".");
                } else identifierString.append("0.");
            });
            return identifierString.toString();
        };
        return identifierGenerator.apply(siteHwMap, hwTypeMap);
    }
}
