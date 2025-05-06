package com.alexser.weathernote.presentation.screens.login

//@Composable
//fun LoginScreen(
//    viewModel: LoginViewModel,
//    onLoginSuccess: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        TextField(
//            value = uiState.email,
//            onValueChange = viewModel::onEmailChange,
//            label = { Text("Email") }
//        )
//
//        TextField(
//            value = uiState.password,
//            onValueChange = viewModel::onPasswordChange,
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation()
//        )
//
//        Button(
//            onClick = { viewModel.onLoginClick(onLoginSuccess) },
//            enabled = !uiState.isLoading
//        ) {
//            Text("Login")
//        }
//
//        if (uiState.error != null) {
//            Text(
//                text = uiState.error!!,
//                color = Color.Red,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        if (uiState.isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//        }
//    }
//}
