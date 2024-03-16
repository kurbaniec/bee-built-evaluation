package test.bee.persistent.jpa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@SpringBootApplication(
    scanBasePackages = ["test.bee.persistent.jpa", "com.beeproduced"]
)
@EnableConfigurationProperties
class TestConfig