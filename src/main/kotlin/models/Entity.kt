package models

import actions.Action

data class Entity(
    val title: String,
    val action: Action? = null
)