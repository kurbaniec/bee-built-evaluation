package test.jpa.lazy

import com.beeproduced.bee.persistent.jpa.meta.MetaModel
import jakarta.annotation.PostConstruct
import jakarta.persistence.EntityManager
import org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */
@Component
class UnproxyConfig(
    @Qualifier("testEM") val em: EntityManager
) {

    @PostConstruct
    fun setupUnproxy() {
        val types = listOf(
            Pair(Movie::class.java, Long::class.java),
            Pair(CinemaHall::class.java, Long::class.java),
            Pair(Ticket::class.java, Long::class.java),
            Pair(PopcornStand::class.java, Long::class.java),
            Pair(CinemaBuff::class.java, Long::class.java)
        )
        for ((entity, id) in types) {
            val mappingModel = em.entityManagerFactory.metamodel as MappingMetamodelImpl
            val entityPersister = mappingModel.getEntityDescriptor(entity)
            MetaModel.addEntity(entity, id, entityPersister)
        }
    }
}