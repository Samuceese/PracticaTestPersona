package persona.validators

import cache.exceptions.DniExceptions

class DniValidator {

    fun validarDni(dni: String): Boolean {
        if (dni.length != 9) {
            throw DniExceptions.DniInvalidoException()
        }
        val numeros = dni.substring(0, 8)
        val letra = dni[8]

        if (!numeros.all { it.isDigit() } || !letra.isLetter()) {
            throw DniExceptions.DniInvalidoException()
        }

        val letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE"
        val indice = numeros.toInt() % 23
        if (letra != letrasValidas[indice]) {
            throw DniExceptions.DniLetraInvalidaException()
        }

        return true
    }
}
