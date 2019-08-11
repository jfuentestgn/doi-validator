package doi.validator.parsers

import java.util.regex.Matcher
import java.util.regex.Pattern

class ParserUtils {

    private static final String DNI_CONTROL_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

    static Matcher doiMatchesPattern(String doi, Pattern pattern) {
        Matcher m = pattern.matcher(doi)
        if (!m.matches()) {
            return null
        }
        return m
    }

    static String calculateDNILetter(int modulus) {
        "" + DNI_CONTROL_LETTERS.charAt(modulus)
    }
}
