package persona.service

import cache.CachePersonas
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import error.CuentaBancariaError
import error.DniError
import error.TarjetaError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import persona.errors.PersonaError
import persona.models.CuentaBancaria
import persona.models.Persona
import persona.models.Tarjeta
import persona.repository.PersonasRepository
import persona.validators.PersonaValidator
import java.util.*
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class PersonasServiceImplTest {


    @Mock
    lateinit var repository: PersonasRepository

    @Mock
    lateinit var cache: CachePersonas

    @Mock
    lateinit var validator: PersonaValidator

    @InjectMocks
    lateinit var service: PersonasServiceImpl


    @Test
    fun getAll() {
        val personas = listOf(
            Persona(
                id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16"),
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
            ),
            Persona(
                id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af17"),
                nombre = "Test2",
                dni = "12345678B",
                cuentaBancaria = CuentaBancaria(
                    iban = "ES12345678901234567890",
                    saldo = 200.0
                ),
                tarjeta = Tarjeta(
                    numero = "1234567812345679",
                    fecha = "01/27"
                )
            )
        )

        whenever(repository.getAll()).thenReturn(personas)

        val result = service.getAll().value


        assertAll(
            { assertEquals(2, result.size) },
            { assertEquals("Test1", result[0].nombre) },
            { assertEquals("Test2", result[1].nombre) }
        )
        verify(repository, times(1)).getAll()
    }

    @Test
    fun getByIdReturnsOkInCache() {
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

        whenever(cache.get(id)).thenReturn(persona)

        val result = service.getById(id)

        assertAll(
            { assertEquals("Test1", result.value.nombre) },
            { assertEquals(id, result.value.id) }
        )

        verify(cache, times(1)).get(id)
        verify(repository, times(0)).getById(id)
    }

    @Test
    fun getByIdReturnsOkInRepoWhenNotInCache() {
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

        whenever(cache.get(id)).thenReturn(null)
        whenever(repository.getById(id)).thenReturn(persona)

        val result = service.getById(id)

        assertAll(
            { assertEquals("Test1", result.value.nombre) },
            { assertEquals(id, result.value.id) }
        )

        verify(cache, times(1)).get(id)
        verify(repository, times(1)).getById(id)
    }

    @Test
    fun saveOk() {
        val persona = Persona(
            id = UUID.randomUUID(),
            nombre = "Test3",
            dni = "12345678C",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 300.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345680",
                fecha = "11/28"
            )
        )

        whenever(validator.validarPersona(persona)).thenReturn(Ok(persona))
        whenever(repository.save(persona)).thenReturn(persona)

        val result = service.save(persona)

        verify(cache, times(1)).put(persona.id, persona)
        verify(validator, times(1)).validarPersona(persona)
        verify(repository, times(1)).save(persona)
    }

    @Test
    fun saveDniInvalido() {
        val personaInvalida = Persona(
            id = UUID.randomUUID(),
            nombre = "Test1",
            dni = "invalidDNI",  // DNI no válido
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(PersonaError.DniNoValidoError(personaInvalida.dni)))

        val result = service.save(personaInvalida)

        assert(result.error is PersonaError.DniNoValidoError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).save(personaInvalida)
    }

    @Test
    fun saveNombreVacio() {
        val personaInvalida = Persona(
            id = UUID.randomUUID(),
            nombre = "",
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

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(PersonaError.NombreError(personaInvalida.nombre)))

        val result = service.save(personaInvalida)

        assert(result.error is PersonaError.NombreError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).save(personaInvalida)
    }

    @Test
    fun saveCuentaBancariaIbanInvalido() {
        val personaInvalida = Persona(
            id = UUID.randomUUID(),
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "INVALID_IBAN",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(CuentaBancariaError.IbanIncorrectoError(personaInvalida.cuentaBancaria.iban)))

        val result = service.save(personaInvalida)

        assert(result.error is CuentaBancariaError.IbanIncorrectoError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).save(personaInvalida)
    }

    @Test
    fun saveCuentaBancariaSaldoInvalido() {
        val personaInvalida = Persona(
            id = UUID.randomUUID(),
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = -12.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(CuentaBancariaError.SaldoError(personaInvalida.cuentaBancaria.saldo.toString())))

        val result = service.save(personaInvalida)

        assert(result.error is CuentaBancariaError.SaldoError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).save(personaInvalida)
    }

    @Test
    fun saveTarjetaNumeroInvalida() {
        val personaInvalida = Persona(
            id = UUID.randomUUID(),
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "INVALID_CARD_NUMBER",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(TarjetaError.NumeroError(personaInvalida.tarjeta.numero)))

        val result = service.save(personaInvalida)

        assert(result.error is TarjetaError.NumeroError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).save(personaInvalida)
    }

    @Test
    fun saveTarjetaFechaInvalida() {
        val personaInvalida = Persona(
            id = UUID.randomUUID(),
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "34/53"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(TarjetaError.FechaError(personaInvalida.tarjeta.fecha)))

        val result = service.save(personaInvalida)

        assert(result.error is TarjetaError.FechaError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).save(personaInvalida)
    }


    @Test
    fun updateOk() {
        val id = UUID.randomUUID()
        val personaOriginal = Persona(
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

        val personaActualizada = personaOriginal.copy(nombre = "UpdatedTest")

        whenever(repository.update(id, personaActualizada)).thenReturn(personaActualizada)
        whenever(validator.validarPersona(personaActualizada)).thenReturn(Ok(personaActualizada))

        val result = service.update(id, personaActualizada)

        assertEquals(personaActualizada, result.value)

        verify(cache, times(1)).put(id, personaActualizada)
        verify(validator, times(1)).validarPersona(personaActualizada)
        verify(repository, times(1)).update(id, personaActualizada)
    }



    @Test
    fun updateDniInvalido() {
        val id = UUID.randomUUID()
        val personaInvalida = Persona(
            id = id,
            nombre = "Test1",
            dni = "invalidDNI",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(PersonaError.DniNoValidoError(personaInvalida.dni)))

        val result = service.update(id, personaInvalida)

        assert(result.error is PersonaError.DniNoValidoError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).update(id, personaInvalida)
    }

    @Test
    fun updateNombreVacio() {
        val id = UUID.randomUUID()
        val personaInvalida = Persona(
            id = id,
            nombre = "",
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

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(PersonaError.NombreError(personaInvalida.nombre)))

        val result = service.update(id, personaInvalida)

        assert(result.error is PersonaError.NombreError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).update(id, personaInvalida)
    }

    @Test
    fun updateCuentaBancariaIbanInvalido() {
        val id = UUID.randomUUID()
        val personaInvalida = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "INVALID_IBAN",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(CuentaBancariaError.IbanIncorrectoError(personaInvalida.cuentaBancaria.iban)))

        val result = service.update(id, personaInvalida)

        assert(result.error is CuentaBancariaError.IbanIncorrectoError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).update(id, personaInvalida)
    }

    @Test
    fun updateCuentaBancariaSaldoInvalido() {
        val id = UUID.randomUUID()
        val personaInvalida = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = -12.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(CuentaBancariaError.SaldoError(personaInvalida.cuentaBancaria.saldo.toString())))

        val result = service.update(id, personaInvalida)

        assert(result.error is CuentaBancariaError.SaldoError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).update(id, personaInvalida)
    }

    @Test
    fun updateTarjetaNumeroInvalida() {
        val id = UUID.randomUUID()
        val personaInvalida = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "INVALID_CARD_NUMBER",
                fecha = "12/26"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(TarjetaError.NumeroError(personaInvalida.tarjeta.numero)))

        val result = service.update(id, personaInvalida)

        assert(result.error is TarjetaError.NumeroError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).update(id, personaInvalida)
    }

    @Test
    fun updateTarjetaFechaInvalida() {
        val id = UUID.randomUUID()
        val personaInvalida = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "34/53"
            )
        )

        whenever(validator.validarPersona(personaInvalida)).thenReturn(Err(TarjetaError.FechaError(personaInvalida.tarjeta.fecha)))

        val result = service.update(id, personaInvalida)

        assert(result.error is TarjetaError.FechaError)

        verify(validator, times(1)).validarPersona(personaInvalida)
        verify(cache, times(0)).put(personaInvalida.id, personaInvalida)
        verify(repository, times(0)).update(id, personaInvalida)
    }

    @Test
    fun deleteOk() {
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

        whenever(repository.delete(id)).thenReturn(persona)

        val result = service.delete(id)

        assertEquals(persona, result.value)

        verify(cache, times(1)).remove(id)
        verify(repository, times(1)).delete(id)
    }

    @Test
    fun deleteNotFound() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        whenever(repository.delete(id)).thenReturn(null)

        val result = service.delete(id)

        assert(result.error is PersonaError.PersonaNoEliminadoError)

        verify(cache, times(0)).remove(id)
        verify(repository, times(1)).delete(id)
    }


}






