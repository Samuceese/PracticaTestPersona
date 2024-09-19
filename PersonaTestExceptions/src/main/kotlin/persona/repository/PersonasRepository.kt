package persona.repository

import persona.models.Persona
import persona.repository.common.Repository
import java.util.*

interface PersonasRepository : Repository<UUID, Persona> {
}