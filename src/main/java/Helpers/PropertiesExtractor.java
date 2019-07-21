package Helpers;

public interface PropertiesExtractor {

//    default String extractSideCode(String baseStationName){
//        int nameLength = baseStationName.length();
//        String extractedCode;
//        if (baseStationName.contains("_X")) {
//            extractedCode = baseStationName.substring(nameLength - 8, nameLength - 2);
//        } else
//            extractedCode = baseStationName.substring(nameLength - 6);
//        return extractedCode;
//    }

    void calculateNumberOfSectors();
}
