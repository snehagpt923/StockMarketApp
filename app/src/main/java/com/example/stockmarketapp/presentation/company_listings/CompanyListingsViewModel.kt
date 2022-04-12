package com.example.stockmarketapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.presentation.company_listings.CompanyListingsEvent.OnSearchQueryChange
import com.example.stockmarketapp.presentation.company_listings.CompanyListingsEvent.Refresh
import com.example.stockmarketapp.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyListingsState())
    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            is Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getCompanyListings(fetchFromRemote, query)
                .collect { result ->
                    when (result) {
                        is Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        is Error -> {
                            result.message?.let {
                                state = state.copy(message = it)
                            }
                        }
                        is Success -> {
                            result.data?.let {
                                state = state.copy(companies = it)
                            }
                        }
                    }
                }
        }
    }
}