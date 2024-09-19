package persona.validators

import cache.exceptions.TarjetaException
import persona.models.Tarjeta

class TarjetaValidator {

    fun validarTarjeta(tarjeta: Tarjeta): Tarjeta {
        if (!validarNumeroTarjeta(tarjeta.numero)) throw TarjetaException.NumeroTarjetaNoValidoException()
        if (!validarFechaTarjeta(tarjeta.fecha)) throw TarjetaException.FechaTarjetaNoValidoException()

        return tarjeta
    }

    fun validarNumeroTarjeta(tarjeta: String): Boolean {
        val numeroLimpio = tarjeta.replace(Regex("[ -]"), "")

        if (!numeroLimpio.matches(Regex("\\d+"))) {
            return false
        }

        val longitud = numeroLimpio.length
        var suma = 0
        var esPar = false

        for (i in longitud - 1 downTo 0) {
            var digito = Character.getNumericValue(numeroLimpio[i])

            if (esPar) {
                digito *= 2
                if (digito > 9) {
                    digito -= 9
                }
            }

            suma += digito
            esPar = !esPar
        }
        return suma % 10 == 0
    }



    fun validarFechaTarjeta(fecha: String): Boolean {
        val regex = Regex("\\d{2}/\\d{2}")
        if (!regex.matches(fecha)) {
            return false
        }

        val (mesStr, anoStr) = fecha.split("/")

        val mes = mesStr.toInt()
        val ano = "20$anoStr".toInt()

        if (mes > 12 || ano < 2024) {
            return false
        }

        return true
    }

}
