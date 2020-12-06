package com.example.wheretoeat.models

data class ResponseDS (
        val total_entries : Int,
        val per_page : Int,
        val current_page : Int,
        val restaurants : List<Restaurant>
)