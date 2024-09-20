package persona.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import error.TarjetaError
import org.lighthousegames.logging.logging
import persona.models.Tarjeta
import java.time.YearMonth
import java.time.format.DateTimeParseException

private val logger = logging()

class TarjetaValidator {

    fun validarTarjeta(tarjeta: Tarjeta): Result<Tarjeta, TarjetaError> {
        logger.debug { "Validando tarjeta $tarjeta" }

        return when {
            !validarNumero(tarjeta.numero) -> Err(TarjetaError.NumeroError(tarjeta.numero))
            !validarCaducidad(tarjeta.fecha) -> Err(TarjetaError.FechaError(tarjeta.fecha))
            else -> Ok(tarjeta)
        }
    }

    private fun validarNumero(numero: String): Boolean {
        logger.debug { "Validando número de tarjeta $numero" }
        val tarjetaFormateada = numero.replace(" ", "").replace("-", "")

        if (!tarjetaFormateada.matches(Regex("\\d+"))) return false

        return calcularSumaLuhn(tarjetaFormateada) % 10 == 0
    }

    private fun calcularSumaLuhn(numero: String): Int {
        var sumaTotal = 0
        var esSegundoDigito = false

        for (i in numero.length - 1 downTo 0) {
            var digito = numero[i].toString().toInt()

            if (esSegundoDigito) {
                digito = if (digito * 2 > 9) digito * 2 - 9 else digito * 2
            }
            sumaTotal += digito
            esSegundoDigito = !esSegundoDigito
        }

        return sumaTotal
    }

    private fun validarCaducidad(fecha: String): Boolean {
        logger.debug { "Validando fecha de caducidad $fecha" }

        if (!fecha.matches(Regex("^\\d{2}/\\d{2}$"))) return false

        val (mes, ano) = fecha.split("/").mapNotNull { it.toIntOrNull() }
        if (mes !in 1..12) return false

        val fechaCaducidad = try {
            YearMonth.of(2000 + ano, mes)
        } catch (e: DateTimeParseException) {
            return false
        }

        return !fechaCaducidad.isBefore(YearMonth.now())
    }
}
