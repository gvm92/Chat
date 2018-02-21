package com.example.gustavo.chat

class Mensaje {
    var id: String
    var cCodUsu: String
    var texto: String

    init {
        this.id = ""
        this.cCodUsu = ""
        this.texto = ""
    }

    constructor(id: String, cCodUsu: String, texto: String) {
        this.id = id
        this.cCodUsu = cCodUsu
        this.texto = texto
    }

    fun getcCodUsu(): String {
        return cCodUsu
    }

    fun setcCodUsu(cCodUsu: String) {
        this.cCodUsu = cCodUsu
    }

    fun getid(): String {
        return id
    }

    fun setid(id: String) {
        this.id = id
    }

    fun gettexto(): String {
        return texto
    }

    fun settexto(texto: String) {
        this.texto = texto
    }
}