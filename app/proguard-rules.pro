# Retrofit service definitions are inspected at runtime.
-keep,allowobfuscation,allowshrinking interface com.dishcovery.app.OshxonaApi

# Moshi uses Kotlin reflection to read these API response models.
-keep class com.dishcovery.app.RecipeShort { *; }
-keep class com.dishcovery.app.RecipeFull { *; }
-keep class com.dishcovery.app.Ingredient { *; }
-keep class com.dishcovery.app.RecipeStep { *; }
-keep class com.dishcovery.app.RecipePage { *; }
-keep class com.dishcovery.app.SearchResponse { *; }
