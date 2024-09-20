package cache

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import persona.models.CuentaBancaria
import persona.models.Persona
import persona.models.Tarjeta
import java.util.*

class CachePersonasImplTest {


    private var cache = CachePersonasImpl()

    @BeforeEach
    fun setUp() {
        cache.clear()
    }


    @Test
    fun get() {
        val persona = Persona(
            id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16"),
            nombre = "Test01",
            dni = "12345678Z",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26",
            )
        )

        cache.put(persona.id, persona)
        val result = cache.get(persona.id)


        assertAll(
            { assertEquals(1, cache.size()) },
            { assertNotNull(result) },
            { assertEquals(persona, result) }
        )
    }

    @Test
    fun getNotFound() {
        val invalidId = UUID.fromString("e6d1d6d4-2b62-4a0b-8e08-8c227c2c9c7e")

        assertNull(cache.get(invalidId))
    }

    @Test
    fun put() {
        val persona = Persona(
            id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16"),
            nombre = "Test01",
            dni = "12345678Z",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 100.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345678",
                fecha = "12/26",
            )
        )
        cache.put(persona.id, persona)
        val result = cache.get(persona.id)

        assertAll(
            { assertEquals(1, cache.size()) },
            { assertNotNull(result) },
            { assertEquals(persona, result) }
        )

    }

    @Test
    fun remove() {
        val persona = Persona(
            id = UUID.fromString("a7c9d1f4-3e5b-46b7-9f2d-88e6b45d01a3"),
            nombre = "Test1",
            dni = "12345678Z",
            cuentaBancaria = CuentaBancaria("ES91 2100 0418 4502 0005 1332",100.0),
            tarjeta = Tarjeta("4539 1488 0343 6467","12/25"),
        )
        cache.put(persona.id, persona)
        cache.remove(persona.id)
        val result = cache.get(persona.id)

        assertAll(
            { assertEquals(cache.size(), 0) },
            { assertNull(result) }
        )
    }

    @Test
    fun clear() {
    }

    @Test
    fun size() {
    }
}