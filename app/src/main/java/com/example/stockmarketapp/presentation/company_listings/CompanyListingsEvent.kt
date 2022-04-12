package com.example.stockmarketapp.presentation.company_listings

sealed class CompanyListingsEvent {
    object Refresh : CompanyListingsEvent()
    class OnSearchQueryChange(val query: String) : CompanyListingsEvent()
}
