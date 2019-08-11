package doi.validator

import doi.validator.parsers.DoiParser

import javax.inject.Singleton
import java.util.regex.Matcher
import java.util.regex.Pattern

@Singleton
class DoiValidatorServiceImpl implements DoiValidatorService {

    private static final String PATTERN_DNI = "(\\d{8})([A-Z])";
    private static final String PATTERN_NIE = "([XYZ])(\\d{7})([A-Z])";
    private static final String PATTERN_NIFPJ = "([ABCDEFGHJNPQRSUVW])(\\d{7})([\\dA-Z])";

    private static final String CONTROL_SOLO_NUMEROS = "ABEH"; // Sólo admiten números como caracter de control
    private static final String CONTROL_SOLO_LETRAS = "KPQS"; // Sólo admiten letras como caracter de control
    private static final String CONTROL_NUMERO_A_LETRA = "JABCDEFGHI"; // Conversión de dígito a letra de control.

    private static final String CONTROL_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String NIFPJ_LETTERS = "JABCDEFGHI";


    private List<DoiParser> parsers

    DoiValidatorServiceImpl(List<DoiParser> parsers) {
        this.parsers = parsers.sort { parser -> parser.getPriority() }
    }

    @Override
    DoiValidInfo validateDoi(String value) {
        return this.parsers.findResult(unknownDoi(value)) { it.parseDoi(value) }
    }

//    @Override
    DoiValidInfo validateDoi0(String value) {
        Matcher matcher = this.doiMatchesPattern(value, PATTERN_DNI)
        if (matcher != null) {
            return validateDNI(value, matcher)
        }

        matcher = this.doiMatchesPattern(value, PATTERN_NIE)
        if (matcher != null) {
            return validateNIE(value, matcher)
        }

        matcher = this.doiMatchesPattern(value, PATTERN_NIFPJ)
        if (matcher != null) {
            return validateNIFPJ(value, matcher);
        }
        return unknownDoi(value)
    }



    private DoiValidInfo unknownDoi(String value) {
        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setValue(value)
        doiValidInfo.setValid(false)
        return doiValidInfo
    }

    // ---------- DNI ------------ //

    private DoiValidInfo validateDNI(String nif, Matcher matcher) {
        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setType(DoiType.DNI)
        doiValidInfo.setValue(nif)

        doiValidInfo.num = matcher.group(1)
        doiValidInfo.sfx = matcher.group(2)
        doiValidInfo.modulus = Integer.parseInt(doiValidInfo.num) % 23
        doiValidInfo.validLetter = "" + CONTROL_LETTERS.charAt(doiValidInfo.modulus)
        doiValidInfo.setValid(doiValidInfo.validLetter.equals(doiValidInfo.sfx));

        return doiValidInfo;
    }

    // ---------- NIE ------------ //

    private DoiValidInfo validateNIE(String nie, Matcher matcher) {
        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setType(DoiType.NIE)
        doiValidInfo.setValue(nie)

        doiValidInfo.pfx = matcher.group(1)
        doiValidInfo.num = matcher.group(2)
        doiValidInfo.sfx = matcher.group(3)

        String pfxValue = ['X':'0', 'Y': '1', 'Z':'2'].find { it.key == doiValidInfo.pfx }.value
        doiValidInfo.modulus = Integer.parseInt(pfxValue + doiValidInfo.num) % 23

        doiValidInfo.validLetter = "" + CONTROL_LETTERS.charAt(doiValidInfo.modulus)
        doiValidInfo.setValid(doiValidInfo.validLetter.equals(doiValidInfo.sfx));

        return doiValidInfo;
    }


    // --------- NIF PJ --------- //
    // http://www.juntadeandalucia.es/servicios/madeja/contenido/recurso/679

    private DoiValidInfo validateNIFPJ(String nif, Matcher matcher) {
        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setType(DoiType.NIFPJ)
        doiValidInfo.setValue(nif)

        doiValidInfo.pfx = matcher.group(1)
        doiValidInfo.num = matcher.group(2)
        doiValidInfo.sfx = matcher.group(3)

        // Sumar los dígitos de las posiciones pares. Suma = A
        // Para cada uno de los dígitos de las posiciones impares, multiplicarlo por 2 y sumar los dígitos del resultado. (B)
        int sumaA = 0
        int sumaB = 0

        for (int i = 0; i < doiValidInfo.num.length(); i++) {
            int n = Integer.parseInt(doiValidInfo.num[i])
            if (i % 2 == 0) {
                // indice par, posición impar
                n *= 2
                sumaB += n < 10 ? n : n - 9
            } else {
                // indice impar, posición par
                sumaA += n
            }
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

        def controlDigit = "$digitoD"
        def controlLetter = "" + NIFPJ_LETTERS.charAt(digitoD)

        if (CONTROL_SOLO_NUMEROS.contains(doiValidInfo.pfx)) {
            doiValidInfo.validLetter = controlDigit
        } else if (CONTROL_SOLO_LETRAS.contains(doiValidInfo.pfx)) {
            doiValidInfo.validLetter = controlLetter
        } else {
            if (doiValidInfo.sfx == controlDigit) {
                doiValidInfo.validLetter = controlDigit
            } else if (doiValidInfo.sfx == controlLetter) {
                doiValidInfo.validLetter = controlLetter
            }
        }

        doiValidInfo.modulus = digitoD
        doiValidInfo.valid = (doiValidInfo.validLetter == doiValidInfo.sfx)
        return doiValidInfo
    }


    // -- UTILITY METHODS -- //

    private Matcher doiMatchesPattern(String doi, String pattern) {
        Pattern p = Pattern.compile(pattern)
        Matcher m = p.matcher(doi)
        if (!m.matches()) {
            return null
        }
        return m
    }
}
