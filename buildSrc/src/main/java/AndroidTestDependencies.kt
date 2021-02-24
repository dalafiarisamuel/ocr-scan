object AndroidTestDependencies {
    private const val androidxTestExt = "androidx.test.ext:junit-ktx:${Versions.androidxTestExt}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"

    val androidTestImplementation = listOf(androidxTestExt, espressoCore)
}