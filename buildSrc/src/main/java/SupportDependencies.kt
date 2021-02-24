object SupportDependencies {

    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    private const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"

    val supportImplementation = listOf(appcompat, constraintlayout, materialDesign)
}