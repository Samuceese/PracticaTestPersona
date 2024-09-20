package error

sealed class TarjetaError (message:String): AllError(message)  {
    class NumeroError(message:String):TarjetaError("Numero de tarjeta no valido")
    class FechaError(message:String):TarjetaError("Fecha de caducidad no valida")
}