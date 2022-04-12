package com.epam.esm.certificates.persistence;

import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.sorting.SortColumn;
import com.epam.esm.certificates.persistence.sorting.SortOrder;

import java.util.List;

/**
 * Implements CrudRepository interface for CRUD operations for CertificateEntity.
 * @see com.epam.esm.certificates.persistence.CrudRepository
 * @see com.epam.esm.certificates.persistence.entity.CertificateEntity
 */

public interface CertificateRepository extends CrudRepository<CertificateEntity, Long> {


    /**
     * Gets all existing entities with the provided name.
     * @param name certificate name
     * @return list of certificates with the provided name
     */
    List<CertificateEntity> getByName(String name);

    /**
     * Gets all existing entities with the provided tag name.
     * @param tag tag name
     * @return ist of certificates with the provided tag name
     */
    List<CertificateEntity> getByTagName(String tag);

    /**
     * Gets all existing entities of certificates sorted.
     * @param sort sort table (name or date)
     * @param order sort order (asc or desc)
     * @return list of sorted certificates
     */
    List<CertificateEntity> getAllSorted(SortColumn sort, SortOrder order);

    /**
     * Gets all existing certificates by searching by name or description
     * @param search search pattern
     * @return list of resulted certificates
     */
    List<CertificateEntity> searchByNameOrDescription(String search);

}
