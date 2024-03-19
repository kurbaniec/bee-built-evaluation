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

Benchmarks results are stored on complete runs in the `reports` folder.