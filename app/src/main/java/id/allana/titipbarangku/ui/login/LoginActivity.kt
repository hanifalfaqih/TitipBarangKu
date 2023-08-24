package id.allana.titipbarangku.ui.login

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.allana.titipbarangku.R
import id.allana.titipbarangku.databinding.ActivityLoginBinding
import id.allana.titipbarangku.ui.homepage.HomepageViewModel
import id.allana.titipbarangku.util.navigateToMainScreen

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }

    private val viewModel: HomepageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Change Sign In Button Text
        val textSignInButton = binding.signInButton.getChildAt(0) as TextView
        textSignInButton.text = getString(R.string.login_with_google)
        binding.signInButton.setOnClickListener {
            Log.d(TAG, "BUTTON SIGN IN: CLICKED!")
            signIn()
        }

        // Configure Google Sign In
        signInClient = Identity.getSignInClient(this)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Display One-Tap Sign In if user logout from Homepage (MainActivity)
        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        }
    }

    private fun signIn() {
        Log.d(TAG, "FIRST FUNCTION")
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "GoogleSignIn: FAILED", e)
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        Log.d(TAG, "SECOND FUNCTION")
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't Start Sign In: ${e.localizedMessage}")
        }
    }

    private fun handleSignInResult(data: Intent?) {
        Log.d(TAG, "THIRD FUNCTION")
        // Result return from launching the SignIn Pending Intent
        try {
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "FirebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                Log.d(TAG, "No ID Token!")
            }
        } catch (e: ApiException) {
            Log.w(TAG, "Google Sign In FAILED!", e)
            handleNavigateToMain(null)
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "FOURTH FUNCTION")
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "SignInWithCredential: SUCCESS")
                    val user = auth.currentUser
                    handleNavigateToMain(user)
                } else {
                    Log.w(TAG, "SignInWithCredential: FAILURE", task.exception)
                    Snackbar.make(binding.root.rootView, "Authentication Failed", 300).show()
                    handleNavigateToMain(null)
                }
                hideProgressBar()
            }
    }

    private fun handleNavigateToMain(user: FirebaseUser?) {
        Log.d(TAG, "FIFTH FUNCTION")
        hideProgressBar()
        if (user != null) {
            Log.d(TAG, "INSERT AUTH UID")
            viewModel.setAuthUser(user.uid, this)
            navigateToMainScreen(this@LoginActivity)
        }
    }

    private fun oneTapSignIn() {
        // Configure One Tap UI
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            ).build()

        // Display One Tap UI
        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener {}
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "LOGIN ACTIVITY"
    }
}