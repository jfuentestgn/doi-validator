package doi.validator

import doi.validator.parsers.DoiParser

import javax.inject.Singleton
import java.util.regex.Matcher
import java.util.regex.Pattern

@Singleton
class DoiValidatorServiceImpl implements DoiValidatorService {
    
    private List<DoiParser> parsers

    DoiValidatorServiceImpl(List<DoiParser> parsers) {
        this.parsers = parsers.sort { parser -> parser.getPriority() }
    }

    @Override
    DoiValidInfo validateDoi(String value) {
        return this.parsers.findResult(unknownDoi(value)) { it.parseDoi(value) }
    }

    private DoiValidInfo unknownDoi(String value) {
        DoiValidInfo doiValidInfo = new DoiValidInfo()
        doiValidInfo.setValue(value)
        doiValidInfo.setValid(false)
        return doiValidInfo
    }

}
