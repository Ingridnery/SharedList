package br.edu.scl.ifsp.inery.sharedlist.view


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.inery.sharedlist.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : BaseActivity() {
    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var gsarl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        alb.createAccountBt.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        alb.passwordResetBt.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        alb.loginBt.setOnClickListener {
            val email = alb.emailEt.text.toString()
            val password = alb.passwordEt.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Usuário $email autenticado com sucesso.",
                    Toast.LENGTH_SHORT
                ).show()
                openMainActivity()
            }.addOnFailureListener {
                Toast.makeText(this, "Falha na autenticação do usuário.", Toast.LENGTH_SHORT).show()
            }
        }

        alb.loginGoogleBt.setOnClickListener {
            val gsa: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
            if (gsa == null) {
                gsarl.launch(googleSignInClient.signInIntent)
            }
            else {
                openMainActivity()
            }
        }

        gsarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val gsa: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(gsa.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Usuário ${gsa.email} autenticado com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                    openMainActivity()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha na autenticação do usuário.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        // Após sair da main activity, finaliza o aplicativo (sai da login activity)
        finish()
    }

}