package persona.service

import PersonasRepositoryImpl
import cache.exceptions.CuentaBancariaException
import cache.exceptions.DniExceptions
import cache.exceptions.PersonaException
import cache.exceptions.TarjetaException
import org.example.cache.CachePersonasImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import persona.models.CuentaBancaria
import persona.models.Persona
import persona.models.Tarjeta
import persona.validators.CuentaBancariaValidator
import persona.validators.DniValidator
import persona.validators.TarjetaValidator
import service.PersonasServiceImpl
import java.util.*
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class PersonasServiceImplTest {

    @Mock
    private lateinit var cache: CachePersonasImpl

    @Mock
    private lateinit var repository: PersonasRepositoryImpl

    @Mock
    private lateinit var dniValidator: DniValidator

    @Mock
    private lateinit var cuentaBancariaValidator: CuentaBancariaValidator

    @Mock
    private lateinit var tarjetaValidator: TarjetaValidator

    @InjectMocks
    private lateinit var service: PersonasServiceImpl


    @Test
    fun getAllOk() {
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

        val result = service.getAll()

        assertEquals(personas, result)
        verify(repository, times(1)).getAll()
    }


    @Test
    fun findByIdInCache() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val personaEnCache = Persona(
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

        whenever(cache.get(id)).thenReturn(personaEnCache)

        val result = service.findById(id)

        assertEquals(personaEnCache, result)
        verify(cache, times(1)).get(id)
    }

    @Test
    fun findByIdNotInCacheButInRepository() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val personaEnRepository = Persona(
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
        whenever(repository.getById(id)).thenReturn(personaEnRepository)

        val result = service.findById(id)

        assertEquals(personaEnRepository,result)
        verify(cache, times(1)).get(id)
        verify(repository, times(1)).getById(id)
    }

    @Test
    fun findByIdNotInCacheAndNotInRepository() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        whenever(cache.get(id)).thenReturn(null)
        whenever(repository.getById(id)).thenReturn(null)

        val exception = assertFailsWith<PersonaException.PersonaNoEncontradaExcepcion>{
            service.findById(id)
        }

        assertEquals("La persona no ha sido encontrada.", exception.message)

        verify(cache, times(1)).get(id)
        verify(repository, times(1)).getById(id)
    }

    @Test
    fun savePersonaOk() {
        val persona = Persona(
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
        )

        whenever(dniValidator.validarDni(persona.dni)).thenReturn(persona.dni.toBoolean())
        whenever(cuentaBancariaValidator.validate(persona.cuentaBancaria)).thenReturn(persona.cuentaBancaria)
        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(persona.tarjeta)
        whenever(repository.save(persona)).thenReturn(persona)

        val result = service.save(persona)

        assertEquals(persona, result)
        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
        verify(repository, times(1)).save(persona)
        verify(cache, times(1)).put(persona.id, persona)
    }

    @Test
    fun savePersonaInvalidDniValidator() {
        val persona = Persona(
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
        )

        doThrow(DniExceptions.DniInvalidoException()).whenever(dniValidator).validarDni(persona.dni)

        assertThrows<DniExceptions.DniInvalidoException> {
            service.save(persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(0)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(0)).validarTarjeta(persona.tarjeta)
        verify(repository, times(0)).save(persona)
        verify(cache, times(0)).put(persona.id, persona)
    }

    @Test
    fun savePersonaInvalidCuentaBancariaValidator() {
        val persona = Persona(
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
        )

        doThrow(CuentaBancariaException.CuentaBancariaInvalidaException()).whenever(cuentaBancariaValidator).validate(persona.cuentaBancaria)

        assertThrows<CuentaBancariaException.CuentaBancariaInvalidaException> {
            service.save(persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(0)).validarTarjeta(persona.tarjeta)
        verify(repository, times(0)).save(persona)
        verify(cache, times(0)).put(persona.id, persona)
    }

    @Test
    fun savePersonaInvalidTarjetaValidator() {
        val persona = Persona(
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
        )

        doThrow(TarjetaException.TarjetaNoValidaException()).whenever(tarjetaValidator).validarTarjeta(persona.tarjeta)

        assertThrows<TarjetaException.TarjetaNoValidaException> {
            service.save(persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
        verify(repository, times(0)).save(persona)
        verify(cache, times(0)).put(persona.id, persona)
    }

    @Test
    fun updateOk() {
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

        whenever(dniValidator.validarDni(persona.dni)).thenReturn(persona.dni.toBoolean())
        whenever(cuentaBancariaValidator.validate(persona.cuentaBancaria)).thenReturn(persona.cuentaBancaria)
        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(persona.tarjeta)
        whenever(repository.update(id, persona)).thenReturn(persona)

        val result = service.update(id, persona)

        assertEquals(persona, result, "La persona actualizada debería coincidir con la entrada")

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
        verify(repository, times(1)).update(id, persona)
        verify(cache, times(1)).put(persona.id, persona)
    }

    @Test
    fun updateInvalidDniValidator() {
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

        doThrow(DniExceptions.DniInvalidoException()).whenever(dniValidator).validarDni(persona.dni)

        assertThrows<DniExceptions.DniInvalidoException> {
            service.update(id, persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(0)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(0)).validarTarjeta(persona.tarjeta)
        verify(repository, times(0)).update(id, persona)
        verify(cache, times(0)).put(persona.id, persona)
    }
    @Test
    fun updateInvalidCuentaBancariaValidator() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val persona = Persona(
            id = id,
            nombre = "Test1",
            dni = "12345678A",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = -50.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26"
            )
        )

        whenever(dniValidator.validarDni(persona.dni)).thenReturn(persona.dni.toBoolean())
        doThrow(CuentaBancariaException.SaldoNoValidoException()).whenever(cuentaBancariaValidator).validate(persona.cuentaBancaria)

        assertThrows<CuentaBancariaException.SaldoNoValidoException> {
            service.update(id, persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(0)).validarTarjeta(persona.tarjeta)
        verify(repository, times(0)).update(id, persona)
        verify(cache, times(0)).put(persona.id, persona)
    }
    @Test
    fun updateInvalidTarjetaValidator() {
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

        whenever(dniValidator.validarDni(persona.dni)).thenReturn(persona.dni.toBoolean())
        whenever(cuentaBancariaValidator.validate(persona.cuentaBancaria)).thenReturn(persona.cuentaBancaria)
        doThrow(TarjetaException.TarjetaNoValidaException()).whenever(tarjetaValidator).validarTarjeta(persona.tarjeta)

        assertThrows<TarjetaException.TarjetaNoValidaException> {
            service.update(id, persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
        verify(repository, times(0)).update(id, persona)
        verify(cache, times(0)).put(persona.id, persona)
    }
    @Test
    fun updateNotFoundInRepository() {
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

        whenever(dniValidator.validarDni(persona.dni)).thenReturn(persona.dni.toBoolean())
        whenever(cuentaBancariaValidator.validate(persona.cuentaBancaria)).thenReturn(persona.cuentaBancaria)
        whenever(tarjetaValidator.validarTarjeta(persona.tarjeta)).thenReturn(persona.tarjeta)
        whenever(repository.update(id, persona)).thenReturn(null)

        assertThrows<PersonaException.PersonaNoActualizadaExcepcion> {
            service.update(id, persona)
        }

        verify(dniValidator, times(1)).validarDni(persona.dni)
        verify(cuentaBancariaValidator, times(1)).validate(persona.cuentaBancaria)
        verify(tarjetaValidator, times(1)).validarTarjeta(persona.tarjeta)
        verify(repository, times(1)).update(id, persona)
        verify(cache, times(0)).put(persona.id, persona)
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

        assertEquals(persona, result, "La persona eliminada debería coincidir con la entrada")

        verify(cache, times(1)).remove(id)
        verify(repository, times(1)).delete(id)
    }
    @Test
    fun deleteNotFoundById() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        whenever(cache.remove(id)).thenReturn(null)
        whenever(repository.delete(id)).thenReturn(null)

        assertThrows<PersonaException.PersonaNoBorradaExcepcion> {
            service.delete(id)
        }

        verify(cache, times(1)).remove(id)
        verify(repository, times(1)).delete(id)
    }

}
