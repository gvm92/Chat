package com.example.gustavo.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.CheckBox
import android.preference.PreferenceManager
import android.os.AsyncTask
import android.widget.Toast
import org.json.JSONArray

class LoginActivity : AppCompatActivity() {

    var IP_Server = "192.168.1.169";
    var urlconsulta = "http://$IP_Server/kotlinapp/Conectar.php";
    var correcto = false
    var guardarUsu = false
    var usuario : String = ""
    var edUsu: EditText? = null
    var edPass: EditText? = null
    var ch: CheckBox? = null
    var devuelveJSON: DevuelveJSON? = null
    var jSONArray: JSONArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        urlconsulta = "http://$IP_Server/kotlinapp/Conectar.php";

        edUsu= findViewById<View>(R.id.edUsuLog) as EditText
        edPass= findViewById<View>(R.id.edPassLog) as EditText
        ch= findViewById<View>(R.id.chRecordarDatos) as CheckBox
        devuelveJSON = DevuelveJSON()
        autologin()
    }

    fun cancelar(v: View){
        onBackPressed()
    }

    fun login(v: View){
        val usu = edUsu?.getText().toString()
        val pass = edPass?.getText().toString()
        if (ch!!.isChecked) {
            guardarUsu = true
        } else
            guardarUsu = false
        ComprobarUsuario().execute(usu, pass)
    }

    fun autologin() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val usu = prefs.getString("usuario", "")
        val pass = prefs.getString("pass", "")
        edUsu?.setText(usu)
        edPass?.setText(pass)
        if (usu != null && pass != null)
            if (usu != "" && pass != "") {
                ch?.setChecked(true)
                ComprobarUsuario().execute(usu, pass)
            }
    }

    fun registrarse(v: View){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    inner class ComprobarUsuario : AsyncTask<String, String, JSONArray>() {

        val str: String? = urlconsulta
        var usu: String? = ""
        var pass: String? = ""

        override fun doInBackground(vararg params: String?): JSONArray? {
            usu = params[0]
            pass = params[1]
            try {
                var parametrosPost = HashMap<String, String>()
                parametrosPost.put("ins_sql", "SELECT * FROM usuarios WHERE cCodUsu='$usu' AND cPasUsu='$pass'")
                jSONArray = devuelveJSON?.sendRequest(str,
                        parametrosPost)
                if (jSONArray != null) {
                    return jSONArray
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(json: JSONArray?) {
            if (json?.length()==0) {
                correcto=false
            } else {
                if (guardarUsu) {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    val editor = prefs.edit()
                    editor.putString("usuario", usu)
                    editor.putString("pass", pass)
                    editor.commit()
                }
                correcto = true
                usuario = usu.toString()
                onBackPressed()
                Toast.makeText(applicationContext, "Usuario correcto", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra("resultado", correcto)
        data.putExtra("cCodUsu", usuario)
        setResult(RESULT_OK, data)
        finish()
    }
}
