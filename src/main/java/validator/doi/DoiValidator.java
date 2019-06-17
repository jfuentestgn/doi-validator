package validator.doi;

import io.micronaut.validation.Validated;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Validated
public class DoiValidator {

    private static final String PATTERN_DNI = "(\\d{8})([A-Z])";
    private static final String PATTERN_NIE = "([XYZ])(\\d{7})([A-Z])";
    private static final String PATTERN_NIFPJ = "([ABCDEFGHJNPQRSUVW])(\\d{7})([\\dA-Z])";

    private static final String CONTROL_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String NIFPJ_LETTERS = "JABCDEFGHI";


    public DoiInfo parseDoi(@NotBlank String doi) {
        DoiInfo doiInfo = this.parseDNI(doi);
        if (doiInfo == null) {
            doiInfo = this.parseNIE(doi);
        }
        if (doiInfo == null) {
            doiInfo = this.parseNIFPJ(doi);
        }

        if (doiInfo == null) {
            doiInfo = new DoiInfo();
            doiInfo.setValue(doi);
            doiInfo.setValid(false);
        }
        return doiInfo;
    }

    // -- DNI -- //

    private DoiInfo parseDNI(String doi) {
        Matcher m = this.doiMatchesPattern(doi, PATTERN_DNI);
        if (m == null) {
            return null;
        }

        DoiInfo doiInfo = new DoiInfo();
        doiInfo.setType(DoiType.DNI);
        doiInfo.setValue(doi);
        this.setValidationFields(doiInfo, m.group(1), m.group(2));
        return doiInfo;
    }

    // -- NIE -- //

    private DoiInfo parseNIE(String doi) {
        Matcher m = this.doiMatchesPattern(doi, PATTERN_NIE);
        if (m == null) {
            return null;
        }

        DoiInfo doiInfo = new DoiInfo();
        doiInfo.setType(DoiType.NIE);
        doiInfo.setValue(doi);
        String pfx = m.group(1);
        doiInfo.setPfx(pfx);

        String pfxValue = "";
        if ("X".equals(pfx)) {
            pfxValue = "0";
        } else if ("Y".equals(pfx)) {
            pfxValue = "1";
        } else {
            pfxValue = "2";
        }
        String numValue = m.group(2);
        doiInfo.setNum(numValue);
        String sfx = m.group(3);

        this.setValidationFields(doiInfo, pfxValue + numValue, sfx);
        return doiInfo;
    }

    private DoiInfo parseNIFPJ(String doi){
        Matcher m = this.doiMatchesPattern(doi, PATTERN_NIFPJ);
        if (m == null) {
            return null;
        }

        DoiInfo doiInfo = new DoiInfo();
        doiInfo.setType(DoiType.NIFPJ);
        doiInfo.setValue(doi);
        String pfx = m.group(1);
        doiInfo.setPfx(pfx);
        String numValue = m.group(2);
        String sfx = m.group(3);
        doiInfo.setNum(numValue);
        doiInfo.setSfx(sfx);

        this.setValidationFieldsPJ(doiInfo, numValue, sfx);
        return doiInfo;
    }


    // -- UTILITY METHODS -- //

    private Matcher doiMatchesPattern(String doi, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(doi);
        if (!m.matches()) {
            return null;
        }
        return m;
    }


    private void setValidationFields(DoiInfo doiInfo, String numeroBase, String sfx) {
        if (doiInfo.getNum() == null) {
            doiInfo.setNum(numeroBase);
        }

        int modulus = Integer.parseInt(numeroBase) % 23;
        doiInfo.setModulus(modulus);
        doiInfo.setSfx(sfx);
        String validLetter = "" + CONTROL_LETTERS.charAt(modulus);
        doiInfo.setValidLetter(validLetter);
        doiInfo.setValid(validLetter.equals(sfx));
    }

    private void setValidationFieldsPJ(DoiInfo doiInfo, String numeroBase, String sfx) {
        if (doiInfo.getNum() == null) {
            doiInfo.setNum(numeroBase);
        }

        // Sumar los dígitos de las posiciones pares. Suma = A
        char [] chars = numeroBase.toCharArray();
        int sumaA = Integer.parseInt(String.valueOf(chars[1]))
                + Integer.parseInt(String.valueOf(chars[3]))
                + Integer.parseInt(String.valueOf(chars[5]));
        // Para cada uno de los dígitos de las posiciones impares, multiplicarlo por 2 y sumar los dígitos del resultado. (B)
        int sumaB = 0;
        for (int i = 0; i < 4; i++) {
            int b = Integer.parseInt(String.valueOf(chars[ i * 2])) * 2;
            if (b > 9) {
                b = 1 + b - 10;
            }
            sumaB += b;
        }
        // Sumar A + B = C
        int sumaC = sumaA + sumaB;
        // Tomar sólo el dígito de las unidades de C. Lo llamaremos dígito E.
        int digitoE = sumaC % 10;
        // Si el dígito E es distinto de 0 lo restaremos a 10. D = 10-E. Esta resta nos da D. Si no, si el dígito E es 0 entonces D = 0 y no hacemos resta.
        int digitoD = 0;
        if (digitoE > 0) {
            digitoD = 10 - digitoE;
        }

        String validLetter = String.valueOf(digitoD);
        if (!"ABEH".contains(doiInfo.getSfx())) {
            validLetter = "" + NIFPJ_LETTERS.charAt(Integer.parseInt(validLetter));
        }
        doiInfo.setValidLetter(validLetter);
        doiInfo.setValid(validLetter.equals(sfx));
    }

}
