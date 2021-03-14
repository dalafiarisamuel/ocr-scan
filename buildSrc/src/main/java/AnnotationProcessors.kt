object AnnotationProcessors {

    private const val moshiKotlinCodgen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiKotlinCodegen}"
    private const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    private const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"

    val AnnotationProcessorsImplementation = listOf(moshiKotlinCodgen, roomCompiler, hiltCompiler)
}