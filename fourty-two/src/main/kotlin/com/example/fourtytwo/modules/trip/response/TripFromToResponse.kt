package com.example.fourtytwo.modules.trip.response


data class TripFromToResponse(
        /*    val fromToLocationList: List<String> = listOf("Hacıosman","Levent","Koç Batı","Beşiktaş","Kadıköy"),*/
        val hacıosman: String? = "Hacıosman",
        val levent: String? = "Levent",
        val kocbati: String? = "Koç Batı",
        val besiktas: String? = "Beşiktaş",
        val kadikoy: String? = "Kadıköy"

)