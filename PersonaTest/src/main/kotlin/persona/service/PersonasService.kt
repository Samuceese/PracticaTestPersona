package persona.service

import persona.models.Persona
import java.util.UUID

interface PersonasService {
    fun getAll(): List<Persona>
    fun findById(id: UUID): Persona
    fun save(persona: Persona): Persona
    fun update(id: UUID,persona: Persona): Persona
    fun delete(id: UUID):Persona
}