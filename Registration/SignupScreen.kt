package com.example.mobcomfinal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

@Composable
fun SignUpScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }
    var studentNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current

    val gradientColors = listOf(
        Color(0xFF4A148C),
        Color(0xFF1A237E),
        Color(0xFF0D1244)
    )

    val radialGradientBrush = Brush.radialGradient(
        colors = gradientColors,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(radialGradientBrush)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "Join CICS SmartPass for easy attendance",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )
                Text(text = "Full Name", fontWeight = FontWeight.Bold, color = Color(0xFF555555))
                ModernInputField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "John Doe"
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Student Number", fontWeight = FontWeight.Bold, color = Color(0xFF555555))
                ModernInputField(
                    value = studentNumber,
                    onValueChange = { studentNumber = it },
                    placeholder = "25-1111"
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Email Address", fontWeight = FontWeight.Bold, color = Color(0xFF555555))
                ModernInputField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "hello@example.com"
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Password", fontWeight = FontWeight.Bold, color = Color(0xFF555555))
                ModernInputField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "********",
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && studentNumber.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                            val ref = FirebaseDatabase
                                .getInstance("https://cics-smartpass-default-rtdb.firebaseio.com")
                                .reference.child("users")

                            val userId = ref.push().key

                            if (userId != null) {
                                val userData = mapOf(
                                    "name" to name,
                                    "studentNumber" to studentNumber,
                                    "email" to email,
                                    "password" to password
                                )

                                ref.child(userId).setValue(userData)
                                    .addOnSuccessListener {
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        error = "Failed: ${it.message}"
                                    }
                            }

                        } else error = "Please fill all fields"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Sign Up", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text("Already have an account? Login")
                    }
                }
            }
        }
        LaunchedEffect(error) {
            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                error = ""
            }
        }
    }
}
