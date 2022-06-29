package liltrip.gencore.utils.numbers;

import java.util.TreeMap;

public class RomanNumberConverter {

    private final static TreeMap<Integer, String> mapToRoman = new TreeMap<>();

    static {
        mapToRoman.put(1000, "M");
        mapToRoman.put(900, "CM");
        mapToRoman.put(500, "D");
        mapToRoman.put(400, "CD");
        mapToRoman.put(100, "C");
        mapToRoman.put(90, "XC");
        mapToRoman.put(50, "L");
        mapToRoman.put(40, "XL");
        mapToRoman.put(10, "X");
        mapToRoman.put(9, "IX");
        mapToRoman.put(5, "V");
        mapToRoman.put(4, "IV");
        mapToRoman.put(1, "I");
    }

    public static String toRoman(int number) {
        int l = mapToRoman.floorKey(number);
        return (number == l) ? mapToRoman.get(number) : mapToRoman.get(l) + toRoman(number-l);
    }
}
