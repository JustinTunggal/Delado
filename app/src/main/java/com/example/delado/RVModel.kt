package com.example.delado

class RVModel {
    var namakegiatan: String? = null
    var deadline: String? = null
    var desc: String? = null

    internal constructor()
    constructor(namakegiatan: String?, deadline: String?, desc: String?) {
        this.namakegiatan = namakegiatan
        this.deadline = deadline
        this.desc = desc
    }

    fun setNamakegiatan() {
        namakegiatan = namakegiatan
    }

    fun setDeadline() {
        deadline = deadline
    }

    fun setDesc() {
        desc = desc
    }
}