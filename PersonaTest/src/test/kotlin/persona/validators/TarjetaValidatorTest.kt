package validator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import persona.validators.TarjetaValidator

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TarjetaValidatorTest {

    private val validator = TarjetaValidator()

    @Test
    @DisplayName("Número de tarjeta válido")
    fun numeroTarjetaValido() {
        // Arrange
        val tarjeta = "4539 1488 0343 6467"

        // Act
        val result = validator.validarNumeroTarjeta(tarjeta)

        // Assert
        assertTrue(result)
    }

    @Test
    @DisplayName("Número de tarjeta inválido (no pasa algoritmo de Luhn)")
    fun numeroTarjetaInvalidoLuhn() {
        // Arrange
        val tarjeta = "1234 5678 9012 3456"

        // Act
        val result = validator.validarNumeroTarjeta(tarjeta)

        // Assert
        assertFalse(result)
    }

    @Test
    @DisplayName("Número de tarjeta con caracteres inválidos")
    fun numeroTarjetaConCaracteresInvalidos() {
        // Arrange
        val tarjeta = "4539-1488-0343-ABC"

        // Act
        val result = validator.validarNumeroTarjeta(tarjeta)

        // Assert
        assertFalse(result)
    }

    @Test
    @DisplayName("Número de tarjeta con guiones y espacios, válido")
    fun numeroTarjetaConGuionesYEspacios() {
        // Arrange
        val tarjeta = "4539-1488-0343-6467"

        // Act
        val result = validator.validarNumeroTarjeta(tarjeta)

        // Assert
        assertTrue(result)
    }

    @Test
    @DisplayName("Número de tarjeta muy corto")
    fun numeroTarjetaMuyCorto() {
        // Arrange
        val tarjeta = "1234"

        // Act
        val result = validator.validarNumeroTarjeta(tarjeta)

        // Assert
        assertFalse(result)
    }

    // Tests para validarFechaTarjeta
    @Test
    @DisplayName("Fecha de tarjeta válida")
    fun fechaTarjetaValida() {
        // Arrange
        val fecha = "12/25"

        // Act
        val result = validator.validarFechaTarjeta(fecha)

        // Assert
        assertTrue(result)
    }

    @Test
    @DisplayName("Fecha de tarjeta con mes mayor a 12")
    fun fechaTarjetaMesInvalido() {
        // Arrange
        val fecha = "13/25"

        // Act
        val result = validator.validarFechaTarjeta(fecha)

        // Assert
        assertFalse(result)
    }

    @Test
    @DisplayName("Fecha de tarjeta con año menor a 2024")
    fun fechaTarjetaAnoInvalido() {
        // Arrange
        val fecha = "11/23"

        // Act
        val result = validator.validarFechaTarjeta(fecha)

        // Assert
        assertFalse(result)
    }

    @Test
    @DisplayName("Fecha de tarjeta con formato incorrecto")
    fun fechaTarjetaFormatoIncorrecto() {
        // Arrange
        val fecha = "12-25"

        // Act
        val result = validator.validarFechaTarjeta(fecha)

        // Assert
        assertFalse(result)
    }

    @Test
    @DisplayName("Fecha de tarjeta válida con año igual a 2024")
    fun fechaTarjetaAnoIgualA2024() {
        // Arrange
        val fecha = "11/24"

        // Act
        val result = validator.validarFechaTarjeta(fecha)

        // Assert
        assertTrue(result)
    }
}
