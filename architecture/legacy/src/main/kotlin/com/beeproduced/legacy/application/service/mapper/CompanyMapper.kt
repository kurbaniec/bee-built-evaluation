package com.beeproduced.legacy.application.service.mapper

import com.beeproduced.legacy.application.dao.CompanyDao
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.Company
import com.beeproduced.legacy.application.model.input.CreateCompanyInput
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
class CompanyMapper(
    private val addressMapper: AddressMapper,
    private val companyMemberMapper: CompanyMemberMapper
) {

    fun toDao(input: CreateCompanyInput, address: Address?): CompanyDao {
        return CompanyDao(
            id = null,
            name = input.name,
            addressId = address?.id,
            address = address?.let { addressMapper.toDao(it) },
            employees = null
        )
    }

    fun toModel(
        dao: CompanyDao,
        context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()
    ): Company {
        val employees = if (Hibernate.isInitialized(dao.employees) && dao.employees != null) {
            companyMemberMapper.toModel(requireNotNull(dao.employees), context)
        } else null
        val address = if (Hibernate.isInitialized(dao.address) && dao.address != null)
            addressMapper.toModel(requireNotNull(dao.address))
        else null
        return Company(
            id = requireNotNull(dao.id),
            name = dao.name,
            employees = employees,
            addressId = dao.addressId,
            address = address
        )
    }
}