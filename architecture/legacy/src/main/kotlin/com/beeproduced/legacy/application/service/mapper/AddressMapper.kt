package com.beeproduced.legacy.application.service.mapper

import com.beeproduced.legacy.application.dao.AddressDao
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.input.CreateAddressInput
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-04
 */

@Component
class AddressMapper {
    fun toDao(input: CreateAddressInput): AddressDao {
        return AddressDao(
            id = null,
            addressLine1 = input.addressLine1,
            addressLine2 = input.addressLine2,
            zipCode = input.zipCode,
            city = input.city
        )
    }

    fun toDao(input: Address): AddressDao {
        return AddressDao(
            id = input.id,
            addressLine1 = input.addressLine1,
            addressLine2 = input.addressLine2,
            zipCode = input.zipCode,
            city = input.city
        )
    }

    fun toModel(dao: AddressDao): Address {
        return Address(
            id = requireNotNull(dao.id),
            addressLine1 = dao.addressLine1,
            addressLine2 = dao.addressLine2,
            zipCode = dao.zipCode,
            city = dao.city
        )
    }
}