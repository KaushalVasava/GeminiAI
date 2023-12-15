package com.lahsuak.apps.geminiai.repo

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import com.lahsuak.apps.geminiai.BuildConfig

class GeminiAIRepoImpl : GeminiAIRepo {
    override fun provideConfig(): GenerationConfig {
        return generationConfig {
            temperature = 0.7f
        }
    }

    override fun getGenerativeModel(
        config: GenerationConfig,
        safetySetting: List<SafetySetting>?,
    ): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.API_KEY,
            generationConfig = config,
            safetySettings = safetySetting,
        )
    }
}
