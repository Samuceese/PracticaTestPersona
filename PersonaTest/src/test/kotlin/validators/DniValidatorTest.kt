package validator

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import validators.DniValidator
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestsAlumnoValidator {
    private val validator = DniValidator()

    @Test
    @DisplayName("Validación del DNI correcto")
    fun casoOk() {
        val dni = "12345678Z"
        val result = validator.validarDni(dni)
        assertTrue(result)
    }

    @Test
    @DisplayName("Dni incorrecto, mayor de 9.")
    fun mayorDe9 (){
        val dni = "123456789Z"
        val result = validator.validarDni(dni)
        assertFalse(result)
    }

    @Test
    @DisplayName("Dni incorrecto, menor de 9.")
    fun menorDe9 (){
        val dni = "1234567Z"
        val result = validator.validarDni(dni)
        assertFalse(result)
    }

    @Test
    @DisplayName("Dni incorrecto, contiene todo letras.")
    fun todoLetras (){
        val dni = "ZZZZZZZZZ"
        val result = validator.validarDni(dni)
        assertFalse(result)
    }

    @Test
    @DisplayName("Dni incorrecto, contiene todo numeros.")
    fun todoNumeros(){
        val dni = "123456789"
        val result = validator.validarDni(dni)
        assertFalse(result)
    }
}