package com.beeproduced.legacy.application.service.mapper

import com.beeproduced.legacy.application.dao.*
import com.beeproduced.legacy.application.model.*
import com.beeproduced.legacy.application.model.input.CreateAddressInput
import com.beeproduced.legacy.application.model.input.CreateCompanyInput
import com.beeproduced.legacy.application.model.input.CreateFilmInput
import com.beeproduced.legacy.application.model.input.CreatePersonInput
import com.beeproduced.legacy.application.utils.CycleAvoidingMappingContext
import org.hibernate.Hibernate
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.Instant

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

@Component
class CompanyMemberMapper(
    @Lazy private val personMapper: PersonMapper,
    @Lazy private val companyMapper: CompanyMapper
) {

    fun toModel(
        daos: Collection<CompanyMemberDao>,
        context: CycleAvoidingMappingContext
    ): Set<CompanyMember> {
        if (daos.isEmpty()) return emptySet()
        if (daos.any {
            context.hasVisited(CompanyMember::class.java, Pair(it.companyId, it.personId))
        }) return emptySet()
        for (dao in daos)
            context.addVisited(CompanyMember::class.java, Pair(dao.companyId, dao.personId))
        return daos.mapTo(HashSet()) { toModel(it, context) }
    }

    fun toModel(
        dao: CompanyMemberDao,
        context: CycleAvoidingMappingContext
    ): CompanyMember {
        val person = if (Hibernate.isInitialized(dao.person) && dao.person != null)
            personMapper.toModel(requireNotNull(dao.person), context)
        else null
        val company = if (Hibernate.isInitialized(dao.company) && dao.company != null)
            companyMapper.toModel(requireNotNull(dao.company), context)
        else null

        return CompanyMember(
            companyId = dao.companyId,
            personId = dao.personId,
            company = company,
            person = person
        )
    }
}

@Component
class FilmMapper {

    fun toDao(input: CreateFilmInput): FilmDao {
        return FilmDao(
            id = null,
            title = input.title,
            year = input.year,
            synopsis = input.synopsis,
            runtime = input.runtime,
            studioIds = input.studios.toMutableSet(),
            directorIds = input.directors.toMutableSet(),
            castIds = input.cast.toMutableSet(),
            addedOn = Instant.now()
        )
    }

    fun toModel(dao: FilmDao): Film {
        return Film(
            id = requireNotNull(dao.id),
            title = dao.title,
            year = dao.year,
            synopsis = dao.synopsis,
            runtime = dao.runtime,
            studioIds = dao.studioIds,
            directorIds = dao.directorIds,
            castIds = dao.castIds,
            addedOn = dao.addedOn
        )
    }
}