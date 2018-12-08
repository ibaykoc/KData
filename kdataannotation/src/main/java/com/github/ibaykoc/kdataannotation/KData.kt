package com.github.ibaykoc.kdataannotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class KData(val validatedClassName: String) {

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD)
    annotation class ParentField

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD)
    annotation class Field(val validatedFieldName : String, val allowNull : Boolean = false)

}