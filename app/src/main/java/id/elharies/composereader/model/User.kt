package id.elharies.composereader.model

import id.elharies.composereader.utils.constans.KeyColumn

data class User(
    var id: String? = null,
    var userId: String = "",
    var displayName: String = "",
    var avatarUrl: String = "",
    var quote: String = "",
    var profession: String = ""
)
