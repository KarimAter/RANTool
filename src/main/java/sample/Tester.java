package sample;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

        File xmlFile = new File("C:\\Ater\\dumps\\2020\\W36\\hw_xmls\\PLMN-PLMN_MRBTS-20346_SRAN_HW.xml");
//        File xmlFile = new File("C:\\Ater\\dumps\\2020\\W23\\home\\omc\\CustomizedScripts\\hw-data\\UPLOAD" +
//                "\\MRBTS20001_lteHw_973657_22836_UL.xml");
        try {
            parseSBTSHardwareXML(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    static Hardware parseSBTSHardwareXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<HwItem> hwItems = new ArrayList<>();
        String sbtsId = "";
        boolean sbtsIdextracted = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList nList = document.getElementsByTagName("raml");
        Node hwData = nList.item(0);

        NodeList hwList = hwData.getChildNodes();

        Node cmdata = hwList.item(1);
        NodeList childNodes = cmdata.getChildNodes();
        int managedObjectsSize = childNodes.getLength();
        int w = 0;

        for (int i = 0; i < managedObjectsSize; i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals("managedObject")) {
                NamedNodeMap attributes = item.getAttributes();
                String className = attributes.getNamedItem("class").getNodeValue();
                System.out.println(className);
                if (!sbtsIdextracted) {
                    String mrbtsLongName = attributes.getNamedItem("distName").getNodeValue();
                    int lastIndex = mrbtsLongName.lastIndexOf("MRBTS-");
                    sbtsId = mrbtsLongName.substring(lastIndex + 6, lastIndex + 11);
                    sbtsIdextracted = true;
                    System.out.println(sbtsId);
                }
                if (!className.equalsIgnoreCase("SMOD_CORE") && className.contains("MOD")) {
                    HashMap<String, String> kvalue = new HashMap<>();
                    w++;
                    NodeList childNodes1 = item.getChildNodes();
                    int length = childNodes1.getLength();
                    for (int j = 0; j < length; j++) {
                        Node item1 = childNodes1.item(j);
                        if (item1 != null) {
                            NamedNodeMap attributes1 = item1.getAttributes();
                            if (attributes1 != null) {
                                for (int k = 0; k < attributes1.getLength(); k++) {
                                    Node item2 = attributes1.item(k);
                                    kvalue.put(item2.getNodeValue(), item1.getFirstChild().getNodeValue());
                                    int x = 1;
                                }
                            }
                            int x = 9;
                        }
                    }

                    HwItem hwItem = new HwItem();
//                    eNobeBSerials.add(unitSerial);
                    String userLabel = kvalue.get("inventoryUnitType");
                    hwItem.setUserLabel(userLabel.substring(userLabel.length() - 4));
                    hwItem.setSerialNumber(kvalue.get("serialNumber"));
                    hwItem.setIdentificationCode(kvalue.get("vendorUnitTypeNumber"));
//                                eNodeBHW.addHwItem(hwItem);
                    hwItems.add(hwItem);
                    int x = 1;
                }

            }

        }

//        System.out.println(w);
        Hardware hardware = new Hardware(hwItems);
        hardware.setUniqueName("4G_" + sbtsId);
//        hardware.setWeek('W' + weekName);
        return hardware;
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

    private static String generateIdentifier
            (HashMap<String, Integer> siteHwMap, LinkedHashMap<Integer, String> hwTypeMap) {
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
