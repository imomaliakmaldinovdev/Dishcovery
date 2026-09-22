package com.dishcovery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.model.UiLanguage
import com.dishcovery.app.presentation.AsyncUiState
import com.dishcovery.app.presentation.RecipeDetailViewModel
import com.dishcovery.app.presentation.RecipeListViewModel
import com.dishcovery.app.presentation.RecipeSearchViewModel
import com.dishcovery.app.presentation.SavedRecipesViewModel
import com.dishcovery.app.presentation.ProfileViewModel

private val Cream = Color(0xFFFFFBF6); private val Tomato = Color(0xFFC7493A); private val Sage = Color(0xFF788B58)
class MainActivity : ComponentActivity() { override fun onCreate(state: Bundle?) { super.onCreate(state); setContent { DishcoveryApp() } } }
private enum class Tab { HOME, EXPLORE, SAVED, PROFILE }
private fun label(tab: Tab, language: UiLanguage) = when (language) { UiLanguage.EN -> when(tab) { Tab.HOME -> "Home"; Tab.EXPLORE -> "Explore"; Tab.SAVED -> "Saved"; Tab.PROFILE -> "Profile" }; UiLanguage.UZ -> when(tab) { Tab.HOME -> "Bosh sahifa"; Tab.EXPLORE -> "Izlash"; Tab.SAVED -> "Saqlangan"; Tab.PROFILE -> "Profil" }; UiLanguage.RU -> when(tab) { Tab.HOME -> "Главная"; Tab.EXPLORE -> "Поиск"; Tab.SAVED -> "Сохранённое"; Tab.PROFILE -> "Профиль" } }
private fun ui(key: String, language: UiLanguage): String = when (language) {
    UiLanguage.EN -> when (key) {
        "discover" -> "Discover recipes"; "liveRecipes" -> "Live recipes from Oshxona"; "searchHint" -> "Search recipes or ingredients"; "fresh" -> "Fresh from Oshxona"; "live" -> "Live"; "explore" -> "Explore"; "exploreSubtitle" -> "Search 4,000+ recipes"; "searchTry" -> "Try “osh”, “tuxum”, “salat”…"; "allRecipes" -> "All recipes"; "videoRecipes" -> "Video recipes"; "savedTitle" -> "Saved"; "savedSubtitle" -> "Your recipe collection"; "settings" -> "Settings"; "language" -> "Language"; "chooseLanguage" -> "Choose language"; "languageDescription" -> "Change the Dishcovery interface language."; "cancel" -> "Cancel"; "back" -> "Back"; else -> key
    }
    UiLanguage.UZ -> when (key) {
        "discover" -> "Retseptlarni kashf eting"; "liveRecipes" -> "Oshxonadan jonli retseptlar"; "searchHint" -> "Retsept yoki masalliq qidiring"; "fresh" -> "Oshxonadan yangiliklar"; "live" -> "Jonli"; "explore" -> "Izlash"; "exploreSubtitle" -> "4 000 dan ortiq retsept qidiring"; "searchTry" -> "“osh”, “tuxum”, “salat”ni sinab ko‘ring…"; "allRecipes" -> "Barcha retseptlar"; "videoRecipes" -> "Video retseptlar"; "savedTitle" -> "Saqlangan"; "savedSubtitle" -> "Sizning retseptlar to‘plamingiz"; "settings" -> "Sozlamalar"; "language" -> "Til"; "chooseLanguage" -> "Tilni tanlang"; "languageDescription" -> "Dishcovery interfeysi tilini o‘zgartiring."; "cancel" -> "Bekor qilish"; "back" -> "Orqaga"; else -> key
    }
    UiLanguage.RU -> when (key) {
        "discover" -> "Откройте рецепты"; "liveRecipes" -> "Актуальные рецепты от Oshxona"; "searchHint" -> "Поиск рецептов или ингредиентов"; "fresh" -> "Новое от Oshxona"; "live" -> "Сейчас"; "explore" -> "Поиск"; "exploreSubtitle" -> "Поиск среди 4 000+ рецептов"; "searchTry" -> "Попробуйте «ош», «тухум», «салат»…"; "allRecipes" -> "Все рецепты"; "videoRecipes" -> "Видео-рецепты"; "savedTitle" -> "Сохранённое"; "savedSubtitle" -> "Ваша коллекция рецептов"; "settings" -> "Настройки"; "language" -> "Язык"; "chooseLanguage" -> "Выберите язык"; "languageDescription" -> "Измените язык интерфейса Dishcovery."; "cancel" -> "Отмена"; "back" -> "Назад"; else -> key
    }
}

@Composable private fun DishcoveryApp() {
    val appViewModel: AppViewModel = viewModel(); val language by appViewModel.language.collectAsStateWithLifecycle(); val saved by appViewModel.savedIds.collectAsStateWithLifecycle(); val nav = rememberNavController(); val current = nav.currentBackStackEntryAsState().value?.destination?.route ?: "home"; val isDetail = current.startsWith("detail")
    MaterialTheme { Scaffold(containerColor = Cream, bottomBar = { if (!isDetail) NavigationBar { Tab.entries.forEach { t -> val route = t.name.lowercase(); NavigationBarItem(selected = current == route, onClick = { nav.navigate(route) { popUpTo("home"); launchSingleTop = true } }, icon = { Icon(when(t) { Tab.HOME -> Icons.Outlined.Home; Tab.EXPLORE -> Icons.Outlined.Explore; Tab.SAVED -> Icons.Outlined.BookmarkBorder; Tab.PROFILE -> Icons.Outlined.PersonOutline }, label(t, language)) }, label = { Text(label(t, language)) }) } } }) { p -> NavHost(navController = nav, startDestination = "home") { composable("home") { Feed(p, language, { nav.navigate("explore") }) { nav.navigate("detail/$it") } }; composable("explore") { Search(p, language) { nav.navigate("detail/$it") } }; composable("saved") { Saved(p, language, saved) { nav.navigate("detail/$it") } }; composable("profile") { Profile(p, language, saved.size, appViewModel::setLanguage) }; composable("detail/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry -> Detail(entry.arguments?.getInt("id") ?: return@composable, p, language, (entry.arguments?.getInt("id") ?: -1) in saved, onBack = { nav.popBackStack() }) { id -> appViewModel.saveRecipe(id); nav.navigate("saved") { popUpTo("home") } } } } } }
}

@Composable private fun Feed(p: PaddingValues, language: UiLanguage, search: () -> Unit, open: (Int) -> Unit) {
    val viewModel: RecipeListViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.load() }
    LazyColumn(Modifier.fillMaxSize().padding(p).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(vertical = 24.dp)) {
        item { Text(ui("discover", language), fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(ui("liveRecipes", language), color = Color.Gray) }
        item { Box(Modifier.fillMaxWidth().clickable { search() }) { OutlinedTextField("", {}, Modifier.fillMaxWidth(), enabled = false, placeholder = { Text(ui("searchHint", language)) }, leadingIcon = { Icon(Icons.Outlined.Search, null) }, shape = RoundedCornerShape(16.dp)) } }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(ui("fresh", language), fontWeight = FontWeight.Bold, fontSize = 20.sp); Text(ui("live", language), color = Sage, fontWeight = FontWeight.Bold, fontSize = 13.sp) } }
        if (state is AsyncUiState.Loading || state is AsyncUiState.Idle) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        if (state is AsyncUiState.Error) item { Text((state as AsyncUiState.Error).message, color = Tomato) }
        val recipes = (state as? AsyncUiState.Success<List<Recipe>>)?.data.orEmpty()
        items(recipes, key = { it.id }) { RecipeCard(it, open) }
    }
}

@Composable private fun Search(p: PaddingValues, language: UiLanguage, open: (Int) -> Unit) {
    val viewModel: RecipeSearchViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }; var recent by remember { mutableStateOf(listOf<String>()) }; var videoOnly by remember { mutableStateOf(false) }
    LaunchedEffect(query) { if (query.length >= 2) { viewModel.search(query); recent = (listOf(query) + recent.filter { it != query }).take(4) } }
    val results = (state as? AsyncUiState.Success<List<Recipe>>)?.data.orEmpty()
    val loading = state is AsyncUiState.Loading
    val failed = state is AsyncUiState.Error
    LazyColumn(Modifier.fillMaxSize().padding(p).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(vertical = 24.dp)) {
        item { Text(ui("explore", language), fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(ui("exploreSubtitle", language), color = Color.Gray) }
        item { OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), placeholder = { Text(ui("searchTry", language)) }, leadingIcon = { Icon(Icons.Outlined.Search, null) }, shape = RoundedCornerShape(16.dp), singleLine = true) }
        item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { FilterChip(selected = !videoOnly, onClick = { videoOnly = false }, label = { Text(ui("allRecipes", language)) }); FilterChip(selected = videoOnly, onClick = { videoOnly = true }, label = { Text(ui("videoRecipes", language)) }) } }
        if(loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        if(query.isEmpty() && recent.isNotEmpty()) item { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Recent searches", fontWeight = FontWeight.Bold, fontSize = 19.sp); recent.forEach { term -> TextButton(onClick = { query = term }) { Icon(Icons.Outlined.History, null); Spacer(Modifier.width(8.dp)); Text(term) } } } }
        if(query.isEmpty() && recent.isEmpty()) item { EmptyPanel("Start exploring", "Search by dish or ingredient, such as osh, tuxum, or salat.", Icons.Outlined.Search) }
        if(query.length == 1) item { Text("Type at least 2 characters to search.", color = Color.Gray) }
        if(failed) item { EmptyPanel("Search is unavailable", "Check your internet connection and try again.", Icons.Outlined.Refresh) }
        if(query.length >= 2 && !loading && !failed && results.isEmpty()) item { EmptyPanel("No recipes found", "Try a shorter or different search term.", Icons.Outlined.SearchOff) }
        items(if(videoOnly) results.filter { it.hasVideo } else results, key = { it.id }) { RecipeCard(it, open) }
    }
}

@Composable private fun Saved(p: PaddingValues, language: UiLanguage, ids: Set<Int>, open: (Int) -> Unit) {
    val viewModel: SavedRecipesViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(ids) { viewModel.load(ids) }
    val recipes = (state as? AsyncUiState.Success<List<Recipe>>)?.data.orEmpty()
    val loading = state is AsyncUiState.Loading
    LazyColumn(Modifier.fillMaxSize().padding(p).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(vertical = 24.dp)) { item { Text(ui("savedTitle", language), fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(ui("savedSubtitle", language), color = Color.Gray) }; if(loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }; if(ids.isEmpty()) item { EmptyPanel("No saved recipes", "Open a recipe and tap Save to add it here.", Icons.Outlined.BookmarkBorder) }; items(recipes, key = { it.id }) { RecipeCard(it, open) } }
}

@Composable private fun Detail(id: Int, p: PaddingValues, language: UiLanguage, saved: Boolean, onBack: () -> Unit, onSave: (Int) -> Unit) {
    val viewModel: RecipeDetailViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(id) { viewModel.load(id) }
    val recipe = (state as? AsyncUiState.Success<Recipe>)?.data
    recipe?.let { r -> LazyColumn(Modifier.fillMaxSize().padding(p), contentPadding = PaddingValues(bottom = 24.dp)) { item { Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, ui("back", language)) }; Text("Recipe details", fontWeight = FontWeight.SemiBold) } }; item { AsyncImage(r.imageUrl, r.title, Modifier.fillMaxWidth().height(260.dp).background(Sage), contentScale = ContentScale.Crop) }; item { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { Text(r.title, fontSize = 29.sp, fontWeight = FontWeight.Bold); Surface(color = Color(0xFFFFE9E5), shape = RoundedCornerShape(20.dp)) { Text(r.category.replaceFirstChar { it.uppercase() }, Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Tomato, fontWeight = FontWeight.SemiBold, fontSize = 13.sp) }; if(r.description.isNotBlank()) Text(r.description, color = Color.DarkGray); Button({ onSave(id) }, Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = if(saved) Sage else Tomato), shape = RoundedCornerShape(16.dp)) { Icon(Icons.Outlined.BookmarkBorder, null); Spacer(Modifier.width(8.dp)); Text(if(saved) "Saved to your collection" else "Save recipe", fontWeight = FontWeight.Bold) }; Text("Ingredients", fontSize = 21.sp, fontWeight = FontWeight.Bold); Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { r.ingredients.forEach { Text("• ${listOfNotNull(it.amount, it.name).joinToString(" ")}") } } }; Text("Steps", fontSize = 21.sp, fontWeight = FontWeight.Bold); r.steps.forEachIndexed { index, step -> Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) { Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) { Surface(Modifier.size(30.dp), color = Tomato, shape = RoundedCornerShape(15.dp)) { Box(contentAlignment = Alignment.Center) { Text("${index + 1}", color = Color.White, fontWeight = FontWeight.Bold) } }; Column { Text(step.label, fontWeight = FontWeight.Bold); Spacer(Modifier.height(4.dp)); Text(step.text, color = Color.DarkGray) } } } } } } } } ?: Box(Modifier.fillMaxSize().padding(p), Alignment.Center) { if(state is AsyncUiState.Error) Text((state as AsyncUiState.Error).message) else CircularProgressIndicator() }
}

@Composable private fun RecipeCard(r: Recipe, open: (Int) -> Unit) = Card(Modifier.fillMaxWidth().clickable { open(r.id) }, shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) { Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) { AsyncImage(r.imageUrl, r.title, Modifier.size(96.dp).clip(RoundedCornerShape(16.dp)).background(Sage), contentScale = ContentScale.Crop); Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) { Text(r.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, maxLines = 2, overflow = TextOverflow.Ellipsis); Surface(color = Color(0xFFFFE9E5), shape = RoundedCornerShape(12.dp)) { Text(r.category.replaceFirstChar { it.uppercase() }, Modifier.padding(horizontal = 8.dp, vertical = 3.dp), color = Tomato, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }; if(r.hasVideo) Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Outlined.PlayCircleOutline, null, Modifier.size(15.dp), tint = Sage); Spacer(Modifier.width(4.dp)); Text("Video recipe", color = Color.Gray, fontSize = 12.sp) } } } }
@Composable private fun Profile(p: PaddingValues, language: UiLanguage, savedCount: Int, changeLanguage: (UiLanguage) -> Unit) { val viewModel: ProfileViewModel = viewModel(); val total by viewModel.recipeCount.collectAsStateWithLifecycle(); var showLanguageDialog by remember { mutableStateOf(false) }; LaunchedEffect(Unit) { viewModel.load() }; LazyColumn(Modifier.fillMaxSize().padding(p).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(vertical = 24.dp)) { item { Text(label(Tab.PROFILE, language), fontSize = 28.sp, fontWeight = FontWeight.Bold) }; item { Row(verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(72.dp), color = Tomato, shape = RoundedCornerShape(36.dp)) { Box(contentAlignment = Alignment.Center) { Text("AM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp) } }; Spacer(Modifier.width(16.dp)); Column { Text("Alex Morgan", fontSize = 21.sp, fontWeight = FontWeight.Bold); Text("Food explorer", color = Color.Gray) } } }; item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Stat("$savedCount", "Saved"); Stat(total?.toString() ?: "…", "Recipes"); Stat(language.label, "Interface") } }; item { Text(ui("settings", language), fontSize = 20.sp, fontWeight = FontWeight.Bold) }; item { SettingRow("Cuisine preferences", "Uzbek · Japanese · Vegetarian") }; item { SettingRow(ui("language", language), language.label, onClick = { showLanguageDialog = true }) }; items(listOf("Dietary preferences", "Notifications", "Help & support")) { setting -> SettingRow(setting) } }; if (showLanguageDialog) LanguageDialog(language, onDismiss = { showLanguageDialog = false }) { choice -> changeLanguage(choice); showLanguageDialog = false } }
@Composable private fun LanguageDialog(selected: UiLanguage, onDismiss: () -> Unit, onChoose: (UiLanguage) -> Unit) = AlertDialog(onDismissRequest = onDismiss, containerColor = Cream, shape = RoundedCornerShape(28.dp), title = { Text(ui("chooseLanguage", selected), fontWeight = FontWeight.Bold, fontSize = 23.sp) }, text = { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { Text(ui("languageDescription", selected), color = Color.Gray); UiLanguage.entries.forEach { option -> Surface(Modifier.fillMaxWidth().clickable { onChoose(option) }, color = if (option == selected) Color(0xFFFFE9E5) else Color.White, shape = RoundedCornerShape(16.dp)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Text(option.label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold); if (option == selected) Icon(Icons.Outlined.Check, null, tint = Tomato) } } } } }, confirmButton = { TextButton(onClick = onDismiss) { Text(ui("cancel", selected), color = Tomato) } })
@Composable private fun SettingRow(title: String, subtitle: String = "", onClick: () -> Unit = {}) = Card(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Medium); if (subtitle.isNotBlank()) Text(subtitle, color = Color.Gray, fontSize = 13.sp) }; Icon(Icons.Outlined.ChevronRight, null, tint = Color.Gray) } }
@Composable private fun Stat(value: String, name: String) = Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold); Text(name, color = Color.Gray, fontSize = 12.sp) }
@Composable private fun EmptyPanel(title: String, body: String, icon: androidx.compose.ui.graphics.vector.ImageVector) = Column(Modifier.fillMaxWidth().padding(vertical = 54.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) { Icon(icon, null, Modifier.size(44.dp), tint = Sage); Text(title, fontWeight = FontWeight.Bold, fontSize = 19.sp); Text(body, color = Color.Gray) }
