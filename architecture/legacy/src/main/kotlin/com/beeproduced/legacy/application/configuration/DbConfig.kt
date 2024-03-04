package com.beeproduced.legacy.application.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

@EnableJpaRepositories(
    basePackages = ["com.beeproduced.legacy.application"],
    entityManagerFactoryRef = "appEntityManager",
    transactionManagerRef = "appTransactionManager"
)
class DbConfig(val env: Environment) {

    @Bean(name = ["appDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource-app")
    fun usermanagementDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["appEntityManager"])
    fun userEntityManager(@Qualifier("appDataSource") dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.setDataSource(dataSource)
        em.setPackagesToScan("com.beeproduced.legacy.application")
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties: HashMap<String, Any> = HashMap()
        properties["hibernate.hbm2ddl.auto"] = requireNotNull(env.getProperty("spring.jpa.hibernate.ddl-auto"))
        properties["hibernate.dialect"] = requireNotNull(env.getProperty("spring.jpa.properties.hibernate.dialect"))
        em.setJpaPropertyMap(properties)
        return em
    }

    @Bean(name = ["appTransactionManager"])
    fun userTransactionManager(@Qualifier("appEntityManager") userEntityManager: AbstractEntityManagerFactoryBean): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = userEntityManager.getObject()
        return transactionManager
    }
}