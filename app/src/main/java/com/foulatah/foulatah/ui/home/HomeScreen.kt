package com.foulatah.foulatah.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.foulatah.foulatah.navigation.ROUTE_ADD_TENANTS
import com.foulatah.foulatah.navigation.ROUTE_DASHBOARD
import com.foulatah.foulatah.navigation.ROUTE_HOME
import com.foulatah.foulatah.ui.tenants.Bill
import com.foulatah.foulatah.ui.tenants.fetchTenantWithBills

data class Screen(val title: String, val icon: Int)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var tenantId by remember { mutableStateOf<String?>(null) }
    var tenantName by remember { mutableStateOf<String?>(null) }
    var tenantEmail by remember { mutableStateOf<String?>(null) }
    var tenantPhone by remember { mutableStateOf<String?>(null) }
    var tenantHouseNumber by remember { mutableStateOf<String?>(null) }
    var tenantBills by remember { mutableStateOf<List<Bill>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(tenantId) {
        tenantId?.let {
            coroutineScope.launch {
                val (tenant, bills) = fetchTenantWithBills(it)
                tenantName = tenant.name
                tenantEmail = tenant.email
                tenantPhone = tenant.phone
                tenantHouseNumber = tenant.houseNumber
                tenantBills = bills
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Foulatah")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_ADD_TENANTS)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.White,
                )
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            tenantName?.let {
                Text(text = "Name: $it", fontSize = 20.sp, color = Color.Black)
            }
            tenantEmail?.let {
                Text(text = "Email: $it", fontSize = 20.sp, color = Color.Black)
            }
            tenantPhone?.let {
                Text(text = "Phone: $it", fontSize = 20.sp, color = Color.Black)
            }
            tenantHouseNumber?.let {
                Text(text = "House Number: $it", fontSize = 20.sp, color = Color.Black)
            }

            tenantBills?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Bills:", fontSize = 24.sp, color = Color.Black)
                it.forEach { bill ->
                    BillDetail(bill = bill)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun BillDetail(bill: Bill) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {
        Text(text = "Bill ID: ${bill.id}", fontSize = 16.sp, color = Color.Black)
        Text(text = "Rent: ${bill.rent}", fontSize = 16.sp, color = Color.Black)
        Text(text = "Arrears: ${bill.arrears}", fontSize = 16.sp, color = Color.Black)
        Text(text = "Garbage: ${bill.garbage}", fontSize = 16.sp, color = Color.Black)
        Text(text = "Water: ${bill.water}", fontSize = 16.sp, color = Color.Black)
        Text(text = "Due Date: ${bill.dueDate}", fontSize = 16.sp, color = Color.Black)
        Text(text = "Total: ${bill.total}", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val selectedIndex = remember { mutableStateOf(0) }

    BottomNavigation(
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        BottomNavigationItem(
            icon = {
                Icon(imageVector = Icons.Filled.Home, contentDescription = null, tint = Color.White)
            },
            label = {
                Text(text = "Home", color = Color.White)
            },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
                navController.navigate(ROUTE_HOME)
            }
        )

        BottomNavigationItem(
            icon = {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null, tint = Color.White)
            },
            label = {
                Text(text = "Dashboard", color = Color.White)
            },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
                navController.navigate(ROUTE_DASHBOARD)
            }
        )

    }
}
