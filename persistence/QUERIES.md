# Persistence Evaluation | Queries

data size per test (ideal):

empty selection

1 entity

1 table

---

 ## bee.persistent.jpa



## jpa.eager

### all selection

* 4 Queries
* 1 CinemaBuff - PopcornStand JOIN CinemaHall
* 1 CinemaBuff - PopcornStand - CinemaHall - PopcornStand
* 1 CinemaBuff - Ticket JOIN Movie JOIN CinemaHall JOIN PopcornStand
* On `dataSize=2`:  7 Queries ((4-1)*2)

```sql
2024-03-25T17:49:23.883+01:00 DEBUG 19964 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T17:49:23.889+01:00 DEBUG 19964 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,c1_0.id,c1_0.capacity,c1_0.hallName,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 left join CinemaHall c1_0 on c1_0.id=p1_0.cinemaHallId where p1_0.id=?
2024-03-25T17:49:23.889+01:00 TRACE 19964 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:49:23.893+01:00 DEBUG 19964 --- [    Test worker] org.hibernate.SQL                        : select p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.cinemaHallId=?
2024-03-25T17:49:23.894+01:00 TRACE 19964 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:49:23.897+01:00 DEBUG 19964 --- [    Test worker] org.hibernate.SQL                        : select t1_0.cinemaBuffId,t1_0.id,m1_0.id,c2_0.id,c2_0.capacity,c2_0.hallName,p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title,t1_0.movieId,t1_0.price,t1_0.seatNumber from Ticket t1_0 left join Movie m1_0 on m1_0.id=t1_0.movieId left join CinemaHall c2_0 on c2_0.id=m1_0.cinemaHallId left join PopcornStand p1_0 on c2_0.id=p1_0.cinemaHallId where t1_0.cinemaBuffId=?
2024-03-25T17:49:23.897+01:00 TRACE 19964 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1
```

## jpa.lazy

### empty selection

* 3 Queries
* 1 CinemaBuff
* 1 CinemaBuff - PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* On `dataSize=2`:  5 Queries

```sql
2024-03-25T17:06:10.996+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T17:06:11.030+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T17:06:11.030+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.033+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.033+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
```

> Hibernate then needs to perform an additional query for each of the  selected entities. That is often called a n+1 select issue. You can  learn more about it in my free course [How to find and fix n+1 select issues](https://thorben-janssen.com/free-n1_select_course/).
>
> https://thorben-janssen.com/best-practices-many-one-one-many-associations-mappings/

### partial selection - favoritePopcornStand

* 3 Queries
* 1 CinemaBuff
* 1 CinemaBuff - PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* On `dataSize=2`:  5 Queries

```sql
2024-03-25T17:06:11.098+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T17:06:11.100+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T17:06:11.101+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.103+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.103+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
```

### partial selection - movie

* 7 Queries
* 1 CinemaBuff
* 1 CinemaBuff - PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* 1 CinemaBuff - Ticket
* 2 CinemaBuff - Ticket - Movie
* 1 CinemaBuff - Ticket - Movie - CinemaHall
* On `dataSize=2`:  13 Queries

```sql
2024-03-25T17:06:11.118+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T17:06:11.121+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T17:06:11.122+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.124+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.124+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.133+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select t1_0.cinemaBuffId,t1_0.id,t1_0.movieId,t1_0.price,t1_0.seatNumber from Ticket t1_0 where t1_0.cinemaBuffId=?
2024-03-25T17:06:11.134+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.138+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title from Movie m1_0 where m1_0.id=?
2024-03-25T17:06:11.138+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.142+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title from Movie m1_0 where m1_0.id=?
2024-03-25T17:06:11.143+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.146+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.146+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
```

### partial selection - ticket

* 9 Queries
* 1 CinemaBuff
* 1 CinemaBuff - PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* 1 CinemaBuff - Ticket
* 2 CinemaBuff - Ticket - Movie
* 1 CinemaBuff - Ticket - Movie - CinemaHall
* 2 CinemaBuff - Ticket - Movie - CinemaHall - PopCornStand
* On `dataSize=2`:  17 Queries

```sql
2024-03-25T17:06:11.164+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T17:06:11.168+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T17:06:11.168+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.170+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.171+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.174+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select t1_0.cinemaBuffId,t1_0.id,t1_0.movieId,t1_0.price,t1_0.seatNumber from Ticket t1_0 where t1_0.cinemaBuffId=?
2024-03-25T17:06:11.175+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.177+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title from Movie m1_0 where m1_0.id=?
2024-03-25T17:06:11.177+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.180+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title from Movie m1_0 where m1_0.id=?
2024-03-25T17:06:11.181+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.184+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.184+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.187+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.cinemaHallId=?
2024-03-25T17:06:11.188+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.190+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.cinemaHallId=?
2024-03-25T17:06:11.191+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]

```

### full selection

* 9 Queries
* 1 CinemaBuff
* 1 CinemaBuff - PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* 1 CinemaBuff - Ticket
* 2 CinemaBuff - Ticket - Movie
* 1 CinemaBuff - Ticket - Movie - CinemaHall
* 2 CinemaBuff - Ticket - Movie - CinemaHall - PopCornStand
* On `dataSize=2`:  17 Queries

```sql
2024-03-25T17:06:11.204+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T17:06:11.207+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T17:06:11.208+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.211+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.211+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.215+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select t1_0.cinemaBuffId,t1_0.id,t1_0.movieId,t1_0.price,t1_0.seatNumber from Ticket t1_0 where t1_0.cinemaBuffId=?
2024-03-25T17:06:11.215+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.217+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title from Movie m1_0 where m1_0.id=?
2024-03-25T17:06:11.217+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T17:06:11.221+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title from Movie m1_0 where m1_0.id=?
2024-03-25T17:06:11.221+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.224+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T17:06:11.225+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.228+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.cinemaHallId=?
2024-03-25T17:06:11.228+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
2024-03-25T17:06:11.232+01:00 DEBUG 16588 --- [    Test worker] org.hibernate.SQL                        : select p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.cinemaHallId=?
2024-03-25T17:06:11.233+01:00 TRACE 16588 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
```

