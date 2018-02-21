package com.example.gustavo.chat

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONArray
import org.json.JSONException
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import java.util.ArrayList
import android.content.Intent
import android.R.id.edit
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

    val LOGINACTIVITY = 1

    var IP_Server = "192.168.1.169";
    var urlconsulta = "http://$IP_Server/kotlinapp/Conectar.php";
    var urlinsertar = "http://$IP_Server/kotlinapp/add.php";
    var arrayMensaje: ArrayList<Mensaje>? = null
    var usuario : String = ""
    var texto : String = ""
    var recView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    val devuelveJSON = DevuelveJSON()
    val insertar = Insertar()
    var jSONArray: JSONArray? = null
    var tf : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recView = findViewById<View>(R.id.reyclerview_message_list) as RecyclerView
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recView?.setLayoutManager(layoutManager)
        tf = findViewById<View>(R.id.edTextoEnviar) as EditText
        urlconsulta = "http://$IP_Server/kotlinapp/Conectar.php";
        urlinsertar = "http://$IP_Server/kotlinapp/add.php";

        val i = Intent(this, LoginActivity::class.java)
        startActivityForResult(i, LOGINACTIVITY)

        arrayMensaje = ArrayList<Mensaje>()
        rellenarRecView()
        ListaMensajes().execute()
    }

    inner class ListaMensajes : AsyncTask<String, String, JSONArray>() {

        val str: String? = urlconsulta
        override fun doInBackground(vararg params: String?): JSONArray? {
            try {
                var parametrosPost = HashMap<String, String>()
                parametrosPost.put("ins_sql", "SELECT * FROM chat")
                jSONArray = devuelveJSON.sendRequest(str,
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
                for (i in 0..json.length() -1) {
                    try {
                        val jsonObject = json.getJSONObject(i)
                        val id = jsonObject.getString("id")
                        val cCodUsu = jsonObject.getString("idusuario")
                        val texto = jsonObject.getString("texto")
                        val mensaje = Mensaje(id, cCodUsu, texto)
                        arrayMensaje?.add(mensaje)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                rellenarRecView()
            } else {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    inner class EnviarMensaje : AsyncTask<String, String, Boolean>() {

        val str: String = urlconsulta
        var res = true

        override fun doInBackground(vararg params: String?): Boolean? {
            try {
                var parametrosPost = HashMap<String, String>()
                parametrosPost.put("ins_sql", texto)
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
            } else {
                Toast.makeText(applicationContext, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
            }
            texto=""
            refreshRecView()
        }
    }

    fun rellenarRecView() {
        val adaptador = AdaptadorMensaje(arrayMensaje, usuario)
        recView?.setAdapter(adaptador)
        recView?.postDelayed(Runnable { recView?.scrollToPosition(recView!!.getAdapter().itemCount - 1) }, 100)
    }

    fun refreshRecView(){
        arrayMensaje = ArrayList<Mensaje>()
        ListaMensajes().execute()
    }

    fun enviarMensaje(v : View){
        if(tf!!.text.toString().trim()!=""){
            texto = "INSERT INTO chat VALUES(null,'$usuario', '"+tf?.text.toString().trim()+"')"
            tf?.setText("")
            EnviarMensaje().execute()
        }
    }

    fun cerrarSesion() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        editor.putString("usuario", "")
        editor.putString("pass", "")
        editor.commit()
        val i = Intent(this, LoginActivity::class.java)
        startActivityForResult(i, LOGINACTIVITY)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.cerrarSesion -> {
                cerrarSesion()
                return true
            }
            R.id.info -> {
                Toast.makeText(applicationContext, "App creada por Gustavo", Toast.LENGTH_LONG).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == LOGINACTIVITY) {
            val t = data.extras!!.get("resultado")!!.toString()
            if (t == "true") {
                val usu = data.extras!!.get("cCodUsu")!!.toString()
                if (usu != "") {
                    usuario = usu
                }
                refreshRecView()
            } else
                finish()
        }
    }
}
