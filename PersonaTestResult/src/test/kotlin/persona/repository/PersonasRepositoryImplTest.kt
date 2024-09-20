package persona.repository

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertAll
import persona.models.CuentaBancaria
import persona.models.Persona
import persona.models.Tarjeta
import java.util.*
import kotlin.test.Test

class PersonasRepositoryImplTest {

    private var repository = PersonasRepositoryImpl()

    @BeforeEach
    fun setUp() {
        repository = PersonasRepositoryImpl()

        repository.save(
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
                fecha = "12/26",
            )
        )
        )

        repository.save(Persona(
            id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af17"),
            nombre = "Test2",
            dni = "12345678B",
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 200.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345679",
                fecha = "01/27",
            )
        ))
    }


    @AfterEach
    fun tearDown() {
        repository = PersonasRepositoryImpl()
    }

    @Test
    fun getAll() {
        val result = repository.getAll()

        assertAll(
            { assert(result.size == 2) },
            { assert(result[0].nombre == "Test1") },
            { assert(result[0].dni == "12345678A") },
            { assert(result[1].nombre == "Test2") },
            { assert(result[1].dni == "12345678B") }
        )
    }

    @Test
    fun getById() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        val result = repository.getById(id)

        assertAll(
            { assert(result?.nombre == "Test1") },
        )
    }

    @Test
    fun getByIdNotFound() {
        val id = UUID.fromString("63591beb-d620-4dcf-a719-3a2c4ed88f03")

        val result = repository.getById(id)

        assert(result == null)
    }

    @Test
    fun save() {
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

        val result = repository.save(persona)

        assertAll(
            { assert(result.nombre == "Test3") },
            { assert(result.dni == "12345678C") }
        )
    }

    @Test
    fun update() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")
        val personaInicial = Persona(
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
        repository.save(personaInicial)

        val personaActualizada = Persona(
            id = id,
            nombre = "Test3",  // Nuevo nombre
            dni = "12345678A", // Mismo DNI
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 300.0  // Nuevo saldo
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345680",
                fecha = "11/28"
            )
        )

        val result = repository.update(id, personaActualizada)!!

        assertAll(
            { assert(result.nombre == "Test3") },
            { assert(result.cuentaBancaria.saldo == 300.0) },
            { assert(result.tarjeta.numero == "1234567812345680") },
            { assert(result.tarjeta.fecha == "11/28") }
        )
    }


    @Test
    fun updateNotFound() {
        val id = UUID.fromString("63591beb-d620-4dcf-a719-3a2c4ed88f03")
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
                fecha = "12/26"
            )
        )

        val result = repository.update(id, persona)

        assert(result == null)
    }

    @Test
    fun delete() {
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        val result = repository.delete(id)!!

        assertAll(
            { assert(result.nombre == "Test1") },
            { assert(result.dni == "12345678A") }
        )
    }

    @Test
    fun deleteNotFound() {
        val id = UUID.fromString("63591beb-d620-4dcf-a719-3a2c4ed88f03")

        val result = repository.delete(id)

        assert(result == null)
    }
}