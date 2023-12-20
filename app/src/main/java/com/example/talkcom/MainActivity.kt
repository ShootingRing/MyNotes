package com.example.talkcom

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.talkcom.ui.theme.TalkComTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkComTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun NoteCard(text: String) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .size(width = 280.dp, height = 100.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun List() {
    var notes by remember { mutableStateOf(listOf("Note 1", "Note 2", "Note 3")) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (note in notes) {
            NoteCard(text = note)
        }

        // FloatingActionButton to add a new note
        FloatingActionButton(
            onClick = { showDialog = true },
            content = { Icon(Icons.Default.Add, contentDescription = null) },
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
        )

        // AlertDialog for adding a new note
        if (showDialog) {
            AddNoteDialog(
                onDismiss = { showDialog = false },
                onAddNote = { newNote ->
                    notes = notes + listOf(newNote)
                    showDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onAddNote: (String) -> Unit
) {
    var noteText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add a New Note") },
        text = {
            TextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Note Content") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddNote.invoke(noteText)
                        onDismiss.invoke()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        confirmButton = {
            IconButton(onClick = {
                onAddNote.invoke(noteText)
                onDismiss.invoke()
            }) {
                Icon(Icons.Default.Send, contentDescription = null)
            }
        },
        dismissButton = {
            IconButton(onClick = {
                onDismiss.invoke()
            }) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun MainPage() {
    val tabs = listOf("profile", "list")
    var position by remember { mutableStateOf(0) }
    var icons = listOf(Icons.Filled.AccountCircle, Icons.Filled.Menu)
    var pages = listOf(Text("Profile", color = MaterialTheme.colorScheme.secondary), Text("List", color = MaterialTheme.colorScheme.secondary))
    Scaffold(
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, tab ->
                    BottomNavigationItem(
                        selected = index == position,
                        label = ({ Text(tab) }),
                        onClick = { position = index },
                        icon = { Icon(icons[index], contentDescription = null) },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    ) {
        when(position) {
            0 -> Text("List", color = MaterialTheme.colorScheme.secondary)
            1 -> List()
        }
    }
}