package com.bestDate.presentation.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.model.SocialProvider
import com.bestDate.data.utils.Logger
import com.bestDate.presentation.auth.AuthViewModel
import com.bestDate.view.alerts.LoaderDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

abstract class BaseAuthFragment<VB: ViewBinding>: BaseVMFragment<VB, AuthViewModel>() {
    override val viewModelClass: Class<AuthViewModel> = AuthViewModel::class.java

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loaderDialog: LoaderDialog

    override fun onInit() {
        super.onInit()
        loaderDialog = LoaderDialog(requireActivity())
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            loaderDialog.stopLoading()
            if (it.exception.message.isBlank()) {
                showMessage(getString(R.string.wrong_auth_data))
            } else showMessage(it.exception.message)
        }
    }

    protected fun loginByGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .requestIdToken(getString(R.string.google_oauth_server_key))
            .requestServerAuthCode(getString(R.string.google_oauth_server_key))
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        googleResultLauncher.launch(signInIntent)
    }

    private var googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
            Logger.print("Google access token: ${account.serverAuthCode}")
            viewModel.loginSocial(SocialProvider.GOOGLE, account.serverAuthCode)//"ya29.a0AX9GBdWdmHCpYI0kPIjTYT-6ynTzQ9vRUoBHwqIlQuhjdEQtQsW-i_t0scXeWbbndN5bJ0jfeGx6Y0kiatEEcDs8U1DX0Ld5sVBqOaMDqcISlgs4GmkjC469wLBjN5cboA2V9MdTT0K0pRuHAu-SbcdlR9JuaCgYKAYUSARASFQHUCsbCFuww0unKzsW_YG1_1t0_Vg0163"
        } catch (e: Exception) {
            loaderDialog.stopLoading()
        }
    }


}