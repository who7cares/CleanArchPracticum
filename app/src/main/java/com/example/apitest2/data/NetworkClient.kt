package com.example.apitest2.data

import com.example.apitest2.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response // лучше dto тут переименовать в request
}

