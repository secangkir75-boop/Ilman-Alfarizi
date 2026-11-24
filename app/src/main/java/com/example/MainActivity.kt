package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.AppDatabase
import com.example.data.local.CertificateEntity
import com.example.data.local.SessionManager
import com.example.data.local.UserAccountEntity
import com.example.data.model.LearningCategory
import com.example.data.model.UserRole
import com.example.data.repository.AuthRepository
import com.example.data.repository.LearningRepository
import com.example.ui.components.CertificateAcceptPromptDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CertificatesScreen
import com.example.ui.screens.DesignerDashboardScreen
import com.example.ui.screens.DesignerInfoScreen
import com.example.ui.screens.ExamScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ModuleDetailScreen
import com.example.ui.screens.StudentProfileScreen
import com.example.ui.theme.EmeraldNight
import com.example.ui.theme.EmeraldVibrant
import com.example.ui.theme.GoldRadiant
import com.example.ui.theme.IhyaulArabiyyahTheme
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.LearningViewModel

object AppRoutes {
    const val HOME = "home"
    const val EXAM = "exam"
    const val CERTIFICATES = "certificates"
    const val DESIGNER_DASHBOARD = "designer_dashboard"
    const val PROFILE = "profile"
    const val DESIGNER_INFO = "designer_info"
    const val MODULE = "module/{category}"

    fun moduleRoute(category: LearningCategory): String = "module/${category.name}"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val sessionManager = SessionManager(applicationContext)
        val authRepository = AuthRepository(database, sessionManager)
        val learningRepository = LearningRepository(database)

        setContent {
            IhyaulArabiyyahTheme {
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModel.Factory(authRepository)
                )
                val learningViewModel: LearningViewModel = viewModel(
                    factory = LearningViewModel.Factory(learningRepository)
                )

                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
                val activeRole by authViewModel.activeRole.collectAsStateWithLifecycle()
                val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(currentUser?.email) {
                    learningViewModel.bindStudentEmail(currentUser?.email)
                }

                val user = currentUser
                if (user == null) {
                    AuthScreen(
                        uiState = authUiState,
                        onSignIn = { email, password, designerOnly ->
                            authViewModel.signIn(email, password, designerOnly)
                        },
                        onRegisterStudent = { email, password, name, school, region, level ->
                            authViewModel.registerStudent(email, password, name, school, region, level)
                        },
                        onResetPassword = { email, name, newPass ->
                            authViewModel.resetStudentPassword(email, name, newPass)
                        },
                        onClearMessages = { authViewModel.clearMessages() }
                    )
                } else {
                    val isDesignerAccount = activeRole == UserRole.DESIGNER
                    val effectiveDesignerMode = isDesignerAccount && !authUiState.designerPreviewAsStudent

                    IhyaulArabiyyahMainShell(
                        currentUser = user,
                        isDesignerAccount = isDesignerAccount,
                        effectiveDesignerMode = effectiveDesignerMode,
                        previewAsStudent = authUiState.designerPreviewAsStudent,
                        authInfoMessage = authUiState.infoMessage,
                        onClearAuthMessages = { authViewModel.clearMessages() },
                        onTogglePreviewAsStudent = { authViewModel.toggleDesignerStudentPreview(it) },
                        authViewModel = authViewModel,
                        learningViewModel = learningViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun IhyaulArabiyyahMainShell(
    currentUser: UserAccountEntity,
    isDesignerAccount: Boolean,
    effectiveDesignerMode: Boolean,
    previewAsStudent: Boolean,
    authInfoMessage: String?,
    onClearAuthMessages: () -> Unit,
    onTogglePreviewAsStudent: (Boolean) -> Unit,
    authViewModel: AuthViewModel,
    learningViewModel: LearningViewModel
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }

    val allContents by learningViewModel.allContents.collectAsStateWithLifecycle()
    val allQuestions by learningViewModel.allQuestions.collectAsStateWithLifecycle()
    val allExamAttempts by learningViewModel.allExamAttempts.collectAsStateWithLifecycle()
    val allCertificates by learningViewModel.allCertificates.collectAsStateWithLifecycle()
    val allStudents by authViewModel.allStudents.collectAsStateWithLifecycle()

    val studentAttempts by learningViewModel.studentAttempts.collectAsStateWithLifecycle()
    val studentCertificates by learningViewModel.studentCertificates.collectAsStateWithLifecycle()
    val learningUiState by learningViewModel.uiState.collectAsStateWithLifecycle()

    val pendingCertificates = remember(studentCertificates) {
        studentCertificates.filter { !it.isAcceptedByStudent }
    }

    var certificatePromptToAccept by remember { mutableStateOf<CertificateEntity?>(null) }

    LaunchedEffect(learningUiState.feedbackMessage) {
        val msg = learningUiState.feedbackMessage
        if (!msg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(msg)
            learningViewModel.clearFeedback()
        }
    }

    LaunchedEffect(authInfoMessage) {
        if (!authInfoMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(authInfoMessage)
            onClearAuthMessages()
        }
    }

    val topLevelRoutes = remember(effectiveDesignerMode) {
        if (effectiveDesignerMode) {
            listOf(
                AppRoutes.HOME,
                AppRoutes.DESIGNER_DASHBOARD,
                AppRoutes.EXAM,
                AppRoutes.CERTIFICATES,
                AppRoutes.PROFILE
            )
        } else {
            listOf(
                AppRoutes.HOME,
                AppRoutes.EXAM,
                AppRoutes.CERTIFICATES,
                AppRoutes.PROFILE
            )
        }
    }

    val showBottomBar = currentRoute in topLevelRoutes

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = EmeraldNight,
                    contentColor = Color.White,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.HOME,
                        onClick = {
                            navController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") },
                        label = { Text("Beranda", fontWeight = FontWeight.Medium) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_home")
                    )

                    if (effectiveDesignerMode) {
                        NavigationBarItem(
                            selected = currentRoute == AppRoutes.DESIGNER_DASHBOARD,
                            onClick = {
                                navController.navigate(AppRoutes.DESIGNER_DASHBOARD) {
                                    popUpTo(AppRoutes.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Perancang") },
                            label = { Text("Perancang", fontWeight = FontWeight.Medium) },
                            colors = navItemColors(),
                            modifier = Modifier.testTag("nav_designer")
                        )
                    }

                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.EXAM,
                        onClick = {
                            navController.navigate(AppRoutes.EXAM) {
                                popUpTo(AppRoutes.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Quiz, contentDescription = "Ujian") },
                        label = { Text("Ujian", fontWeight = FontWeight.Medium) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_exam")
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.CERTIFICATES,
                        onClick = {
                            navController.navigate(AppRoutes.CERTIFICATES) {
                                popUpTo(AppRoutes.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (pendingCertificates.isNotEmpty()) {
                                        Badge(containerColor = GoldRadiant, contentColor = EmeraldNight) {
                                            Text("${pendingCertificates.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.WorkspacePremium, contentDescription = "Sertifikat")
                            }
                        },
                        label = { Text("Sertifikat", fontWeight = FontWeight.Medium) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_certificates")
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppRoutes.PROFILE,
                        onClick = {
                            navController.navigate(AppRoutes.PROFILE) {
                                popUpTo(AppRoutes.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") },
                        label = { Text("Profil", fontWeight = FontWeight.Medium) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        com.example.ui.components.ArabianPatternBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = AppRoutes.HOME
            ) {
                composable(AppRoutes.HOME) {
                    HomeScreen(
                        currentUser = currentUser,
                        isDesigner = effectiveDesignerMode,
                        allContents = allContents,
                        studentAttempts = studentAttempts,
                        pendingCertificates = pendingCertificates,
                        onOpenCategory = { category ->
                            navController.navigate(AppRoutes.moduleRoute(category))
                        },
                        onOpenExam = {
                            navController.navigate(AppRoutes.EXAM) {
                                launchSingleTop = true
                            }
                        },
                        onOpenDesignerInfo = {
                            navController.navigate(AppRoutes.DESIGNER_INFO)
                        },
                        onOpenDesignerDashboard = {
                            navController.navigate(AppRoutes.DESIGNER_DASHBOARD) {
                                launchSingleTop = true
                            }
                        },
                        onPromptAcceptCertificate = { cert ->
                            certificatePromptToAccept = cert
                        }
                    )
                }

                composable(
                    route = AppRoutes.MODULE,
                    arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { entry ->
                    val categoryName = entry.arguments?.getString("category") ?: LearningCategory.MATERI.name
                    val category = LearningCategory.entries.firstOrNull { it.name == categoryName }
                        ?: LearningCategory.MATERI
                    val filteredContents = remember(allContents, category) {
                        allContents.filter { it.category == category.name }
                    }
                    ModuleDetailScreen(
                        category = category,
                        contents = filteredContents,
                        isDesigner = effectiveDesignerMode,
                        studentEmail = currentUser.email,
                        onBack = { navController.popBackStack() },
                        onSaveContent = { learningViewModel.saveContent(it) },
                        onDeleteContent = { learningViewModel.deleteContent(it) },
                        onSavePractice = { contentId, text ->
                            learningViewModel.savePractice(
                                studentEmail = currentUser.email,
                                contentId = contentId,
                                category = category.name,
                                responseText = text
                            )
                        }
                    )
                }

                composable(AppRoutes.EXAM) {
                    ExamScreen(
                        currentUser = currentUser,
                        questions = allQuestions,
                        attempts = studentAttempts,
                        lastExamAttempt = learningUiState.lastExamAttempt,
                        onSubmitExam = { questions, answers ->
                            learningViewModel.submitExam(
                                studentEmail = currentUser.email,
                                studentName = currentUser.fullName,
                                questions = questions,
                                selectedAnswers = answers
                            )
                        },
                        onDismissResultDialog = { learningViewModel.clearLastExamAttempt() }
                    )
                }

                composable(AppRoutes.CERTIFICATES) {
                    CertificatesScreen(
                        certificates = studentCertificates,
                        onPromptAcceptCertificate = { cert ->
                            certificatePromptToAccept = cert
                        }
                    )
                }

                composable(AppRoutes.DESIGNER_DASHBOARD) {
                    if (effectiveDesignerMode) {
                        DesignerDashboardScreen(
                            allContents = allContents,
                            allQuestions = allQuestions,
                            allStudents = allStudents,
                            allAttempts = allExamAttempts,
                            allCertificates = allCertificates,
                            onSaveContent = { learningViewModel.saveContent(it) },
                            onDeleteContent = { learningViewModel.deleteContent(it) },
                            onSaveQuestion = { learningViewModel.saveQuestion(it) },
                            onDeleteQuestion = { learningViewModel.deleteQuestion(it) },
                            onIssueCertificate = { email, name, attemptId, score, predicate, note ->
                                learningViewModel.issueCertificate(email, name, attemptId, score, predicate, note)
                            },
                            onDeleteCertificate = { learningViewModel.deleteCertificate(it) }
                        )
                    }
                }

                composable(AppRoutes.PROFILE) {
                    StudentProfileScreen(
                        currentUser = currentUser,
                        isDesignerAccount = isDesignerAccount,
                        previewAsStudent = previewAsStudent,
                        onTogglePreviewAsStudent = onTogglePreviewAsStudent,
                        onSaveProfile = { name, school, region, level, phone, goal ->
                            authViewModel.updateProfile(
                                current = currentUser,
                                fullName = name,
                                schoolOrCampus = school,
                                originRegion = region,
                                arabicLevel = level,
                                phone = phone,
                                learningGoal = goal
                            )
                        },
                        onOpenDesignerInfo = { navController.navigate(AppRoutes.DESIGNER_INFO) },
                        onOpenDesignerDashboard = {
                            onTogglePreviewAsStudent(false)
                            navController.navigate(AppRoutes.DESIGNER_DASHBOARD) {
                                launchSingleTop = true
                            }
                        },
                        onSignOut = { authViewModel.signOut() }
                    )
                }

                composable(AppRoutes.DESIGNER_INFO) {
                    DesignerInfoScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    if (certificatePromptToAccept != null) {
        val cert = certificatePromptToAccept!!
        CertificateAcceptPromptDialog(
            certificate = cert,
            onAccept = {
                learningViewModel.acceptCertificate(cert.id)
                certificatePromptToAccept = null
                navController.navigate(AppRoutes.CERTIFICATES) {
                    launchSingleTop = true
                }
            },
            onDismiss = { certificatePromptToAccept = null }
        )
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = EmeraldNight,
    selectedTextColor = GoldRadiant,
    indicatorColor = GoldRadiant,
    unselectedIconColor = Color.White.copy(alpha = 0.72f),
    unselectedTextColor = Color.White.copy(alpha = 0.72f)
)
