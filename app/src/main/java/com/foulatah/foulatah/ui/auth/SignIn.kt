package com.foulatah.foulatah.ui.auth



import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foulatah.foulatah.navigation.ROUTE_DASHBOARD
import com.foulatah.foulatah.navigation.ROUTE_REGISTER
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    BackHandler {
        navController.popBackStack()

    }
    AuthHeader()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text("Login", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        } else {



            Button(
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        error = "Please fill in all fields"
                    } else {
                        isLoading = true
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    navController.navigate(ROUTE_DASHBOARD)
                                } else {
                                    error = task.exception?.message ?: "Login failed"
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            androidx.compose.material3.Text(
                modifier = Modifier

                    .clickable {
                        navController.navigate(ROUTE_REGISTER)
                    },
                text = "Register Here",
                textAlign = TextAlign.Center,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )



        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
