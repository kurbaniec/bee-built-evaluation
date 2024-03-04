package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.jpa.repository.BaseDataRepository
import com.beeproduced.legacy.application.dao.AddressDao
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.AddressId
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

interface AddressRepository : JpaRepository<AddressDao, AddressId>