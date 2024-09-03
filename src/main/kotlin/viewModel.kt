@file:Suppress("unused", "FunctionName")

import androidx.compose.runtime.Composable

class HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    AppBar(viewModel)
    ContactListScreen(viewModel)
    BottomBar(viewModel)
}

@Composable
fun AppBar(viewModel: HomeViewModel) {

}

@Composable
fun ContactListScreen(viewModel: HomeViewModel) {

}

@Composable
fun BottomBar(viewModel: HomeViewModel) {

}