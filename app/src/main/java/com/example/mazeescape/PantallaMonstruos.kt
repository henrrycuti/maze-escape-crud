package com.example.mazeescape

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMonstruos() {
    val context = LocalContext.current
    val dao = remember { MonstruoDatabase.get(context).monstruoDao() }
    val scope = rememberCoroutineScope()
    val listaFlow = remember { dao.getAll() }
    val monstruos by listaFlow.collectAsState(initial = emptyList())

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var velocidad by remember { mutableStateOf("") }
    var editando by remember { mutableStateOf<Monstruo?>(null) }
    var error by remember { mutableStateOf("") }

    fun limpiar() {
        nombre = ""
        descripcion = ""
        velocidad = ""
        editando = null
        error = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monstruos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ---------- Formulario (crear / editar) ----------
            Text(
                text = if (editando == null) "Nuevo monstruo" else "Editar monstruo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = velocidad,
                onValueChange = { velocidad = it },
                label = { Text("Velocidad (1 a 10)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (error.isNotEmpty()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val vel = velocidad.toIntOrNull()
                    if (nombre.isBlank() || vel == null || vel !in 1..10) {
                        error = "Escribe un nombre y una velocidad entre 1 y 10"
                    } else {
                        // Guardamos los valores ANTES de lanzar la corrutina,
// porque limpiar() vacía los campos antes de que ella corra
                        val n = nombre.trim()
                        val d = descripcion.trim()
                        val actual = editando
                        scope.launch {
                            if (actual == null) {
                                dao.insert(Monstruo(nombre = n, descripcion = d, velocidad = vel))
                            } else {
                                dao.update(actual.copy(nombre = n, descripcion = d, velocidad = vel))
                            }
                        }
                        limpiar()
                    }
                }) {
                    Text(if (editando == null) "Guardar" else "Actualizar")
                }
                if (editando != null) {
                    OutlinedButton(onClick = { limpiar() }) { Text("Cancelar") }
                }
            }

            // ---------- Lista (leer) ----------
            Text(
                text = "Mis monstruos (${monstruos.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(monstruos, key = { it.id }) { m ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    m.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    m.descripcion,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Velocidad: ${m.velocidad}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            // UPDATE: carga los datos en el formulario
                            IconButton(onClick = {
                                editando = m
                                nombre = m.nombre
                                descripcion = m.descripcion
                                velocidad = m.velocidad.toString()
                                error = ""
                            }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar")
                            }
                            // DELETE
                            IconButton(onClick = {
                                scope.launch { dao.delete(m) }
                                if (editando?.id == m.id) limpiar()
                            }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
