package at.fhhagenberg.me.ada.speedrunmobile.network

object SpeedrunProxyFactory {
    fun createProxy() : SpeedrunProxy {
        return SpeedrunProxyImpl()
    }
}