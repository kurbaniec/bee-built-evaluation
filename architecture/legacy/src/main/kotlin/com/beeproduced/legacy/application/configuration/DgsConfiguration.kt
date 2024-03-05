package com.beeproduced.legacy.application.configuration

import com.beeproduced.bee.functional.dgs.result.fetcher.implementation.aspect.ResultFetcherAspectConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */
@Configuration
@EnableAspectJAutoProxy
class DgsConfiguration : ResultFetcherAspectConfiguration()