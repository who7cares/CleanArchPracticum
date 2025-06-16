package com.example.apitest2.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.apitest2.data.StorageClient
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val context: Context,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {

    private val prefs: SharedPreferences = context.getSharedPreferences("MOVIES_SEARCH", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun storageData(data: T) {
        prefs.edit().putString(dataKey, gson.toJson(data, type)).apply()
    }

    override fun getData(): T? {
        val dataJson = prefs.getString(dataKey, null)
        return if (dataJson == null) {
            null
        } else {
            gson.fromJson(dataJson, type)
        }
    }


}


/*
в Kotlin и Java нет возможности выяснить конкретный класс,
который будет указан в качестве обобщённого типа (T). Но
нам нужно каким-то образом сообщить gson о том, объекты
какого типа мы будем конвертировать в JSON и обратно.
Для этого передадим в конструкторе экземпляр Type - специальный интерфейс,
предназначенный для хранения информации о типе данных.

Получить объект, реализующий такой интерфейс таким образом,
чтобы он содержал информацию о нужном нам типе данных,
можно с использованием TypeToken:

object : TypeToken<History>() {}.type

Инициализация PrefsStorageClient будет выглядеть вот так:

PrefsStorageClient<ArrayList<Movie>>(
        context,
        "HISTORY",
        object : TypeToken<ArrayList<Movie>>() {}.type)

 */