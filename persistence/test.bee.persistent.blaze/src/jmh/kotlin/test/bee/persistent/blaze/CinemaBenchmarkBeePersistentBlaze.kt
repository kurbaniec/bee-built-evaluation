package test.bee.persistent.blaze

import common.benchmark.BasePersistenceBenchmark
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.TestPropertySourceUtils

// @ExtendWith(SpringExtension::class)
// @SpringBootTest(classes = [TestConfig::class])
// @TestPropertySource("classpath:application-pg-bench.properties")
open class CinemaBenchmarkBeePersistentBlaze : BasePersistenceBenchmark() {
    override val path: String = "../reports/bee.persistent.blaze"
    final override val dataSize: Int = System.getProperty("dataSize").toInt()
    final override val config: List<Class<*>> = listOf(TestConfig::class.java)
    final override val propertySource: List<String> = listOf(
        "classpath:application.properties",
        "classpath:application-pg-bench.properties"
    )

    init {
        CONFIG = config
        PROPERTY_SOURCE = propertySource
        DATA_SIZE = dataSize
    }

    //
    // override var context: AnnotationConfigApplicationContext = buildContext()
    //
    // private final fun buildContext(): AnnotationConfigApplicationContext {
    //     // Create a new application context
    //     context = AnnotationConfigApplicationContext()
    //
    //
    //     // Register your configuration class
    //     context.register(TestConfig::class.java)
    //
    //     // Add test properties
    //     TestPropertySourceUtils.addPropertiesFilesToEnvironment(context, "classpath:application.properties")
    //     TestPropertySourceUtils.addPropertiesFilesToEnvironment(context, "classpath:application-pg-bench.properties")
    //     TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, "spring.datasource.url=jdbc:h2:mem:testdb")
    //
    //
    //
    //     // Refresh the context to apply changes
    //     context.refresh()
    //
    //     val myService = context.getBean(CinemaBuffRepository::class.java)
    //
    //     return context
    // }
}