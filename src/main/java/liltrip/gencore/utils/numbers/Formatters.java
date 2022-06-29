package liltrip.gencore.utils.numbers;

import java.text.DecimalFormat;

public class Formatters {

    private static final DecimalFormat formatPrecise = new DecimalFormat("###,###.#####");
    private static final DecimalFormat format = new DecimalFormat("###,###.##");

    public static String convertToCurrency(double amt) {
        return "$" + format.format(amt);
    }

    public static String convertToCurrencyPrecise(double amt) {
        return "$" + formatPrecise.format(amt);
    }

    public static String convertToCommaNumber(double amt) {
        return format.format(amt);
    }

    public static String convertToCommaNumberPrecise(double amt) {
        return formatPrecise.format(amt);
    }

}
