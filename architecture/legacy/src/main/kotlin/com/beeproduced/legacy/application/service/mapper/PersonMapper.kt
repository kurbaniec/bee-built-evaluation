package com.beeproduced.legacy.application.service.mapper

import com.beeproduced.legacy.application.dao.PersonDao
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.Person
import com.beeproduced.legacy.application.model.input.CreatePersonInput
import com.beeproduced.legacy.application.utils.CycleAvoidingMappingContext
import org.hibernate.Hibernate
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-04
 */
@Component
class PersonMapper(
    private val addressMapper: AddressMapper,
    private val companyMemberMapper: CompanyMemberMapper
) {

    fun toDao(input: CreatePersonInput, address: Address?): PersonDao {
        return PersonDao(
            id = null,
            firstname = input.firstname,
            lastname = input.lastname,
            addressId = address?.id,
            address = address?.let { addressMapper.toDao(it) },
            memberOf = null
        )
    }

    fun toModel(
        dao: PersonDao,
        context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()
    ): Person {
        val memberOf = if (Hibernate.isInitialized(dao.memberOf) && dao.memberOf != null) {
            companyMemberMapper.toModel(requireNotNull(dao.memberOf), context)
        } else null
        val address = if (Hibernate.isInitialized(dao.address) && dao.address != null)
            addressMapper.toModel(requireNotNull(dao.address))
        else null

        return Person(
            id = requireNotNull(dao.id),
            firstname = dao.firstname,
            lastname = dao.lastname,
            memberOf = memberOf,
            addressId = dao.addressId,
            address = address
        )
    }
}







