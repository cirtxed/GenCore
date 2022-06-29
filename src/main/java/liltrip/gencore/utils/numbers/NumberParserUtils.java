package liltrip.gencore.utils.numbers;

import com.google.common.primitives.Doubles;

public class NumberParserUtils {

    public static Number parse(String parsing) {
        String number = parsing.replace(",","").replaceFirst("k", "000")
                .replaceFirst("m", "000000").replaceFirst("b", "000000000")
                .replaceFirst("t", "000000000000").replaceFirst("q", "000000000000000");
        return Doubles.tryParse(number);
    }
}
