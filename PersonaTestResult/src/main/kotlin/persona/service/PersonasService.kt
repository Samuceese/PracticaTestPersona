package persona.service

import com.github.michaelbull.result.Result
import error.AllError
import persona.models.Persona
import java.util.*

interface PersonasService {
    fun getAll(): Result<List<Persona>,AllError>
    fun getById(id: UUID): Result<Persona,AllError>
    fun save(persona: Persona): Result<Persona,AllError>
    fun update(id: UUID, persona: Persona): Result<Persona,AllError>
    fun delete(id: UUID): Result<Persona,AllError>
}