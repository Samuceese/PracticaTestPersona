package persona.validators

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DniValidatorTest {

    private val validator = DniValidator()

    @Test
    @DisplayName("Validación del DNI correcto")
    fun casoOk() {
        val dni = "12345678Z"

        val result = validator.validarNumeroDni(dni)
        assertTrue(result)
    }

    @Test
    @DisplayName("DNI incorrecto, más de 9 caracteres")
    fun mayorDe9() {
        val dni = "123456789Z"

        assertFalse(validator.validarNumeroDni(dni))
    }

    @Test
    @DisplayName("DNI incorrecto, menos de 9 caracteres")
    fun menorDe9() {
        val dni = "1234567Z"

        assertFalse(validator.validarNumeroDni(dni))
    }

    @Test
    @DisplayName("DNI incorrecto, solo letras")
    fun todoLetras() {
        val dni = "ZZZZZZZZZ"

        assertFalse(validator.validarNumeroDni(dni))

    }

    @Test
    @DisplayName("DNI incorrecto, solo números")
    fun todoNumeros() {
        val dni = "123456789"

        assertFalse(validator.validarNumeroDni(dni))
    }
}