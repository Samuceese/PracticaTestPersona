package persona.repository

import persona.models.Persona
import java.util.*


interface PersonasRepository {
    fun getAll(): List<Persona>
    fun getById(id: UUID): Persona?
    fun save(persona: Persona): Persona
    fun update(id: UUID, t: Persona): Persona?
    fun delete(id: UUID): Persona?
}