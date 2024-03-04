package com.beeproduced.legacy.application.dao

import com.beeproduced.legacy.application.model.AddressId
import com.beeproduced.legacy.application.model.CompanyId
import jakarta.persistence.*

@Entity
@Table(name = "companies")
class CompanyDao(
    @Id
    @GeneratedValue
    val id: CompanyId?,
    var name: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    var employees: MutableSet<CompanyMemberDao>?,
    @Column(name = "address_id")
    var addressId: AddressId?,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id", insertable = false, updatable = false)
    var address: AddressDao?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompanyDao) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (addressId != other.addressId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (addressId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Company(id=$id, name='$name', employees=${employees?.map { it.personId }}, address=$addressId, address=$address)"
    }
}