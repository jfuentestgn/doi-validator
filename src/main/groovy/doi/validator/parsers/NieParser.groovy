package doi.validator.parsers

import doi.validator.DoiType
import doi.validator.DoiValidInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Singleton
import java.util.regex.Matcher
import java.util.regex.Pattern

@Singleton
class NieParser implements DoiParser {

    static final Logger LOG = LoggerFactory.getLogger(NieParser.class)

    private static final String PATTERN_NIE = "([XYZ])(\\d{7})([A-Z])"

    private Pattern pattern

    NieParser() {
        this.pattern = Pattern.compile(PATTERN_NIE)
    }

    @Override
    int getPriority() {
        return 30
    }

    @Override
    DoiValidInfo parseDoi(String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Trying NieParser...")
        }
        Matcher matcher = ParserUtils.doiMatchesPattern(value, this.pattern)
        if (matcher == null) {
            return null
        }

        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setType(DoiType.NIE)
        doiValidInfo.setValue(value)

        doiValidInfo.pfx = matcher.group(1)
        doiValidInfo.num = matcher.group(2)
        doiValidInfo.sfx = matcher.group(3)

        String pfxValue = ['X':'0', 'Y': '1', 'Z':'2'].find { it.key == doiValidInfo.pfx }.value
        doiValidInfo.modulus = Integer.parseInt(pfxValue + doiValidInfo.num) % 23

        doiValidInfo.validLetter = ParserUtils.calculateDNILetter(doiValidInfo.modulus)
        doiValidInfo.setValid(doiValidInfo.validLetter.equals(doiValidInfo.sfx))

        return doiValidInfo;
    }
}
