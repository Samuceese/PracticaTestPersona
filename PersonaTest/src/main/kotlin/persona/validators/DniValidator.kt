package persona.validators

class DniValidator {

    class InvalidDniFormatException(message: String) : Exception(message)
    class InvalidDniLetterException(message: String) : Exception(message)

    class DniValidator {

        fun validarDni(dni: String): Boolean {
            val regex = Regex("[0-9]{8}[A-Z]")

            if (!dni.matches(regex)) {
                throw InvalidDniFormatException("El formato del DNI es incorrecto. Debe tener 8 números y 1 letra mayúscula.")
            }

            val numerosDni = dni.slice(0..7).toInt()
            val letraDni = dni.slice(8..8).uppercase()

            val letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE"
            val resto = (numerosDni % 23)
            val letraCorrecta = letrasDni[resto].toString()

            if (letraDni != letraCorrecta) {
                throw InvalidDniLetterException("La letra del DNI es incorrecta. Se esperaba $letraCorrecta.")
            }

            return true
        }
    }

}