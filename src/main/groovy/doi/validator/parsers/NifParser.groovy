package doi.validator.parsers

import doi.validator.DoiType
import doi.validator.DoiValidInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Singleton
import java.util.regex.Matcher
import java.util.regex.Pattern

@Singleton
class NifParser implements DoiParser {

    static final Logger LOG = LoggerFactory.getLogger(NifParser.class)

    private static final String PATTERN_DNI = "(\\d{8})([A-Z])";

    private Pattern pattern

    NifParser() {
        this.pattern = Pattern.compile(PATTERN_DNI)
    }

    @Override
    int getPriority() {
        return 10
    }

    @Override
    DoiValidInfo parseDoi(String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Trying NIFParser...")
        }
        Matcher matcher = ParserUtils.doiMatchesPattern(value, this.pattern)
        if (matcher == null) {
            return null
        }

        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setType(DoiType.DNI)
        doiValidInfo.setValue(value)

        doiValidInfo.num = matcher.group(1)
        doiValidInfo.sfx = matcher.group(2)
        doiValidInfo.modulus = Integer.parseInt(doiValidInfo.num) % 23
        doiValidInfo.validLetter = ParserUtils.calculateDNILetter(doiValidInfo.modulus)
        doiValidInfo.setValid(doiValidInfo.validLetter.equals(doiValidInfo.sfx))

        return doiValidInfo
    }
}
