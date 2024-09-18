package persona.validators

import cache.exceptions.DniExceptions

class DniValidator {

    class InvalidDniFormatException(message: String) : Exception(message)
    class InvalidDniLetterException(message: String) : Exception(message)

    class DniValidator {

        fun validarDni(dni: String): Boolean {
            val regex = Regex("[0-9]{8}[A-Z]")

            if (!dni.matches(regex)) {
                throw DniExceptions.DniInvalidoException("El formato del DNI no es válido.")
            }

            val numerosDni = dni.slice(0..7).toInt()
            val letraDni = dni.slice(8..8).uppercase()

            val letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE"
            val resto = (numerosDni % 23)
            val letraCorrecta = letrasDni[resto].toString()

            if (letraDni != letraCorrecta) {
                throw DniExceptions.DniLetraInvalidaException("La letra del DNI no es válida.")
            }

            return true
        }
    }

}