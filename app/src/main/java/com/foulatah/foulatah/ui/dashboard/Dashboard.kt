package com.foulatah.foulatah.ui.dashboard

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.foulatah.foulatah.navigation.ROUTE_ABOUT
import com.foulatah.foulatah.navigation.ROUTE_COMPLAINTS
import com.foulatah.foulatah.navigation.ROUTE_LOGIN
import com.foulatah.foulatah.navigation.ROUTE_PAYMENT
import com.foulatah.foulatah.navigation.ROUTE_SUGGESTIONS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

private var progressDialog: ProgressDialog? = null

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(navController: NavHostController) {
    var tenant by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()
    var user: User? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }
    var tenantCount by remember { mutableStateOf(0) }

    BackHandler {
        navController.popBackStack()
    }

    // Fetch user details from Firestore
    LaunchedEffect(key1 = currentUser?.uid) {
        if (currentUser != null) {
            val userDocRef = firestore.collection("users").document(currentUser.uid)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        user = document.toObject<User>()
                        tenant = user?.tenant ?: ""  // Update tenant state
                        name = user?.name ?: ""      // Update name state
                    }
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    isLoading = false
                }
        }
    }

    LaunchedEffect(Unit) {
        firestore.collection("Tenants")
            .get()
            .addOnSuccessListener { result ->
                tenantCount = result.size()
            }
            .addOnFailureListener { exception ->
                // Handle failures
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Dashboard", color = Color.White, fontSize = 30.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Icon", tint = Color.White)
                    }
                },
            )
        }, content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dashboard starts here

                val dashboardItems = listOf(
                    DashboardItemData(
                        title = "Tenants",
                        icon = Icons.Default.AccountCircle,
                        badgeCount = tenantCount,
                        onClick = { navController.navigate(ROUTE_LOGIN)
                            // Navigate to tenants screen
                            // Example: navController.navigate("tenants")
                        }
                    ),
                    DashboardItemData(
                        title = "Payment",
                        icon = Icons.AutoMirrored.Filled.Send,
                        badgeCount = 3,
                        onClick = { navController.navigate(ROUTE_PAYMENT)
                            // Navigate to payment screen
                            // Example: navController.navigate("payment")
                        }
                    ),
                    DashboardItemData(
                        title = "Complaints",
                        icon = Icons.Default.Email,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_COMPLAINTS)
                            // Navigate to complaints screen
                            // Example: navController.navigate("complaints")
                        }
                    ),
                    DashboardItemData(
                        title = "suggestions",
                        icon = Icons.Default.MailOutline,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_SUGGESTIONS)
                            // Navigate to suggestions screen
                            // Example: navController.navigate("suggestions")
                        }
                    ),
                    DashboardItemData(
                        title = "About",
                        icon = Icons.Default.Build,
                        badgeCount = 4,
                        onClick = { navController.navigate(ROUTE_ABOUT)
                            // Navigate to about screen
                            // Example: navController.navigate("about")
                        }
                    ),
                    // Add more dashboard items as needed
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(dashboardItems) { item ->
                        DashboardItem(item)
                    }
                }

                // Dashboard ends here
            }
        }
    )
}


@Composable
fun DashboardItem(item: DashboardItemData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = item.onClick,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,  // Updated content description for better accessibility
                tint = Color.Black,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            // Add a badge if the badge count is greater than 0
            if (item.badgeCount > 0) {
                Badge(count = item.badgeCount)
            }
        }
    }
}

@Composable
fun Badge(count: Int) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(20.dp)
            .clip(CircleShape)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}


data class DashboardItemData(
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int,
    val onClick: () -> Unit
)

data class User(
    val tenantId: String = "",
    val tenant: String = "",
    val name: String = ""
)

fun saveUserDetails(user: User, param: (Any) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("users").document(user.tenantId)
        .set(user)
        .addOnSuccessListener {
            progressDialog?.dismiss()
            // Success message or navigation
        }
        .addOnFailureListener {
            progressDialog?.dismiss()
            // Handle failure
        }
}
