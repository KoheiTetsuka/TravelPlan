package com.android.exemple.planapp.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

sealed class Item(var dist: String, var icon: ImageVector) {
    object Plan: Item("Plan", Icons.Filled.Edit)
    object Property: Item("Property", Icons.Filled.List)
}

@Composable
fun BottomBar(navController: NavController) {
    var selectedItem = remember { mutableStateOf(0) }
    val items = listOf(Item.Plan, Item.Property)

    BottomNavigation {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.dist) },
                label = { Text(item.dist) },
                selected = selectedItem.value == index,
                onClick = {
                    if (item.dist == "Plan") {
                        navController.navigate("home")
                    } else if (item.dist == "Property") {
                        navController.navigate("property")
                    }
                }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BottomNavigationPreview() {
//    BottomBar(navController: NavController)
//}
