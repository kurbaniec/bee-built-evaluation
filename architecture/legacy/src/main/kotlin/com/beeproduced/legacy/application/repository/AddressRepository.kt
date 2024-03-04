package com.beeproduced.legacy.application.repository

import com.beeproduced.legacy.application.dao.AddressDao
import com.beeproduced.legacy.application.model.AddressId
import org.springframework.data.jpa.repository.JpaRepository

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

interface AddressRepository : JpaRepository<AddressDao, AddressId>