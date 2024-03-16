package test.jpa.lazy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@SpringBootApplication(
    scanBasePackages = ["test.jpa.lazy", "com.beeproduced"]
)
@EnableConfigurationProperties
class TestConfig