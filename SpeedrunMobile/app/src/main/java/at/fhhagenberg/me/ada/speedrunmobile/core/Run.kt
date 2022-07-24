package at.fhhagenberg.me.ada.speedrunmobile.core

data class Run(
    var id: String?,
    var place: Int?,
    var comment: String?,
    var videos: List<String?>?,
    var runners: List<String?>?,
    var submitted: String?,
    var times: Any?,
    var actualRunner: Runner? = null
)
