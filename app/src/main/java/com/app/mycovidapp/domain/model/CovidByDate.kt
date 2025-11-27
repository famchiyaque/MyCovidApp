package com.app.mycovidapp.domain.model

data class CovidDateEntry (
        val country: String,
        val region: String,
        val cases: Case
        )

data class CovidByDate(
  val data: List<CovidDateEntry>
)