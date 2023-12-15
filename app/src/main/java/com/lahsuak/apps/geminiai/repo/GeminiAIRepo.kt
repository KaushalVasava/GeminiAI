package com.lahsuak.apps.geminiai.repo

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.SafetySetting

interface GeminiAIRepo {
    fun provideConfig(): GenerationConfig

    fun getGenerativeModel(
        config: GenerationConfig,
        setting: List<SafetySetting>? = null,
    ): GenerativeModel
}