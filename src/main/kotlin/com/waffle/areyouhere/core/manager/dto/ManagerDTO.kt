package com.waffle.areyouhere.core.manager.dto

import com.waffle.areyouhere.core.manager.model.Manager

data class ManagerDTO(
    val id: Long,
    val email: String,
    val name: String,
    val password: String,
) {
    constructor(manager: Manager) : this(
        id = manager.id!!,
        email = manager.email,
        name = manager.email,
        password = manager.password,
    )
}
