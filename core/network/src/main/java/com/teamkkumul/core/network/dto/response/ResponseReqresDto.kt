package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseReqresDto(
    @SerialName("data")
    val data: List<Data>,
    @SerialName("page")
    val page: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("support")
    val support: Support,
    @SerialName("total")
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int,
) {
    @Serializable
    data class Data(
        @SerialName("avatar")
        val avatar: String,
        @SerialName("email")
        val email: String,
        @SerialName("first_name")
        val firstName: String,
        @SerialName("id")
        val id: Int,
        @SerialName("last_name")
        val lastName: String,
    )

    @Serializable
    data class Support(
        @SerialName("text")
        val text: String,
        @SerialName("url")
        val url: String,
    )
}
