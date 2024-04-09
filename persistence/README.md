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

Benchmarks were performed on Windows 11-based laptop with
a mobile six-core processor (Intel i7-8750H @ 2.20 GHz, turbo mode disabled), 16 GB DDR4
RAM and NVMe SSD.

### bee.persistent.blaze

#### Data Size 125

```bash
Benchmark                                                                  Mode  Cnt    Score   Error  Units
CinemaBenchmarkBeePersistentBlaze.b1EmptySelection                        thrpt    5  147.541 ± 4.014  ops/s
CinemaBenchmarkBeePersistentBlaze.b2PartialSelectionFavoritePopcornStand  thrpt    5  140.089 ± 2.192  ops/s
CinemaBenchmarkBeePersistentBlaze.b3PartialSelectionMovie                 thrpt    5   87.408 ± 6.843  ops/s
CinemaBenchmarkBeePersistentBlaze.b4PartialSelectionTickets               thrpt    5   53.083 ± 2.957  ops/s
CinemaBenchmarkBeePersistentBlaze.b5FullSelection                         thrpt    5   49.516 ± 1.171  ops/s
```

#### Data Size 250

```bash
Benchmark                                                                  Mode  Cnt   Score   Error  Units
CinemaBenchmarkBeePersistentBlaze.b1EmptySelection                        thrpt    5  99.495 ± 4.201  ops/s
CinemaBenchmarkBeePersistentBlaze.b2PartialSelectionFavoritePopcornStand  thrpt    5  94.345 ± 1.472  ops/s
CinemaBenchmarkBeePersistentBlaze.b3PartialSelectionMovie                 thrpt    5  52.334 ± 0.653  ops/s
CinemaBenchmarkBeePersistentBlaze.b4PartialSelectionTickets               thrpt    5  29.855 ± 0.690  ops/s
CinemaBenchmarkBeePersistentBlaze.b5FullSelection                         thrpt    5  27.686 ± 0.437  ops/s
```

#### Data Size 500

```bash
Benchmark                                                                  Mode  Cnt   Score   Error  Units
CinemaBenchmarkBeePersistentBlaze.b1EmptySelection                        thrpt    5  61.977 ± 1.469  ops/s
CinemaBenchmarkBeePersistentBlaze.b2PartialSelectionFavoritePopcornStand  thrpt    5  59.790 ± 1.108  ops/s
CinemaBenchmarkBeePersistentBlaze.b3PartialSelectionMovie                 thrpt    5  30.395 ± 2.976  ops/s
CinemaBenchmarkBeePersistentBlaze.b4PartialSelectionTickets               thrpt    5  17.003 ± 0.573  ops/s
CinemaBenchmarkBeePersistentBlaze.b5FullSelection                         thrpt    5  15.167 ± 3.794  ops/s
```

### bee.persistent.jpa

#### Data Size 125

```bash
Benchmark                                                                Mode  Cnt   Score   Error  Units
CinemaBenchmarkBeePersistentJpa.b1EmptySelection                        thrpt    5   2.655 ± 0.076  ops/s
CinemaBenchmarkBeePersistentJpa.b2PartialSelectionFavoritePopcornStand  thrpt    5   5.194 ± 0.107  ops/s
CinemaBenchmarkBeePersistentJpa.b3PartialSelectionMovie                 thrpt    5   1.723 ± 0.053  ops/s
CinemaBenchmarkBeePersistentJpa.b4PartialSelectionTickets               thrpt    5  54.987 ± 1.233  ops/s
CinemaBenchmarkBeePersistentJpa.b5FullSelection                         thrpt    5  49.383 ± 2.044  ops/s
```

#### Data Size 250

```bash
Benchmark                                                                Mode  Cnt   Score   Error  Units
CinemaBenchmarkBeePersistentJpa.b1EmptySelection                        thrpt    5   1.329 ± 0.047  ops/s
CinemaBenchmarkBeePersistentJpa.b2PartialSelectionFavoritePopcornStand  thrpt    5   2.601 ± 0.059  ops/s
CinemaBenchmarkBeePersistentJpa.b3PartialSelectionMovie                 thrpt    5   0.879 ± 0.036  ops/s
CinemaBenchmarkBeePersistentJpa.b4PartialSelectionTickets               thrpt    5  31.912 ± 0.556  ops/s
CinemaBenchmarkBeePersistentJpa.b5FullSelection                         thrpt    5  28.351 ± 0.681  ops/s
```

#### Data Size 500

```bash
Benchmark                                                                Mode  Cnt   Score   Error  Units
CinemaBenchmarkBeePersistentJpa.b1EmptySelection                        thrpt    5   0.675 ± 0.010  ops/s
CinemaBenchmarkBeePersistentJpa.b2PartialSelectionFavoritePopcornStand  thrpt    5   1.316 ± 0.046  ops/s
CinemaBenchmarkBeePersistentJpa.b3PartialSelectionMovie                 thrpt    5   0.430 ± 0.013  ops/s
CinemaBenchmarkBeePersistentJpa.b4PartialSelectionTickets               thrpt    5  16.942 ± 0.812  ops/s
CinemaBenchmarkBeePersistentJpa.b5FullSelection                         thrpt    5  15.004 ± 0.452  ops/s
```

### Spring Data JPA (lazy)

#### Data Size 125

```bash
Benchmark                                                       Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaLazy.b1EmptySelection                        thrpt    5  2.691 ± 0.078  ops/s
CinemaBenchmarkJpaLazy.b2PartialSelectionFavoritePopcornStand  thrpt    5  2.690 ± 0.056  ops/s
CinemaBenchmarkJpaLazy.b3PartialSelectionMovie                 thrpt    5  0.896 ± 0.025  ops/s
CinemaBenchmarkJpaLazy.b4PartialSelectionTickets               thrpt    5  0.669 ± 0.029  ops/s
CinemaBenchmarkJpaLazy.b5FullSelection                         thrpt    5  0.669 ± 0.014  ops/s
```

#### Data Size 250

```bash
Benchmark                                                       Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaLazy.b1EmptySelection                        thrpt    5  1.337 ± 0.051  ops/s
CinemaBenchmarkJpaLazy.b2PartialSelectionFavoritePopcornStand  thrpt    5  1.327 ± 0.080  ops/s
CinemaBenchmarkJpaLazy.b3PartialSelectionMovie                 thrpt    5  0.435 ± 0.011  ops/s
CinemaBenchmarkJpaLazy.b4PartialSelectionTickets               thrpt    5  0.322 ± 0.008  ops/s
CinemaBenchmarkJpaLazy.b5FullSelection                         thrpt    5  0.321 ± 0.010  ops/s
```

#### Data Size 500

```bash
Benchmark                                                       Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaLazy.b1EmptySelection                        thrpt    5  0.664 ± 0.022  ops/s
CinemaBenchmarkJpaLazy.b2PartialSelectionFavoritePopcornStand  thrpt    5  0.659 ± 0.013  ops/s
CinemaBenchmarkJpaLazy.b3PartialSelectionMovie                 thrpt    5  0.217 ± 0.005  ops/s
CinemaBenchmarkJpaLazy.b4PartialSelectionTickets               thrpt    5  0.159 ± 0.005  ops/s
CinemaBenchmarkJpaLazy.b5FullSelection                         thrpt    5  0.160 ± 0.005  ops/s
```

### Spring Data JPA (eager)

#### Data Size 125

```bash
Benchmark                                                        Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaEager.b1EmptySelection                        thrpt    5  1.450 ± 0.051  ops/s
CinemaBenchmarkJpaEager.b2PartialSelectionFavoritePopcornStand  thrpt    5  1.464 ± 0.048  ops/s
CinemaBenchmarkJpaEager.b3PartialSelectionMovie                 thrpt    5  1.457 ± 0.093  ops/s
CinemaBenchmarkJpaEager.b4PartialSelectionTickets               thrpt    5  1.460 ± 0.042  ops/s
CinemaBenchmarkJpaEager.b5FullSelection                         thrpt    5  1.467 ± 0.016  ops/s
```

#### Data Size 250

```bash
Benchmark                                                        Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaEager.b1EmptySelection                        thrpt    5  0.718 ± 0.018  ops/s
CinemaBenchmarkJpaEager.b2PartialSelectionFavoritePopcornStand  thrpt    5  0.717 ± 0.019  ops/s
CinemaBenchmarkJpaEager.b3PartialSelectionMovie                 thrpt    5  0.716 ± 0.013  ops/s
CinemaBenchmarkJpaEager.b4PartialSelectionTickets               thrpt    5  0.717 ± 0.007  ops/s
CinemaBenchmarkJpaEager.b5FullSelection                         thrpt    5  0.717 ± 0.023  ops/s
```

#### Data Size 500

```bash
Benchmark                                                        Mode  Cnt  Score   Error  Units
CinemaBenchmarkJpaEager.b1EmptySelection                        thrpt    5  0.345 ± 0.009  ops/s
CinemaBenchmarkJpaEager.b2PartialSelectionFavoritePopcornStand  thrpt    5  0.343 ± 0.026  ops/s
CinemaBenchmarkJpaEager.b3PartialSelectionMovie                 thrpt    5  0.346 ± 0.009  ops/s
CinemaBenchmarkJpaEager.b4PartialSelectionTickets               thrpt    5  0.347 ± 0.011  ops/s
CinemaBenchmarkJpaEager.b5FullSelection                         thrpt    5  0.351 ± 0.002  ops/s
```

