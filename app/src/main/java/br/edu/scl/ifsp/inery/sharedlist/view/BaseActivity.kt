package br.edu.scl.ifsp.inery.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.inery.sharedlist.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BaseActivity: AppCompatActivity() {
    protected val EXTRA_TASK = "ToDo"
    protected val EXTRA_VIEW_TASK = "ViewToDo"

    protected val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    protected val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(this, googleSignInOptions)
    }

}