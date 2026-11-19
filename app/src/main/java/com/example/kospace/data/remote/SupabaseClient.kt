package com.example.kospace.data.remote


import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://mqibruwzjfwrwmjbhdsf.supabase.co",
        supabaseKey = "sb_publishable_Rqb-Gl6qJHMPm24msbOHXQ_JklnZbrl"
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
    }
}
