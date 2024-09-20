package persona.validators

import error.CuentaBancariaError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import persona.models.CuentaBancaria

class CuentaBancariaValidatorTest {

    private val validador= CuentaBancariaValidator()


    @Test
    fun validarCuentaOk() {
        val cuenta= CuentaBancaria("ES91 2100 0418 4502 0005 1332",10.0)

        assertEquals(validador.validarCuenta(cuenta).value,cuenta)
    }

    @Test
    fun validarIbanOk() {
        val iban="ES91 2100 0418 4502 0005 1332"

        assertTrue(validador.validarIban(iban))

    }

    @Test
    fun validarIbanNoValido() {
        val iban="ES00 1234 5678 9123 4567 8901"

        assertFalse(validador.validarIban(iban))

    }

    @Test
    fun validarSaldo() {
        val saldo=500.0

        assertTrue(validador.validarSaldo(saldo))
    }


    @Test
    fun validarSaldoMenor0() {
        val saldo=-500.0

        assertFalse(validador.validarSaldo(saldo))
    }

    @Test
    fun validarCuentaIbanNoValido() {
        val cuenta= CuentaBancaria("ES00 1234 5678 9123 4567 8901",100.0)

        val result=validador.validarCuenta(cuenta).error

        assertTrue(result is CuentaBancariaError.IbanIncorrectoError)

    }

    @Test
    fun validarCuentaSaldoNoValido() {
        val cuenta= CuentaBancaria("ES91 2100 0418 4502 0005 1332",-100.0)

        val result=validador.validarCuenta(cuenta).error

        assertTrue(result is CuentaBancariaError.SaldoError)
    }

}