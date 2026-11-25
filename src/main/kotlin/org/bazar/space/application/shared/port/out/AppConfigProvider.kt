package org.bazar.space.application.shared.port.out

interface AppConfigProvider {
    val batchSize: Int

    /** app.join-space.base-join-url: invite link is built as `{baseJoinUrl}/{joinCode}` */
    val baseJoinUrl: String
}
