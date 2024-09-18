package validators

class DniValidator {

    fun validarDni(dni: String): Boolean {
        val regex = Regex("[0-9]{8}[A-Z]")

        if (!dni.matches(regex)) {
            return false
        }

        val numerosDni = dni.slice(0..7).toInt()
        val letraDni = dni.slice(8..8).uppercase()

        val letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE"

        val resto = (numerosDni % 23)
        val letraCorrecta = letrasDni[resto].toString()

        return letraDni == letraCorrecta
    }
}