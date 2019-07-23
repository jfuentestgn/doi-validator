package doi.validator

import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@MicronautTest
class DoiValidatorServiceSpec extends Specification {

    @Inject
    DoiValidatorService doiValidatorService

    void "simple test"() {
        when:
        def a = 1

        then:
        a == 1
    }

    void "test service is injected"() {
        expect:
        this.doiValidatorService != null
        this.doiValidatorService instanceof DoiValidatorService
        this.doiValidatorService instanceof DoiValidatorServiceImpl
    }

    @Unroll
    void "test DNI validation: #dni"() {
        when:
        DoiValidInfo result = this.doiValidatorService.validateDoi(dni)

        then:
        result
        result.valid == valid
        result.type.type == 'DNI'
        result.sfx == letter
        result.validLetter == letter
        result.value == dni

        where:
        dni         | valid | letter
        '39732731R' | true  | 'R'
        '68606429M' | true  | 'M'
        '30599083K' | true  | 'K'
        '65758048J' | true  | 'J'
        '81341904N' | true  | 'N'
        '75208748J' | true  | 'J'
        '20375110P' | true  | 'P'
        '46062916B' | true  | 'B'
        '22848063R' | true  | 'R'
        '18034379X' | true  | 'X'
        '44506578Z' | true  | 'Z'
        '24941711M' | true  | 'M'
        '05087831R' | true  | 'R'
        '30372132B' | true  | 'B'
        '02925111V' | true  | 'V'
        '78819304Y' | true  | 'Y'
        '12051615Y' | true  | 'Y'
        '08205875G' | true  | 'G'
        '14081039W' | true  | 'W'
        '93447397F' | true  | 'F'
        '95145216N' | true  | 'N'
        '76526187N' | true  | 'N'
        '74636442Q' | true  | 'Q'
        '86724086X' | true  | 'X'
        '91368312F' | true  | 'F'
        '37932364M' | true  | 'M'
        '89240150N' | true  | 'N'
        '39683221X' | true  | 'X'
        '77841452E' | true  | 'E'
        '89676300J' | true  | 'J'
        '26409320F' | true  | 'F'
        '27220915R' | true  | 'R'
        '63531668X' | true  | 'X'
        '15528997H' | true  | 'H'
        '41414343Z' | true  | 'Z'
        '72414004E' | true  | 'E'
        '72226058D' | true  | 'D'
        '26564809Q' | true  | 'Q'
        '41907162N' | true  | 'N'
        '33569794Y' | true  | 'Y'
        '62591631Y' | true  | 'Y'
    }


    @Unroll
    void "test NIE validation: #nie"() {
        when:
        DoiValidInfo result = this.doiValidatorService.validateDoi(nie)

        then:
        result
        result.valid == valid
        result.type.type == 'NIE'
        result.sfx == letter
        result.validLetter == letter
        result.value == nie

        where:
        nie         | valid | letter
        'Y2157394P' | true  | 'P'
        'X5977591Y' | true  | 'Y'
        'Z4584473A' | true  | 'A'
        'Y2595716L' | true  | 'L'
        'X5658193D' | true  | 'D'
        'Z1216398W' | true  | 'W'
        'X9280611L' | true  | 'L'
        'Z5704245C' | true  | 'C'
        'Z2664843E' | true  | 'E'
        'Z6261717H' | true  | 'H'
        'X5825494P' | true  | 'P'
        'Z7352258Z' | true  | 'Z'
        'Z1577141J' | true  | 'J'
        'X0609287V' | true  | 'V'
        'Y9317222M' | true  | 'M'
        'X8490692N' | true  | 'N'
        'X5090456G' | true  | 'G'
        'Y4441903L' | true  | 'L'
        'X6020295E' | true  | 'E'
        'Y6413602C' | true  | 'C'
        'X8440022B' | true  | 'B'
    }

    @Unroll
    void "Test NIF-PJ Validation: #nif"() {
        when:
        DoiValidInfo result = this.doiValidatorService.validateDoi(nif)

        then:
        result
        result.valid == valid
        result.pfx == nif[0]
        result.type.type == 'NIF'

        where:
        nif         | valid
        'A05195185' |   true
        'V5385971F' |   true
        'J2161702B' |   true
        'S9345987C' |   true
        'B25692567' |   true
        'D35691906' |   true
        'V1908738F' |   true
        'D30547160' |   true
        'E00324574' |   true
        'Q6224307F' |   true
        'C35388347' |   true
        'A67006932' |   true
        'E62111539' |   true
        'U9642609C' |   true
        'E03241114' |   true
        'V6251786G' |   true
        'U2554649J' |   true
        'P6471328B' |   true
        'Q4071757A' |   true
        'S2327989F' |   true
    }
}
