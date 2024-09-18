package validator

import cache.exceptions.DniExceptions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import persona.validators.DniValidator

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DniValidatorTest {

    private val validator = DniValidator.DniValidator()

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

        //act
        val result = assertThrows<DniExceptions> { validator.validarDni(dni) }

        //assert
        assertEquals("El formato del DNI no es válido.", result.message)


    }


    @Test
    @DisplayName("DNI incorrecto, menos de 9 caracteres")
    fun menorDe9() {
        // Arrange
        val dni = "1234567Z"

        // Assert
        val result = assertThrows<DniExceptions> { validator.validarDni(dni) }
            // Act
        assertEquals("El formato del DNI no es válido.", result.message)
    }

    @Test
    @DisplayName("DNI incorrecto, solo letras")
    fun todoLetras() {
        // Arrange
        val dni = "ZZZZZZZZZ"

        // Assert
        val result = assertThrows<DniExceptions> { validator.validarDni(dni) }
        // Act
        assertEquals("El formato del DNI no es válido.", result.message)
    }

    @Test
    @DisplayName("DNI incorrecto, solo números")
    fun todoNumeros() {
        // Arrange
        val dni = "123456789"

        // Assert
        val result = assertThrows<DniExceptions> { validator.validarDni(dni) }
        // Act
        assertEquals("El formato del DNI no es válido.", result.message)
    }

    @Test
    @DisplayName("DNI incorrecto, letra incorrecta")
    fun letraIncorrecta() {
        // Arrange
        val dni = "12345678A"

        // Assert
        val result = assertThrows<DniExceptions> { validator.validarDni(dni) }
        // Act
        assertEquals("La letra del DNI no es válida.", result.message)
    }
}
