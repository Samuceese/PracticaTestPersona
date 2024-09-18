package persona.repository

import persona.models.Persona
import persona.repository.common.Repository

interface PersonasRepository : Repository<String, Persona> {
}