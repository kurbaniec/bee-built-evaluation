package test.bee.persistent.blaze

import com.beeproduced.bee.persistent.blaze.BeeBlazeRepository
import com.beeproduced.bee.persistent.blaze.annotations.BeeRepository
import common.*
import jakarta.persistence.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@Entity
class Movie(
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
) : MovieBase

@BeeRepository
interface MovieRepository : BeeBlazeRepository<Movie, Long>

@Entity
data class CinemaHall(
    @Id @GeneratedValue
    override val id: Long = 0,
    override val hallName: String,
    override val capacity: Int,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cinemaHall")
    override val popcornStands: Set<PopcornStand>? = null
) : CinemaHallBase

@BeeRepository
interface CinemaHallRepository : BeeBlazeRepository<CinemaHall, Long>

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
) : TicketBase

@BeeRepository
interface TicketRepository : BeeBlazeRepository<Ticket, Long>

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
) : PopcornStandBase

@BeeRepository
interface PopcornStandRepository : BeeBlazeRepository<PopcornStand, Long>

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
) : CinemaBuffBase

@BeeRepository
interface CinemaBuffRepository : BeeBlazeRepository<CinemaBuff, Long>