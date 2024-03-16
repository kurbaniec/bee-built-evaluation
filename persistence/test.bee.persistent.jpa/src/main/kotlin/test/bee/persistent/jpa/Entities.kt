package test.bee.persistent.jpa

import com.beeproduced.bee.persistent.jpa.entity.DataEntity
import com.beeproduced.bee.persistent.jpa.repository.BaseDataRepository
import common.*
import jakarta.persistence.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@Entity
data class Movie(
    @Id @GeneratedValue
    override val id: Long = 0,
    override val title: String,
    override val director: String,
    override val durationInMinutes: Int,
    override val genre: String,
    val cinemaHallId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinemaHallId", referencedColumnName = "id", insertable = false, updatable = false)
    override val cinemaHall: CinemaHall? = null
) : MovieBase, DataEntity<Movie> {
    override fun clone(): Movie = copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Movie) return false

        if (id != other.id) return false
        if (title != other.title) return false
        if (director != other.director) return false
        if (durationInMinutes != other.durationInMinutes) return false
        if (genre != other.genre) return false
        if (cinemaHallId != other.cinemaHallId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + director.hashCode()
        result = 31 * result + durationInMinutes
        result = 31 * result + genre.hashCode()
        result = 31 * result + cinemaHallId.hashCode()
        return result
    }
}

@Component
class MovieRepository(
    @Qualifier("testEM") em: EntityManager
): BaseDataRepository<Movie, Long>(em)

@Entity
data class CinemaHall(
    @Id @GeneratedValue
    override val id: Long = 0,
    override val hallName: String,
    override val capacity: Int,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cinemaHall")
    override val popcornStands: Set<PopcornStand>? = null
) : CinemaHallBase, DataEntity<CinemaHall> {
    override fun clone(): CinemaHall = this.copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CinemaHall) return false

        if (id != other.id) return false
        if (hallName != other.hallName) return false
        if (capacity != other.capacity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + hallName.hashCode()
        result = 31 * result + capacity
        return result
    }
}

@Component
class CinemaHallRepository(
    @Qualifier("testEM") em: EntityManager
) : BaseDataRepository<CinemaHall, Long>(em)

@Entity
data class Ticket(
    @Id @GeneratedValue
    override val id: Long = 0,
    override val price: Double,
    override val seatNumber: String,
    val movieId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    override val movie: Movie? = null,
    val cinemaBuffId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinemaBuffId", referencedColumnName = "id", insertable = false, updatable = false)
    val cinemaBuff: CinemaBuff? = null
) : TicketBase, DataEntity<Ticket> {
    override fun clone(): Ticket = copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ticket) return false

        if (id != other.id) return false
        if (price != other.price) return false
        if (seatNumber != other.seatNumber) return false
        if (movieId != other.movieId) return false
        if (cinemaBuffId != other.cinemaBuffId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + seatNumber.hashCode()
        result = 31 * result + movieId.hashCode()
        result = 31 * result + cinemaBuffId.hashCode()
        return result
    }
}

@Component
class TicketRepository(
    @Qualifier("testEM") em: EntityManager
) : BaseDataRepository<Ticket, Long>(em)

@Entity
data class PopcornStand(
    @Id @GeneratedValue
    override val id: Long = 0,
    override val name: String,
    override val flavor: String,
    override val price: Double,
    val cinemaHallId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinemaHallId", referencedColumnName = "id", insertable = false, updatable = false)
    val cinemaHall: CinemaHall? = null
) : PopcornStandBase, DataEntity<PopcornStand> {
    override fun clone(): PopcornStand = copy()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PopcornStand) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (flavor != other.flavor) return false
        if (price != other.price) return false
        if (cinemaHallId != other.cinemaHallId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + flavor.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + cinemaHallId.hashCode()
        return result
    }
}

@Component
class PopcornStandRepository(
    @Qualifier("testEM") em: EntityManager
) : BaseDataRepository<PopcornStand, Long>(em)

@Entity
data class CinemaBuff(
    @Id @GeneratedValue
    override val id: Long = 0,
    override val name: String,
    override val favoriteGenre: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cinemaBuff")
    override val tickets: Set<Ticket>? = null,
    val favoritePopCornStandId: Long,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favoritePopCornStandId", insertable = false, updatable = false)
    override val favoritePopcornStand: PopcornStand? = null
) : CinemaBuffBase, DataEntity<CinemaBuff> {
    override fun clone(): CinemaBuff = copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CinemaBuff) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (favoriteGenre != other.favoriteGenre) return false
        if (favoritePopCornStandId != other.favoritePopCornStandId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + favoriteGenre.hashCode()
        result = 31 * result + favoritePopCornStandId.hashCode()
        return result
    }
}

@Component
class CinemaBuffRepository(
    @Qualifier("testEM") em: EntityManager
) : BaseDataRepository<CinemaBuff, Long>(em)