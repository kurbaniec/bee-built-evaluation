package com.beeproduced.legacy.application.dao

import com.beeproduced.legacy.application.model.AddressId
import com.beeproduced.legacy.application.model.PersonId
import jakarta.persistence.*

@Entity
@Table(name = "persons")
class PersonDao(
    @Id
    @GeneratedValue
    val id: PersonId?,
    var firstname: String,
    var lastname: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    var memberOf: MutableSet<CompanyMemberDao>?,
    @Column(name = "address_id")
    var addressId: AddressId?,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id", insertable = false, updatable = false)
    var address: AddressDao?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PersonDao) return false

        if (id != other.id) return false
        if (firstname != other.firstname) return false
        if (lastname != other.lastname) return false
        if (addressId != other.addressId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstname.hashCode()
        result = 31 * result + lastname.hashCode()
        result = 31 * result + (addressId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Person(id=$id, firstname='$firstname', lastname='$lastname', memberOf=${memberOf?.map { it.companyId }}, addressId=$addressId, address=$address)"
    }
}