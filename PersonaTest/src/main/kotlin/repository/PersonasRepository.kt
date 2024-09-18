package repository

import models.Persona
import repository.common.Repository
import java.util.UUID

interface PersonasRepository : Repository<String, Persona> {
}