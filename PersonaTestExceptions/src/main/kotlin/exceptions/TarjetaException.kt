package cache.exceptions

sealed class TarjetaException(message : String) : RuntimeException(message) {
    class NumeroTarjetaNoValidoException() : TarjetaException("El numero de la tarjeta no es correcto")
    class FechaTarjetaNoValidoException() : TarjetaException("La fecha de la tarjeta no es correcta")
    class TarjetaNoValidaException() : TarjetaException("La tarjeta no es válida")
}