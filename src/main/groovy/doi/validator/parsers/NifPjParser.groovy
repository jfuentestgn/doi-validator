package doi.validator.parsers

import doi.validator.DoiType
import doi.validator.DoiValidInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Singleton
import java.util.regex.Matcher
import java.util.regex.Pattern

@Singleton
class NifPjParser implements DoiParser {

    static final Logger LOG = LoggerFactory.getLogger(NifPjParser.class)

    private static final String PATTERN_NIFPJ = "([ABCDEFGHJNPQRSUVW])(\\d{7})([\\dA-Z])"
    private static final String CONTROL_SOLO_NUMEROS = "ABEH"; // Sólo admiten números como caracter de control
    private static final String CONTROL_SOLO_LETRAS = "KPQS"; // Sólo admiten letras como caracter de control

    private static final String NIFPJ_LETTERS = "JABCDEFGHI"

    private Pattern pattern

    NifPjParser() {
        this.pattern = Pattern.compile(PATTERN_NIFPJ)
    }

    @Override
    int getPriority() {
        return 20
    }

    @Override
    DoiValidInfo parseDoi(String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Trying NifPjParser...")
        }
        Matcher matcher = ParserUtils.doiMatchesPattern(value, this.pattern)
        if (matcher == null) {
            return null
        }

        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setType(DoiType.NIFPJ)
        doiValidInfo.setValue(value)

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
}
