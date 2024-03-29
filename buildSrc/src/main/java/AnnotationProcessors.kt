object AnnotationProcessors {

    private const val moshiKotlinCodgen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiKotlinCodegen}"
    private const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    private const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"
    private const val hiltAndroid: String =
        "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    private const val hiltWorkerCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltWorker}"
    private const val sqlLiteJdbc = "org.xerial:sqlite-jdbc:3.36.0"


    val AnnotationProcessorsImplementation =
        listOf(moshiKotlinCodgen, roomCompiler, hiltAndroid, hiltCompiler, hiltWorkerCompiler, sqlLiteJdbc)
}