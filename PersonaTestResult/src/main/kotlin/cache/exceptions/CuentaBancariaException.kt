package cache.exceptions

sealed class CuentaBancariaException(message : String) : RuntimeException(message) {
    class IbanNoValidoException() : CuentaBancariaException("La cuenta bancaria no es válida.")
    class SaldoNoValidoException() : CuentaBancariaException("El saldo de la cuenta no es válido.")
    class CuentaBancariaInvalidaException() : CuentaBancariaException("La cuenta bancaria no es válida.")
}