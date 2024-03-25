# Persistence Evaluation | Queries

## Data

Data set:

* Cinemabuff
  * Popcornstand (1x)
  * Tickets (2x)
    * Movie (1x)
      * CinemaHall (1x)
        * Popcornstand (2x)

> FavoritePopCornStand (1x) is part of Popcornstand (2x)

Data size per test in ideal scenario:

> use following to access testcontainer
>
> ```
> psql -U test
> \c test
> ```

### empty selection

* 1 Entity
  * 1 CinemaBuff
* 1 Table (CinemaBuff)
* 1 Row
* On `dataSize=125`:  125 Rows
* On `dataSize=250`:  250 Rows
* On `dataSize=500`:  500 Rows

```sql
select id from cinemabuff;
```

```sql
select count(*) from 
(select id from cinemabuff group by id);
```

### partial selection - favoritePopcornStand

* 2 Entities
  * 1 CinemaBuff
  * 1 PopcornStand
* 2 Tables (CinemaBuff, PopcornStand)
* 1 Row
* On `dataSize=125`:  125 Rows
* On `dataSize=250`:  250 Rows
* On `dataSize=500`:  500 Rows

```sql
select cb.id as cbid, fp.id as fpid from cinemabuff cb left join popcornstand fp on fp.id=cb.favoritePopCornStandId;
```

```sql
select count(*) from
(select cb.id as cbid, fp.id as fpid from cinemabuff cb left join popcornstand fp on fp.id=cb.favoritePopCornStandId);
```

### partial selection - movie

* 5 Entities
  * 1 CinemaBuff
  * 2 Tickets
  * 2 Movies
* 5 Tables (CinemaBuff, Tickets, Movies)
* 2 Rows
* On `dataSize=125`:  250 Rows
* On `dataSize=250`:  500 Rows
* On `dataSize=500`:  1000 Rows

```sql
select cb.id as cbid, t.id as tid, m.id as mid from cinemabuff cb left join ticket t on cb.id=t.cinemaBuffId left join movie m on m.id=t.movieId;
```

```sql
select count(*) from
(select cb.id as cbid, t.id as tid, m.id as mid from cinemabuff cb left join ticket t on cb.id=t.cinemaBuffId left join movie m on m.id=t.movieId);
```

### partial selection - ticket

* 11 Entities
  * 1 CinemaBuff
  * 2 Tickets
  * 2 Movies
  * 2 CinemaHall
  * 4 PopcornStand
* 5 Tables (CinemaBuff, Tickets, Movies, CinemaHall, PopcornStand)
* 4 Row
* On `dataSize=125`:  500 Rows
* On `dataSize=250`:  1000 Rows
* On `dataSize=500`:  2000 Rows

```sql
select cb.id as cbId, t.id as tid, m.id as mid, ch.id as chid, p.id as pid from cinemabuff cb left join ticket t on cb.id=t.cinemaBuffId left join movie m on m.id=t.movieId left join cinemahall ch on ch.id=m.cinemaHallId left join popcornstand p on ch.id=p.cinemaHallId;
```

```sql
select count(*) from
(select cb.id as cbId, t.id as tid, m.id as mid, ch.id as chid, p.id as pid from cinemabuff cb left join ticket t on cb.id=t.cinemaBuffId left join movie m on m.id=t.movieId left join cinemahall ch on ch.id=m.cinemaHallId left join popcornstand p on ch.id=p.cinemaHallId);
```

### full selection

* 11 Entities
  * 1 CinemaBuff
  * 2 Tickets
  * 2 Movies
  * 2 CinemaHall
  * 4 PopcornStand
* 6 Tables (CinemaBuff, Tickets, Movies, CinemaHall, 2x PopcornStand)
* 4 Rows
* On `dataSize=125`:  500 Rows
* On `dataSize=250`:  1000 Rows
* On `dataSize=500`:  2000 Rows

```sql
select cb.id as cbid, fp.id as fpid, t.id as tid, m.id as mid, ch.id as chid, p.id as pid from cinemabuff cb left join popcornstand fp on fp.id=cb.favoritePopcornStandId left join ticket t on cb.id=t.cinemaBuffId left join movie m on m.id=t.movieId left join cinemahall ch on ch.id=m.cinemaHallId left join popcornstand p on ch.id=p.cinemaHallId;
```

```sql
select count(*) from
(select cb.id as cbid, fp.id as fpid, t.id as tid, m.id as mid, ch.id as chid, p.id as pid from cinemabuff cb left join popcornstand fp on fp.id=cb.favoritePopcornStandId left join ticket t on cb.id=t.cinemaBuffId left join movie m on m.id=t.movieId left join cinemahall ch on ch.id=m.cinemaHallId left join popcornstand p on ch.id=p.cinemaHallId);
```



---

## bee.persistent.blaze

### empty selection

* 1 Query
* 1 CinemaBuff
* On `dataSize=2`:  1 Queries

```sql
2024-03-25T19:20:13.489+01:00 DEBUG 27348 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,null,null,null,null,null,null,null,null,null,c1_0.name,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from CinemaBuff c1_0
```

### partial selection - favoritePopcornStand

* 1 Query
* 1 CinemaBuff JOIN PopcornStand
* On `dataSize=2`:  1 Queries

```sql
2024-03-25T19:20:13.583+01:00 DEBUG 27348 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,null,null,null,null,f1_0.cinemaHallId,f1_0.flavor,f1_0.name,f1_0.price,c1_0.name,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from CinemaBuff c1_0 left join PopcornStand f1_0 on f1_0.id=c1_0.favoritePopCornStandId
```

### partial selection - movie

* 1 Query
* 1 CinemaBuff JOIN Ticket JOIN Movie
* On `dataSize=2`:  1 Queries

```sql
2024-03-25T19:20:13.623+01:00 DEBUG 27348 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,null,null,null,null,null,null,null,null,null,c1_0.name,t1_0.id,null,null,null,null,null,null,null,null,null,null,t1_0.cinemaBuffId,t1_0.movieId,null,null,null,null,null,null,null,null,null,null,null,null,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title,t1_0.price,t1_0.seatNumber from CinemaBuff c1_0 left join Ticket t1_0 on c1_0.id=t1_0.cinemaBuffId left join Movie m1_0 on m1_0.id=t1_0.movieId
```

### partial selection - ticket

* 1 Query
* 1 CinemaBuff JOIN Ticket JOIN Movie JOIN CInemaHall JOIN PopcornStand
* On `dataSize=2`:  1 Queries

```sql
2024-03-25T19:20:13.662+01:00 DEBUG 27348 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,null,null,null,null,null,null,null,null,null,c1_0.name,t1_0.id,null,null,null,null,null,null,null,null,null,null,t1_0.cinemaBuffId,t1_0.movieId,m1_0.cinemaHallId,c2_0.capacity,c2_0.hallName,p1_0.id,null,null,null,null,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title,t1_0.price,t1_0.seatNumber from CinemaBuff c1_0 left join Ticket t1_0 on c1_0.id=t1_0.cinemaBuffId left join Movie m1_0 on m1_0.id=t1_0.movieId left join CinemaHall c2_0 on c2_0.id=m1_0.cinemaHallId left join PopcornStand p1_0 on c2_0.id=p1_0.cinemaHallId
```

### full selection

* 1 Query
* 1 CinemaBuff JOIN PopcornStand f1 JOIN Ticket JOIN Movie JOIN CInemaHall JOIN PopcornStand p1
* On `dataSize=2`:  1 Queries

 ## bee.persistent.jpa

### empty selection

* 3 Queries
* 1 CinemaBuff
* 1 CinemaBuff - PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* On `dataSize=2`:  5 Queries

```sql
2024-03-25T18:27:42.687+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name from CinemaBuff c1_0
2024-03-25T18:27:42.711+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T18:27:42.711+01:00 TRACE 26100 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T18:27:42.714+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T18:27:42.715+01:00 TRACE 26100 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
```

### partial selection - favoritePopcornStand

* 2 Queries
* 1 CinemaBuff JOIN PopcornStand
* 1 CinemaBuff - PopcornStand - CinemaHall
* On `dataSize=2`:  3 Queries (2*1 + 1)
  * 2*1 (dataSize * subquery) + 1 (mainquery)

```sql
2024-03-25T18:27:42.787+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,f1_0.id,f1_0.cinemaHallId,f1_0.flavor,f1_0.name,f1_0.price,c1_0.name from CinemaBuff c1_0 left join PopcornStand f1_0 on f1_0.id=c1_0.favoritePopCornStandId
2024-03-25T18:27:42.791+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T18:27:42.792+01:00 TRACE 26100 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
```

### partial selection - movie

* 4 Queries
* 1 CinemaBuff JOIN Ticket JOIN Movie
* 1 CinemaBuff - PopcornStand
* 2 CinemaBuff - Ticket - Movie - CinemaHall
* On `dataSize=2`:  7 Queries

```sql
2024-03-25T18:27:42.817+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name,t1_0.cinemaBuffId,t1_0.id,m1_0.id,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title,t1_0.movieId,t1_0.price,t1_0.seatNumber from CinemaBuff c1_0 left join Ticket t1_0 on c1_0.id=t1_0.cinemaBuffId left join Movie m1_0 on m1_0.id=t1_0.movieId
2024-03-25T18:27:42.823+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select p1_0.id,p1_0.cinemaHallId,p1_0.flavor,p1_0.name,p1_0.price from PopcornStand p1_0 where p1_0.id=?
2024-03-25T18:27:42.824+01:00 TRACE 26100 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T18:27:42.826+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T18:27:42.827+01:00 TRACE 26100 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [1]
2024-03-25T18:27:42.831+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.capacity,c1_0.hallName from CinemaHall c1_0 where c1_0.id=?
2024-03-25T18:27:42.832+01:00 TRACE 26100 --- [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter [1] as [BIGINT] - [2]
```

### partial selection - ticket

* 1 Query
* 1 CinemaBuff JOIN Ticket JOIN Movie JOIN CinemaHall JOIN PopcornStand
* On `dataSize=2`:  1 Query

```sql
2024-03-25T18:27:42.848+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,c1_0.name,t1_0.cinemaBuffId,t1_0.id,m1_0.id,c2_0.id,c2_0.capacity,c2_0.hallName,p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title,t1_0.movieId,t1_0.price,t1_0.seatNumber from CinemaBuff c1_0 left join Ticket t1_0 on c1_0.id=t1_0.cinemaBuffId left join Movie m1_0 on m1_0.id=t1_0.movieId left join CinemaHall c2_0 on c2_0.id=m1_0.cinemaHallId left join PopcornStand p1_0 on c2_0.id=p1_0.cinemaHallId
```

### full selection

* 1 Query
* 1 CinemaBuff JOIN PopcornStand f1 JOIN Ticket JOIN Movie JOIN CinemaHall JOIN PopcornStand p1
* On `dataSize=2`:  1 Query

```sql
2024-03-25T18:27:42.868+01:00 DEBUG 26100 --- [    Test worker] org.hibernate.SQL                        : select c1_0.id,c1_0.favoriteGenre,c1_0.favoritePopCornStandId,f1_0.id,f1_0.cinemaHallId,f1_0.flavor,f1_0.name,f1_0.price,c1_0.name,t1_0.cinemaBuffId,t1_0.id,m1_0.id,c3_0.id,c3_0.capacity,c3_0.hallName,p1_0.cinemaHallId,p1_0.id,p1_0.flavor,p1_0.name,p1_0.price,m1_0.cinemaHallId,m1_0.director,m1_0.durationInMinutes,m1_0.genre,m1_0.title,t1_0.movieId,t1_0.price,t1_0.seatNumber from CinemaBuff c1_0 left join PopcornStand f1_0 on f1_0.id=c1_0.favoritePopCornStandId left join Ticket t1_0 on c1_0.id=t1_0.cinemaBuffId left join Movie m1_0 on m1_0.id=t1_0.movieId left join CinemaHall c3_0 on c3_0.id=m1_0.cinemaHallId left join PopcornStand p1_0 on c3_0.id=p1_0.cinemaHallId
```



## jpa.eager

### all selection

* 4 Queries
* 1 CinemaBuff - PopcornStand JOIN CinemaHall
* 1 CinemaBuff - PopcornStand - CinemaHall - PopcornStand
* 1 CinemaBuff - Ticket JOIN Movie JOIN CinemaHall JOIN PopcornStand
* On `dataSize=2`:  7 Queries (2*3+1)

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

