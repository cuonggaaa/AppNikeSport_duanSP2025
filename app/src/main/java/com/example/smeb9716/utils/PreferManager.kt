package com.example.smeb9716.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import kotlin.reflect.KClass


class PreferManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences? = context.getSharedPreferences("Base_Preferences", Context.MODE_PRIVATE)

    companion object {
        private var preferManager: PreferManager? = null
        fun getInstance(context: Context): PreferManager {
            if (preferManager == null) {
                preferManager = PreferManager(context)
            }

            if (preferManager != null)
                return preferManager!!
            else
                return getInstance(context)
        }
    }

    // write String + Int + Float + Boolean
    fun write(key: String?, value: String?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Boolean) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Int) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Float) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putFloat(key, value)
        prefsEditor.apply()
    }

    // read String + Int + Float + Boolean
    fun readString(key: String?, defValue: String? = null): String? {
        return if (sharedPreferences != null) {
            sharedPreferences.getString(key, defValue)
        } else defValue
    }

    fun readInt(key: String?, defValue: Int): Int {
        return sharedPreferences?.getInt(key, defValue) ?: defValue
    }

    fun readFloat(key: String?, defValue: Float): Float {
        return sharedPreferences?.getFloat(key, defValue) ?: defValue
    }

    fun readBoolean(key: String?, defValue: Boolean ): Boolean {
        return sharedPreferences?.getBoolean(key, defValue) ?: defValue
    }

    // write Object
    // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android (20 vote)
    fun writeObject(key: String?, any: Any){
        val prefsEditor = sharedPreferences!!.edit()
        val gson = Gson()
        val dataJson = gson.toJson(any)
        prefsEditor.putString(key, dataJson)
        prefsEditor.apply()
    }

    // read Object
    fun <T : Any> readObject(key: String?, classOfT: Class<T> ): Any {
        val gson = Gson()
        val any = preferManager!!.readString(key, "")
        return gson.fromJson(any, classOfT)
    }

    // write List<Object>
    // https://stackoverflow.com/questions/28107647/how-to-save-listobject-to-sharedpreferences (58 vote)
    fun <T> writeListObject(key: String?, list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        write(key, json)
    }

    // read List<Object>
    // https://stackoverflow.com/questions/28107647/how-to-save-listobject-to-sharedpreferences (0 vote, index : end)
    fun <T : Serializable> readListObject(key: String, clazz: KClass<T>): List<T> {
        return sharedPreferences.let { prefs ->
            val gson = Gson()
            val data = prefs!!.getString(key, null)
            val type: Type = TypeToken.getParameterized(MutableList::class.java, clazz.java).type
            gson.fromJson(data, type) as MutableList<T>
        }
    }

    // clear all data
    fun clearAllData(){
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.clear().apply()
    }

    // remove data
    fun removeData(key: String?){
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.remove(key).apply()
    }
}