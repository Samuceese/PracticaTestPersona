package cache.exceptions

sealed class CuentaBancariaException(message : String) : Exception(message) {
    class IbanNoValidoException(message: String) : CuentaBancariaException("La cuenta bancaria no es válida.")
    class SaldoNoValidoException(message: String) : CuentaBancariaException("El saldo de la cuenta no es válido.")
}