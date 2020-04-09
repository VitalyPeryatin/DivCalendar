package com.infinity_coder.divcalendar.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

class PortfolioWithSecurities {
    @Embedded
    lateinit var portfolio: PortfolioDbModel

    @Relation(
        parentColumn = PortfolioDbModel.COLUMN_NAME,
        entity = SecurityPackageDbModel::class,
        entityColumn = SecurityPackageDbModel.COLUMN_PORTFOLIO_NAME
    )
    var securities: List<SecurityPackageDbModel> = emptyList()
}