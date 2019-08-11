package doi.validator.parsers

import doi.validator.DoiValidInfo

interface DoiParser {

    int getPriority()

    DoiValidInfo parseDoi(String value)
}