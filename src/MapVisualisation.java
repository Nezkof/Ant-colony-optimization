import java.util.HashMap;
import java.util.Map;

public class MapVisualisation {
    private int[] bestTourOrder;
    Map<Integer, String> cityMap;

    MapVisualisation(int[] bestTourOrder){
        this.bestTourOrder = bestTourOrder;
        cityMap = new HashMap<>();
        cityMap.put(0, "Вінниця");
        cityMap.put(1, "Дніпро");
        cityMap.put(2, "Донецьк");
        cityMap.put(3, "Житомир");
        cityMap.put(4, "Запоріжжя");
        cityMap.put(5, "Івано-Франківськ");
        cityMap.put(6, "Київ");
        cityMap.put(7, "Кропивницький");
        cityMap.put(8, "Луганськ");
        cityMap.put(9, "Луцьк");
        cityMap.put(10, "Львів");
        cityMap.put(11, "Миколаїв");
        cityMap.put(12, "Одеса");
        cityMap.put(13, "Полтава");
        cityMap.put(14, "Рівне");
        cityMap.put(15, "Симферопіль");
        cityMap.put(16, "Суми");
        cityMap.put(17, "Тернопіль");
        cityMap.put(18, "Ужгород");
        cityMap.put(19, "Харків");
        cityMap.put(20, "Херсон");
        cityMap.put(21, "Хмельницький");
        cityMap.put(22, "Черкаси");
        cityMap.put(23, "Чернівці");
        cityMap.put(24, "Чернігів");
    }

    public void showMap(){
        for (int j : bestTourOrder) {
            String color = "\033[0;32m";
            String reset = "\033[0m";
            String city = cityMap.get(j);
            String formattedCity = color + city + reset;
            cityMap.put(j, formattedCity);

            System.out.println("\n\n===========================================================================================================================================================================================================================\n\n");
            System.out.printf("%110s\n", cityMap.get(24));
            System.out.printf("%135s\n", cityMap.get(16));
            System.out.printf("%40s\n", cityMap.get(9));
            System.out.printf("%50s\n", cityMap.get(14));
            System.out.printf("%75s %40s\n", cityMap.get(3),cityMap.get(6));
            System.out.printf("%160s\n", cityMap.get(19));
            System.out.printf("%28s\n", cityMap.get(10));
            System.out.printf("%142s\n", cityMap.get(13));
            System.out.printf("%45s\n", cityMap.get(17));
            System.out.printf("%65s\n", cityMap.get(21));
            System.out.printf("%110s\n", cityMap.get(22));
            System.out.printf("%75s\n", cityMap.get(0));
            System.out.printf("%45s\n", cityMap.get(5));
            System.out.printf("%25s %160s\n", cityMap.get(18), cityMap.get(8));
            System.out.printf("%115s %30s\n", cityMap.get(7), cityMap.get(1));
            System.out.printf("%50s \n", cityMap.get(23));
            System.out.printf("%170s\n", cityMap.get(2));
            System.out.printf("%150s\n", cityMap.get(4));
            System.out.printf("\n\n\n%110s\n", cityMap.get(11));
            System.out.printf("\n%125s\n", cityMap.get(20));
            System.out.printf("\n%95s\n", cityMap.get(12));
            System.out.printf("\n\n\n%135s\n", cityMap.get(15));
        }
    }


}
