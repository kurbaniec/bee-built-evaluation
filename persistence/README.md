# Persistence Evaluation

Benchmarking following implementations via JMH

* `bee.persistent.blaze`
* `bee.persistent.jpa`
* JPA (lazy)
* JPA (eager)

The JPA benchmarks use Spring Data and the Unproxy method of `bee.persistent.jpa` to mitigate `LazyInitializationExceptions`. Lazy benchmarks also load request data via `Hibernate.initialize`.

---

Tests and Benchmarks can be run via the `runTests` & `runBenchmarks` scripts.

> ⚠️ Shell scripts are not yet tested and could be faulty!

Benchmarks results are stored on complete runs in the `reports` folder.

## Measures

Preliminary results with data size of 500 (root `CinemaBuff` entities)

```
Benchmark                                                                  Mode  Cnt    Score    Error  Units
CinemaBenchmarkBeePersistentBlaze.b1EmptySelection                        thrpt    5  137,712 ± 11,703  ops/s
CinemaBenchmarkBeePersistentBlaze.b2PartialSelectionFavoritePopcornStand  thrpt    5  111,542 ± 64,445  ops/s
CinemaBenchmarkBeePersistentBlaze.b3PartialSelectionMovie                 thrpt    5   51,828 ±  2,251  ops/s
CinemaBenchmarkBeePersistentBlaze.b4PartialSelectionTickets               thrpt    5   26,475 ±  1,755  ops/s
CinemaBenchmarkBeePersistentBlaze.b5FullSelection                         thrpt    5   24,468 ±  2,525  ops/s
```

```
Benchmark                                                                Mode  Cnt   Score   Error  Units
CinemaBenchmarkBeePersistentJpa.b1EmptySelection                        thrpt    5   0,877 ± 0,191  ops/s
CinemaBenchmarkBeePersistentJpa.b2PartialSelectionFavoritePopcornStand  thrpt    5   1,755 ± 0,169  ops/s
CinemaBenchmarkBeePersistentJpa.b3PartialSelectionMovie                 thrpt    5   0,598 ± 0,026  ops/s
CinemaBenchmarkBeePersistentJpa.b4PartialSelectionTickets               thrpt    5  25,817 ± 5,832  ops/s
CinemaBenchmarkBeePersistentJpa.b5FullSelection                         thrpt    5  22,882 ± 6,805  ops/s
```

```
Benchmark                                                        Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaEager.b1EmptySelection                        thrpt    5  0,431 ± 0,567  ops/s
CinemaBenchmarkJpaEager.b2PartialSelectionFavoritePopcornStand  thrpt    5  0,583 ± 0,040  ops/s
CinemaBenchmarkJpaEager.b3PartialSelectionMovie                 thrpt    5  0,565 ± 0,125  ops/s
CinemaBenchmarkJpaEager.b4PartialSelectionTickets               thrpt    5  0,582 ± 0,064  ops/s
CinemaBenchmarkJpaEager.b5FullSelection                         thrpt    5  0,546 ± 0,213  ops/s
```

```
Benchmark                                                       Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaLazy.b1EmptySelection                        thrpt    5  1,126 ± 0,084  ops/s
CinemaBenchmarkJpaLazy.b2PartialSelectionFavoritePopcornStand  thrpt    5  0,944 ± 0,714  ops/s
CinemaBenchmarkJpaLazy.b3PartialSelectionMovie                 thrpt    5  0,342 ± 0,099  ops/s
CinemaBenchmarkJpaLazy.b4PartialSelectionTickets               thrpt    5  0,214 ± 0,018  ops/s
CinemaBenchmarkJpaLazy.b5FullSelection                         thrpt    5  0,215 ± 0,021  ops/s
```

