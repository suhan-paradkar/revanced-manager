package app.revanced.manager

import android.app.Application

class Global {
    companion object {
        private const val websiteUrl = "https://revanced.app"
        const val githubUrl = "$websiteUrl/github"
        const val discordUrl = "$websiteUrl/discord"

        private const val ghOrg = "revanced"
        const val ghPatches = "$ghOrg/revanced-patches"
        const val ghPatcher = "$ghOrg/revanced-patcher"
        const val ghManager = "$ghOrg/revanced-manager"

        val app: Application
            get() = MainActivity.app
    }
}