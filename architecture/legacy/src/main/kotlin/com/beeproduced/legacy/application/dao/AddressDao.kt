package com.beeproduced.legacy.application.dao

import com.beeproduced.legacy.application.model.AddressId
import jakarta.persistence.*

@Entity
@Table(name = "addresses")
class AddressDao(
    @Id
    @GeneratedValue
    val id: AddressId?,
    @Column(name = "address_line1")
    val addressLine1: String,
    @Column(name = "address_line2", nullable = true)
    val addressLine2: String?,
    @Column(name = "zip_code")
    val zipCode: String,
    val city: String
)