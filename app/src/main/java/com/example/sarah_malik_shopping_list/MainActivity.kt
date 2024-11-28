package com.example.sarah_malik_shopping_list

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sarah_malik_shopping_list.ui.theme.Sarah_malik_shopping_listTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sarah_malik_shopping_listTheme {
                ShoppingListApp()
            }
        }
    }
}

@Composable
fun ShoppingListApp() {
    // Use a mutable state list for proper re-composition
    val itemList = remember { mutableStateListOf<String>() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { itemList.add("") }) {
                Text("+")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itemList) { item ->
                    ShoppingListItem(
                        item = item,
                        onRemove = { itemList.remove(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingListItem(item: String, onRemove: () -> Unit) {
    var itemName by remember { mutableStateOf(item) }
    var isChecked by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf("1") }
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        QuantityDropdown(quantity = quantity, onQuantityChange = { quantity = it })
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show()
            onRemove()
        }) {
            Text("Remove")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityDropdown(quantity: String, onQuantityChange: (String) -> Unit) {
    val options = (1..20).map { it.toString() }
    var expanded by remember { mutableStateOf(false) }

    Box {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = quantity,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onQuantityChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListAppPreview() {
    Sarah_malik_shopping_listTheme {
        ShoppingListApp()
    }
}