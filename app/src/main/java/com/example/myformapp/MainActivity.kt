package com.example.myformapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MyFormApp()
        }
    }
}

@Composable
fun MyFormApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "displayForm") {
        composable("displayForm") {
            DisplayFormScreen(navController = navController)
        }
        composable("addForm") {
            AddFormScreen(navController = navController)
        }
        composable(route = "edit/{id}/{name}/{email}/{gender}", arguments = listOf(
            navArgument("id") { type = NavType.StringType },
            navArgument("name") { type = NavType.StringType },
            navArgument("email") { type = NavType.StringType },
            navArgument("gender") { type = NavType.StringType }
        )) { backstackEntry ->
            val args = backstackEntry.arguments!!
            EditFormScreen(
                navController,
                Form(
                    id = args.getString("id") ?: "",
                    name = args.getString("name") ?: "",
                    email = args.getString("email") ?: "",
                    gender = args.getString("gender") ?: ""
                )
            )
        }
    }
}

