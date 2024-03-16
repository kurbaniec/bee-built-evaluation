package test.bee.persistent.jpa

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["test.bee.persistent.jpa"],
    entityManagerFactoryRef = "testEM",
    transactionManagerRef = "testTM"
)
class DbConfig(val env: Environment) {
    @Bean(name = ["testDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource-test")
    fun testDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["testEM"])
    fun testtEntityManager(@Qualifier("testDataSource") dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.setDataSource(dataSource)
        em.setPackagesToScan("test.bee.persistent.jpa")
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties: HashMap<String, Any> = HashMap()
        properties["hibernate.hbm2ddl.auto"] = requireNotNull(env.getProperty("spring.jpa.hibernate.ddl-auto"))
        properties["hibernate.dialect"] = requireNotNull(env.getProperty("spring.jpa.properties.hibernate.dialect"))
        em.setJpaPropertyMap(properties)
        return em
    }

    @Bean(name = ["testTM"])
    fun testTransactionManager(@Qualifier("testEM") bEntityManager: AbstractEntityManagerFactoryBean): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = bEntityManager.getObject()
        return transactionManager
    }
}
