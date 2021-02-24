object AnnotationProcessors {

    private const val moshiKotlinCodgen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiKotlinCodegen}"
    private const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    val AnnotationProcessorsImplementation = listOf(moshiKotlinCodgen, roomCompiler)
}