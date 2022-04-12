package com.epam.esm.certificates.persistence.sorting;

/**
 * Enum for certificates soring by column (name or description)
 * @see com.epam.esm.certificates.persistence.CertificateRepository#getAllSorted(SortColumn, SortOrder)
 */
public enum SortColumn {

    NAME("name"), DATE("create_date");

    private final String value;

    SortColumn(String sortTable) {
        this.value = sortTable;
    }

    public String getValue() {
        return value;
    }

}
