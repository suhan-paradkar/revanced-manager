package app.revanced.manager.ui.data

@Suppress("ArrayInDataClass")
@kotlinx.serialization.Serializable
data class PatcherSubscreenData(
    val packageId: String? = null,
    val patches: Array<String>? = null
)
