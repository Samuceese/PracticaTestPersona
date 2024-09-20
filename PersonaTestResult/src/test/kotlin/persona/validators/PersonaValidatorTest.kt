package persona.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import error.CuentaBancariaError
import error.TarjetaError
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import persona.errors.PersonaError
import persona.models.CuentaBancaria
import persona.models.Persona
import persona.models.Tarjeta
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFalse

@ExtendWith(MockitoExtension::class)
class ClienteValidatorTest {

    @Mock
    lateinit var tarjetaValidator: TarjetaValidator

    @Mock
    lateinit var cuentaBancariaValidator: CuentaBancariaValidator

    @Mock
    lateinit var dniValidator: DniValidator

    @InjectMocks
    lateinit var personaValidator: PersonaValidator

    @Test
    fun validarPersonaOk() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(Ok(persona.tarjeta))
        whenever(cuentaBancariaValidator.validarCuenta(persona.cuentaBancaria)).thenReturn(Ok(persona.cuentaBancaria))
        whenever(dniValidator.validarDni(persona.dni)).thenReturn(Ok(persona.dni))

        val result = personaValidator.validarPersona(persona).value

        assertAll(
            { assert(result.nombre == persona.nombre) },
            { assert(result.dni == persona.dni) },
            { assert(result.tarjeta == persona.tarjeta) },
            { assert(result.cuentaBancaria == persona.cuentaBancaria) }
        )

        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
        verify(cuentaBancariaValidator, times(1)).validarCuenta(persona.cuentaBancaria)
        verify(dniValidator, times(1)).validarDni(persona.dni)
    }


    @Test
    fun validarPersonaDniInvalido() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678B",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(Ok(persona.tarjeta))
        whenever(cuentaBancariaValidator.validarCuenta(persona.cuentaBancaria)).thenReturn(Ok(persona.cuentaBancaria))
        whenever(dniValidator.validarDni(persona.dni)).thenReturn(Err(PersonaError.DniNoValidoError(persona.dni)))

        val result = personaValidator.validarPersona(persona).error

        assert(result is PersonaError.DniNoValidoError)

        verify(dniValidator, times(1)).validarDni(persona.dni)
    }

    @Test
    fun validarPersonaTarjetaInvalida() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "123456781234567",
                fecha = "12/26"
            )
        )

        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(
            Err(TarjetaError.NumeroError(persona.tarjeta.numero))
        )


        val result = personaValidator.validarPersona(persona).error

        assert(result is TarjetaError.NumeroError)

        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
    }

    @Test
    fun validarPersonaFechaTarjetaInvalida() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "01/20"
            )
        )

        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(
            Err(TarjetaError.FechaError(persona.tarjeta.fecha))
        )

        val result = personaValidator.validarPersona(persona).error

        assert(result is TarjetaError.FechaError)

        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
    }


    @Test
    fun validarPersonaCuentaInvalida() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES00000000000000000000",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(cuentaBancariaValidator.validarCuenta(persona.cuentaBancaria)).thenReturn(
            Err(CuentaBancariaError.IbanIncorrectoError(persona.cuentaBancaria.iban))
        )


        val result = personaValidator.validarPersona(persona).error

        assert(result is CuentaBancariaError.IbanIncorrectoError)

        verify(cuentaBancariaValidator, times(1)).validarCuenta(persona.cuentaBancaria)
    }

    @Test
    fun validarPersonaSaldoInvalido() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = -50.0 // Saldo inválido
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(cuentaBancariaValidator.validarCuenta(persona.cuentaBancaria)).thenReturn(
            Err(CuentaBancariaError.SaldoError(persona.cuentaBancaria.saldo.toString()))
        )

        val result = personaValidator.validarPersona(persona).error

        assert(result is CuentaBancariaError.SaldoError)

        verify(cuentaBancariaValidator, times(1)).validarCuenta(persona.cuentaBancaria)
    }


    @Test
    fun validarNombreOk() {
        val nombre = "Test1"
        assertTrue(personaValidator.validarNombre(nombre))
    }

    @Test
    fun validarNombreInvalido() {
        val nombre = "T"
        assertFalse(personaValidator.validarNombre(nombre))
    }
}
