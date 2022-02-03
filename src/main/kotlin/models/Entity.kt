package models

import Actions.Action

data class Entity(
    val title: String,
    val action: Action? = null
)