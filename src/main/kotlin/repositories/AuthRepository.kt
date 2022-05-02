package repositories

import actionManager
import actions.Action
import data.delivering.OwnServerService
import models.User
import ui.PageType

class AuthRepository(val dataDeliveringService: OwnServerService) {
    var user: User = User.Companion.ANONYMOUS

    fun logIn(login: String, password: String) {
        if (dataDeliveringService.login(login, password)) {
            print("You've entered as admin")
            user = User.Companion.ADMIN
            actionManager.send(Action.UI.OPEN_PAGE(PageType.ADMIN))
        } else {
            print("Failed admin entry attempting.")
        }
    }
}