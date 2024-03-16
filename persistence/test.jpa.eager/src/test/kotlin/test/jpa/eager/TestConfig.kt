package test.jpa.eager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@SpringBootApplication(
    scanBasePackages = ["test.jpa.eager", "com.beeproduced"]
)
@EnableConfigurationProperties
class TestConfig