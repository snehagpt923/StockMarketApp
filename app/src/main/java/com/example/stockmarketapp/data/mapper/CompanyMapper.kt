package com.example.stockmarketapp.data.mapper

import com.example.stockmarketapp.data.local.CompanyListingEntity
import com.example.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() =
    CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )

fun CompanyListing.toCompanyListingEntity() =
    CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )