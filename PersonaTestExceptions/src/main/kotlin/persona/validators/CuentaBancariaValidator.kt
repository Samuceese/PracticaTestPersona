package persona.validators

import cache.exceptions.CuentaBancariaException
import org.lighthousegames.logging.logging
import persona.models.CuentaBancaria
import java.math.BigInteger

class CuentaBancariaValidator {

    private val logger = logging()

    fun validate(cuentaBancaria: CuentaBancaria): CuentaBancaria {
        logger.debug { "Iniciando la validación de la cuenta bancaria" }

        if (!isIbanValid(cuentaBancaria.iban)) {
            logger.error { "Error en la validación del IBAN" }
            throw CuentaBancariaException.IbanNoValidoException()
        }

        if (!isSaldoValid(cuentaBancaria.saldo)) {
            logger.error { "Error en la validación del saldo" }
            throw CuentaBancariaException.SaldoNoValidoException()
        }

        logger.info { "Cuenta bancaria validada correctamente" }
        return cuentaBancaria
    }

    private fun isIbanValid(iban: String): Boolean {
        logger.debug { "Validando el IBAN" }
        val ibanLimpio = iban.replace(" ", "")
        if (ibanLimpio.length != 24) {
            logger.error { "El IBAN debe tener 24 caracteres" }
            return false
        }

        val ibanReordenado = ibanLimpio.substring(4) + ibanLimpio.substring(0, 4)
        val ibanDigits = StringBuilder()

        for (char in ibanReordenado) {
            ibanDigits.append(
                if (char.isDigit()) {
                    char
                } else {
                    char.code - 55
                }
            )
        }

        val ibanNumber = BigInteger(ibanDigits.toString())
        return ibanNumber % BigInteger("97") == BigInteger("1")
    }

    private fun isSaldoValid(saldo: Double): Boolean {
        logger.debug { "Validando el saldo" }
        val isValid = saldo > 0

        if (isValid) {
            logger.info { "Saldo válido" }
        } else {
            logger.error { "El saldo debe ser mayor a 0" }
        }

        return isValid
    }
}
