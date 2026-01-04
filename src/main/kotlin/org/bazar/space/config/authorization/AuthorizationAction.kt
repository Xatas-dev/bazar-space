package org.bazar.space.config.authorization

enum class AuthorizationAction(val actionName: String) {
    ADD_USER_TO_SPACE("add_user_to_space"),
    REMOVE_USER_FROM_SPACE("remove_user_from_space"),
    DELETE_SPACE("delete_space"),
    ACCESS_CHAT("access_chat"),
    READ_SPACE("read_space"),
    ACCESS_STORAGE("access_storage");
}