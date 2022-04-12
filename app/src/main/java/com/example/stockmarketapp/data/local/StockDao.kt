package com.example.stockmarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    @Query(
        """
            SELECT * FROM companylistingentity 
            WHERE LOWER(name) LIKE '%s' || LOWER(:query) || '%s' OR
            UPPER(:query) == symbol
        """
    )
    suspend fun searchCompanyListings(query: String): List<CompanyListingEntity>
}