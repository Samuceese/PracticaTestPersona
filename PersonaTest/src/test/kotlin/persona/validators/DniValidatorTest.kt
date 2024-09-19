package validator

import cache.exceptions.DniExceptions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import persona.validators.DniValidator

class DniValidatorTest {

    private val validator = DniValidator()

    @Test
    @DisplayName("Validación del DNI correcto")
    fun casoOk() {
        // Arrange
        val dni = "12345678Z"

        // Act
        val result = validator.validarDni(dni)

        // Assert
        assertTrue(result)
    }

    @Test
    @DisplayName("DNI incorrecto, más de 9 caracteres")
    fun mayorDe9() {
        // Arrange
        val dni = "123456789Z"

        // Act & Assert
        val exception = assertThrows<DniExceptions> { validator.validarDni(dni) }
        assertEquals("El formato del DNI no es válido.", exception.message)
    }

    @Test
    @DisplayName("DNI incorrecto, menos de 9 caracteres")
    fun menorDe9() {
        // Arrange
        val dni = "1234567Z"

        // Act & Assert
        val exception = assertThrows<DniExceptions> { validator.validarDni(dni) }
        assertEquals("El formato del DNI no es válido.", exception.message)
    }

    @Test
    @DisplayName("DNI incorrecto, solo letras")
    fun todoLetras() {
        // Arrange
        val dni = "ZZZZZZZZZ"

        // Act & Assert
        val exception = assertThrows<DniExceptions> { validator.validarDni(dni) }
        assertEquals("El formato del DNI no es válido.", exception.message)
    }

    @Test
    @DisplayName("DNI incorrecto, solo números")
    fun todoNumeros() {
        // Arrange
        val dni = "123456789"

        // Act & Assert
        val exception = assertThrows<DniExceptions> { validator.validarDni(dni) }
        assertEquals("El formato del DNI no es válido.", exception.message)
    }

    @Test
    @DisplayName("DNI incorrecto, letra incorrecta")
    fun letraIncorrecta() {
        // Arrange
        val dni = "12345678A"

        // Act & Assert
        val exception = assertThrows<DniExceptions> { validator.validarDni(dni) }
        assertEquals("La letra del DNI no es válida.", exception.message)
    }
}
