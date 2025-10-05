package com.numie.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.numie.app.screens.SplashScreen
import com.numie.app.screens.HomeScreen
import com.numie.app.ui.theme.NumieTheme

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            NumieTheme {
                var showSplash by remember { mutableStateOf(true) }
                var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

                when {
                    showSplash -> {
                        SplashScreen(
                            onSplashFinished = {
                                Log.d("Splash", "Finished! Move to Auth/Home.")
                                showSplash = false
                            }
                        )
                    }
                    isLoggedIn -> {
                        HomeScreen()
                    }
                    else -> {
                        AuthScreen(
                            auth = auth,
                            onLoginSuccess = { isLoggedIn = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    auth: FirebaseAuth,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        message = if (task.isSuccessful) {
                            onLoginSuccess() // auto-login after registration
                            "Registered! UID: ${auth.currentUser?.uid}"
                        } else {
                            "Registration failed: ${task.exception?.message}"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        message = if (task.isSuccessful) {
                            onLoginSuccess()
                            "Log in successful! UID: ${auth.currentUser?.uid}"
                        } else {
                            "Login failed: ${task.exception?.message}"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(message)
    }
}
