package test.jpa.eager

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-12-14
 */
@SpringBootApplication(scanBasePackages = ["test.jpa.eager", "com.beeproduced"])
class Application

fun main(args: Array<String>) {
    val application = SpringApplication(Application::class.java)
    application.run(*args)
}
