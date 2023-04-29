package com.bestDate.presentation.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.toServerFormat
import com.bestDate.data.model.SocialProvider
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.auth.AuthViewModel
import com.bestDate.presentation.registration.GenderType
import com.bestDate.view.alerts.LoaderDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

abstract class BaseAuthFragment<VB: ViewBinding>: BaseVMFragment<VB, AuthViewModel>() {
    override val viewModelClass: Class<AuthViewModel> = AuthViewModel::class.java

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    protected lateinit var loaderDialog: LoaderDialog
    protected var isLoggedIn = false
    private var user: UserDB? = null

    abstract fun navigateToMain()
    abstract fun navigateToFillData(name: String?, birthDate: String?, genderType: GenderType)
    abstract fun navigateToPhoto()
    abstract fun navigateToQuestionnaire()

    override fun onInit() {
        super.onInit()
        loaderDialog = LoaderDialog(requireActivity())
        facebookCallback()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.errorLiveData) {
            errorAction()
        }
        observe(viewModel.user) {
            user = it
        }
        observe(viewModel.loginSuccessLiveData) {
            if (user != null && isLoggedIn) {
                chooseRoute()
            }
        }
    }

    protected open fun errorAction() {
        isLoggedIn = false
        loaderDialog.stopLoading()
        showMessage(getString(R.string.wrong_auth_data))
    }

    private fun chooseRoute() {
        loaderDialog.stopLoading()
        when {
            viewModel.registrationSocialMode -> {
                viewModel.registrationSocialMode = false
                val name = viewModel.user.value?.name
                val gender = viewModel.user.value?.getGenderType() ?: GenderType.WOMAN_LOOKING_MAN
                val birthDate = viewModel.user.value?.getBirthDate()?.toServerFormat()
                navigateToFillData(name, birthDate, gender)
            }
            viewModel.user.value?.hasNoPhotos() == true && viewModel.getSkipImageCount() < 3 -> {
                navigateToPhoto()
            }
            viewModel.user.value?.questionnaireEmpty() == true &&
                    viewModel.getSkipQuestionnaireCount() < 3 -> {
                navigateToQuestionnaire()
            }
            else -> {
                navigateToMain()
            }
        }
    }

    private fun facebookCallback() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    loaderDialog.startLoading()
                    isLoggedIn = true
                    viewModel.loginSocial(SocialProvider.FACEBOOK, result.accessToken.token, getString(R.string.app_locale))
                }

                override fun onCancel() {
                    isLoggedIn = false
                }

                override fun onError(error: FacebookException) {
                    isLoggedIn = false
                    showMessage(getString(R.string.oops_its_error))
                }
            })
    }

    protected fun loginWithFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, callbackManager, listOf("public_profile"))
    }

    protected fun loginByGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .requestServerAuthCode(getString(R.string.google_oauth_server_key))
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        googleResultLauncher.launch(signInIntent)
    }

    private var googleResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        } else {
            showMessage(getString(R.string.oops_its_error))
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            loaderDialog.startLoading()
            isLoggedIn = true
            viewModel.loginByGoogle(account.serverAuthCode, getString(R.string.app_locale))
        } catch (e: Exception) {
            loaderDialog.stopLoading()
        }
    }
}