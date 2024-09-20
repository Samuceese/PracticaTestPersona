package persona.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.lighthousegames.logging.logging
import persona.errors.PersonaError

private val logger = logging()

class DniValidator {

    fun validarDni(dni: String): Result<String, PersonaError> {
        logger.debug { "Validando DNI: $dni" }
        return if (validarNumeroDni(dni)) {
            Ok(dni)
        } else {
            Err(PersonaError.DniNoValidoError(dni))
        }
    }

    private fun validarNumeroDni(dni: String): Boolean {
        logger.debug { "Validando número de DNI: $dni" }
        val dniRegex = Regex("[0-9]{8}[A-Z]$")

        if (!dniRegex.matches(dni)) return false

        val numeros = dni.substring(0..7).toInt()
        val letra = dni[8].uppercase()

        val letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE"
        val letraCalculada = letrasDni[numeros % 23]

        return letra == letraCalculada.toString()
    }
}
