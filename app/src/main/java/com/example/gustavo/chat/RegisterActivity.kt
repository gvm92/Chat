package com.example.gustavo.chat

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.json.JSONArray
import android.widget.EditText
import android.widget.Toast


class RegisterActivity : AppCompatActivity() {

    var IP_Server = "192.168.1.169";
    var urlconsulta = "http://$IP_Server/kotlinapp/Conectar.php";
    var urlinsertar = "http://$IP_Server/kotlinapp/add.php";
    var devuelveJSON = DevuelveJSON()
    val insertar = Insertar()
    var jSONArray: JSONArray? = null
    var edUsu: EditText? = null
    var edPass:EditText? = null
    var edPass2:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        urlconsulta = "http://$IP_Server/kotlinapp/Conectar.php";
        urlinsertar = "http://$IP_Server/kotlinapp/add.php";
        edUsu = findViewById<View>(R.id.edUsuReg) as EditText
        edPass = findViewById<View>(R.id.edPassReg1) as EditText
        edPass2 = findViewById<View>(R.id.edPassReg2) as EditText
        devuelveJSON = DevuelveJSON()
    }

    fun registrarse(v: View) {
        val usu = edUsu?.getText().toString().trim()
        val pass = edPass?.getText().toString().trim()
        val pass2 = edPass2?.getText().toString().trim()
        if (usu != "" && pass != "" && pass2 != "") {
            if (pass == pass2)
                ComprobarUsuario().execute(usu, pass)
            else
                Toast.makeText(this@RegisterActivity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this@RegisterActivity, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
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
            if (json != null) {
                if (json.length() == 0) {
                    RegistrarUsuario().execute(usu, pass)
                } else {
                    Toast.makeText(applicationContext, "Nombre de usuario ya en uso", Toast.LENGTH_LONG).show()
                }
            }
            else
                Toast.makeText(applicationContext, "No se ha podido conectar con el servidor", Toast.LENGTH_LONG).show()
        }
    }

    inner class RegistrarUsuario : AsyncTask<String, String, Boolean>() {

        val str: String = urlconsulta
        var res = true
        var usu: String? = ""
        var pass: String? = ""

        override fun doInBackground(vararg params: String?): Boolean? {
            usu = params[0]
            pass = params[1]
            try {
                var parametrosPost = HashMap<String, String>()
                parametrosPost.put("ins_sql", "INSERT INTO usuarios VALUES('$usu', '$pass')")
                res = insertar.sendRequest(str,
                        parametrosPost)
                if (res) {
                    return res
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(res: Boolean?) {
            if (res!!) {
                Toast.makeText(applicationContext, "Registrado con exito", Toast.LENGTH_SHORT).show()
                onBackPressed()
            } else {
                Toast.makeText(applicationContext, "Error al registrarse", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cancelar(v: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        finish()
    }
}
