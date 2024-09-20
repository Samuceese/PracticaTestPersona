package validators

import error.TarjetaError
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import persona.models.Tarjeta
import persona.validators.TarjetaValidator

class TarjetaValidatorTest {

    private val validador = TarjetaValidator()

    @Test
    fun validarNumeroOk() {
        val numero = "4111 1111 1111 1111"

        assertTrue(validador.validarNumero(numero))
    }

    @Test
    fun validarNumeroInvalido() {
        val numero = "4111 1111 1111"

        assertFalse(validador.validarNumero(numero))
    }

    @Test
    fun validarFechaValida() {
        val fecha = "08/27"

        assertTrue(validador.validarFecha(fecha))
    }

    @Test
    fun validarFechaAnoInvalido() {
        val fecha = "08/22"

        assertFalse(validador.validarFecha(fecha))
    }

    @Test
    fun validarFechaMesInvalido() {
        val fecha = "13/25"

        assertFalse(validador.validarFecha(fecha))
    }

    @Test
    fun validarTarjetaValida() {
        val tarjeta = Tarjeta("4111 1111 1111 1111", "08/27")

        val result = validador.validarTarjeta(tarjeta).value
        assertEquals(tarjeta, result)
    }

    @Test
    fun validarTarjetaInvalidoNumero() {
        val tarjeta = Tarjeta("4111 1111 111", "12/27")

        val result = validador.validarTarjeta(tarjeta).error
        assertTrue(result is TarjetaError.NumeroError)
    }

    @Test
    fun validarTarjetaInvalidoFecha() {
        val tarjeta = Tarjeta("4111 1111 1111 1111", "15/25")

        val result = validador.validarTarjeta(tarjeta).error
        assertTrue(result is TarjetaError.FechaError)
    }
}
