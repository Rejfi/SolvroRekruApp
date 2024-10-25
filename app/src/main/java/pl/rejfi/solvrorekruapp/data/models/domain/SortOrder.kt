package pl.rejfi.solvrorekruapp.data.models.domain

enum class SortOrder(val value: String) {
    ID_ASC("+id"),
    ID_DESC("-id"),
    NAME_ASC("+name"),
    NAME_DESC("-name"),
    INSTRUCTIONS_ASC("+instructions"),
    INSTRUCTIONS_DESC("-instructions"),
    ALCOHOLIC_ASC("+alcoholic"),
    ALCOHOLIC_DESC("-alcoholic"),
    CATEGORY_ASC("+category"),
    CATEGORY_DESC("-category"),
    GLASS_ASC("+glass"),
    GLASS_DESC("-glass"),
    CREATED_AT_ASC("+createdAt"),
    CREATED_AT_DESC("-createdAt"),
    UPDATED_AT_ASC("+updatedAt"),
    UPDATED_AT_DESC("-updatedAt");
}