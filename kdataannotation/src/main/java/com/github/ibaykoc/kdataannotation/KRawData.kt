package com.github.ibaykoc.kdataannotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KRawData(val validatedClassName: String) {

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD)
    annotation class ValidateParentField

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD)
    annotation class ValidateField(val validatedFieldName : String)

}