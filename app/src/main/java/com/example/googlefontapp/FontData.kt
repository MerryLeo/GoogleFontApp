package com.example.googlefontapp

data class FontWrapper(val kind: String, val items: List<FontData>)

data class FontData(
    val family: String,
    val variants: List<String>?,
    val subsets: List<String>?,
    val version: String?,
    val lastModified: String?,
    val files: Any?,
    val category: String?,
    val kind: String?
)