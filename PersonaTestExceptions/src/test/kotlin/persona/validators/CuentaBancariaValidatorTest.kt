package persona.validators

import cache.exceptions.CuentaBancariaException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import persona.models.CuentaBancaria
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaBancariaValidatorTest {

    private lateinit var validator: CuentaBancariaValidator

    @BeforeEach
    fun setUp() {
        validator = CuentaBancariaValidator()
    }

    @Test
    fun validateCuentaOk() {
        // Arrange
        val cuenta = CuentaBancaria("ES91 2100 0418 4502 0005 1332", 10.0)

        // Act
        val result = validator.validate(cuenta)

        // Assert
        assertEquals(cuenta, result, "La cuenta bancaria debería ser válida y coincidir con la entrada")
    }

    @Test
    fun validateCuentaErrorIbanShort() {
        // Arrange
        val cuenta = CuentaBancaria("ES912100041845020005133", 10.0)

        // Act & Assert
        val exception = assertFailsWith<CuentaBancariaException.IbanNoValidoException> {
            validator.validate(cuenta)
        }
        assertEquals("La cuenta bancaria no es válida.", exception.message)
    }

    @Test
    fun validateCuentaErrorIbanInvalido() {
        // Arrange
        val cuenta = CuentaBancaria("ES6020385778987654321098", 10.0)

        // Act & Assert
        val exception = assertFailsWith<CuentaBancariaException.IbanNoValidoException> {
            validator.validate(cuenta)
        }
        assertEquals("La cuenta bancaria no es válida.", exception.message)
    }

    @Test
    fun validateCuentaErrorSueldoNegativo() {
        // Arrange
        val cuenta = CuentaBancaria("ES9121000418450200051332", -1.0)

        // Act & Assert
        val exception = assertFailsWith<CuentaBancariaException.SaldoNoValidoException> {
            validator.validate(cuenta)
        }
        assertEquals("El saldo de la cuenta no es válido.", exception.message)
    }

    @Test
    fun validateCuentaOkSueldoPositivo() {
        // Arrange
        val cuenta = CuentaBancaria("ES9121000418450200051332", 1.0)

        // Act
        val result = validator.validate(cuenta)

        // Assert
        assertEquals(cuenta, result, "La cuenta bancaria debería ser válida y coincidir con la entrada")
    }
}
