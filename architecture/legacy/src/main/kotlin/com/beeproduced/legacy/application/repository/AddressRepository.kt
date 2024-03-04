package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.jpa.repository.BaseDataRepository
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.AddressId
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Component
class AddressRepository(
    @Qualifier("organisationEntityManager") em: EntityManager
) : BaseDataRepository<Address, AddressId>(em)