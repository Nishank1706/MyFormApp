package com.example.myformapp

import android.R.attr.fontStyle
import android.R.attr.fontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign

//This screen displays the list of forms from Firebase
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayFormScreen(navController: NavController) {

    // state list to hold forms
    val forms = remember { mutableStateListOf<Form>() }

    // load forms from Firebase (assumes getforms takes a callback (List<Form>) -> Unit)
    LaunchedEffect(Unit) {
        FirebaseRepository.getforms { loaded ->
            forms.clear()
            forms.addAll(loaded)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Form Page") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addForm")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Form"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Lazy list of forms
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(forms) { form ->
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            onClick = {
                            navController.navigate("edit/${form.id}/${form.name}/${form.email}/${form.gender}")/* Not just only navigate but navigate with the specific data. */ },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Name : ${form.name}",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.width(24.dp))
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 8.dp)
                                            .border(
                                                width = 1.dp,
                                                color = Color(0xFF6200EE),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Email : ${form.email}",
                                            fontWeight = FontWeight.Bold,
                                            fontStyle = FontStyle.Italic,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Gender : ${form.gender}")
                                    }
                                }

                                //Delete Icon
                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(20.dp)
                                        .clickable {
                                            // Delete the form from Firebase
                                            FirebaseRepository.deleteform(form.id)
                                            /*{
                                                // On successful deletion, remove from local list
                                                forms.remove(form)
                                                 }
                                                 */
                                        }

                                    )

                            }
                        }
                    }
                }
            )
        }
    }
}