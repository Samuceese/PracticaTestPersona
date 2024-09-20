package persona.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import error.CuentaBancariaError
import org.lighthousegames.logging.logging
import persona.models.CuentaBancaria
import java.math.BigInteger

private val logger= logging()

class CuentaBancariaValidator {

    fun validarCuenta(cuenta: CuentaBancaria): Result<CuentaBancaria,CuentaBancariaError>{
        logger.debug { "Validando cuenta ${cuenta.iban}" }
        if (!validarIban(cuenta.iban)) return Err( CuentaBancariaError.IbanIncorrectoError(cuenta.iban))
        if (!validarSaldo(cuenta.saldo))  return Err( CuentaBancariaError.SaldoError(cuenta.iban))

        return Ok( cuenta)
    }
    fun validarIban(iban:String): Boolean {
        logger.debug { "Validando iban $iban" }
        val ibanFormateado = iban.replace(" ", "").uppercase()

        val ibanReordenado = ibanFormateado.substring(4) + ibanFormateado.substring(0, 4)

        val ibanNumerico = StringBuilder()
        for (char in ibanReordenado) {
            ibanNumerico.append(
                if (char.isDigit()) char else (char.code - 'A'.code + 10).toString()
            )
        }
        val modulo = ibanNumerico.toString().toBigInteger().mod(BigInteger("97"))

        return modulo == BigInteger.ONE
    }
    fun validarSaldo(saldo:Double):Boolean{
        logger.debug { "Validando saldo" }
        return saldo>=0.0
    }
}