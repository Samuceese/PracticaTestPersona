package personas.repository

import PersonasRepositoryImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import persona.models.CuentaBancaria
import persona.models.Persona
import persona.models.Tarjeta
import java.util.*

class PersonasRepositoryImplTest {

    private var repository = PersonasRepositoryImpl()

    @BeforeEach
    fun setUp() {
        repository = PersonasRepositoryImpl()

        repository.save(Persona(
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
        ))

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
        // Act
        val result = repository.getAll()

        // Assert
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
        // Arrange
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        // Act
        val result = repository.getById(id)

        // Assert
        assertAll(
            { assert(result?.nombre == "Test1") },
        )
    }

    @Test
    fun getByIdNotFound() {
        // Arrange
        val id = UUID.fromString("63591beb-d620-4dcf-a719-3a2c4ed88f03")

        // Act
        val result = repository.getById(id)

        // Assert
        assert(result == null)
    }

    @Test
    fun save() {
        // Arrange
        val persona = Persona(
            id = UUID.randomUUID(),  // Usar un ID nuevo para evitar conflicto
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

        // Act
        val result = repository.save(persona)

        // Assert
        assertAll(
            { assert(result.nombre == "Test3") },
            { assert(result.dni == "12345678C") }
        )
    }

    @Test
    fun update() {
        // Arrange
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        val persona = Persona(
            id = id,  // Usar el mismo ID
            nombre = "Test3",
            dni = "12345678A",  // DNI no debe cambiar
            cuentaBancaria = CuentaBancaria(
                iban = "ES12345678901234567890",
                saldo = 300.0
            ),
            tarjeta = Tarjeta(
                numero = "1234567812345681",
                fecha = "11/28"
            )
        )

        // Act
        val result = repository.update(id, persona)!!

        // Assert
        assertAll(
            { assert(result.nombre == "Test3") },
            { assert(result.dni == "12345678A") }
        )
    }

    @Test
    fun updateNotFound() {
        // Arrange
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

        // Act
        val result = repository.update(id, persona)

        // Assert
        assert(result == null)
    }

    @Test
    fun delete() {
        // Arrange
        val id = UUID.fromString("23d41191-8a78-4c02-9127-06e76b56af16")

        // Act
        val result = repository.delete(id)!!

        // Assert
        assertAll(
            { assert(result.nombre == "Test1") },
            { assert(result.dni == "12345678A") }
        )
    }

    @Test
    fun deleteNotFound() {
        // Arrange
        val id = UUID.fromString("63591beb-d620-4dcf-a719-3a2c4ed88f03")

        // Act
        val result = repository.delete(id)

        // Assert
        assert(result == null)
    }
}