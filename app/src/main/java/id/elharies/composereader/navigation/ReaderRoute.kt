package id.elharies.composereader.navigation

sealed class ReaderRoute(val nameScreen: String) {
    data object Splash: ReaderRoute("splash")
    data object Login: ReaderRoute("login")
    data object Home: ReaderRoute("home")
    data object Stats: ReaderRoute("stats")
    data object Update: ReaderRoute("update")
    data object Detail: ReaderRoute("detail")
    data object Search: ReaderRoute("search")
}