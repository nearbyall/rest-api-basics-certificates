package com.epam.esm.certificates.service;

import com.epam.esm.certificates.persistence.sorting.SortColumn;
import com.epam.esm.certificates.persistence.sorting.SortOrder;
import com.epam.esm.certificates.service.dto.CertificateDTO;
import com.epam.esm.certificates.service.dto.CertificatePatchDTO;

import java.util.List;
import java.util.Optional;

/**
 * Implements Service interface for business logic operations of the Certificates.
 * @see com.epam.esm.certificates.service.dto.CertificateDTO
 * @see com.epam.esm.certificates.persistence.entity.CertificateEntity
 */
public interface CertificateService extends Service<CertificateDTO, Long> {

    /**
     * Gets all existing objects of certificates DTOs sorted.
     * @param sort sort table (name or date)
     * @param order sort order (asc or desc)
     * @return list of sorted certificates DTOs
     */
    List<CertificateDTO> getAllSorted(SortColumn sort, SortOrder order);

    /**
     * Gets all existing objects with the provided certificate name.
     * @param name certificate name
     * @return list of certificate DTOs
     */
    List<CertificateDTO> getByName(String name);

    /**
     * Gets all existing objects with the provided tag name.
     * @param tag tag name
     * @return list of certificate DTOs
     */
    List<CertificateDTO> getByTagName(String tag);

    /**
     * Updates certificate and its tags.
     * Can delete tags corresponding to that gift certificate.
     * All fields of the GiftCertificateDTO must be provided.
     * @param certificate certificate DTO with fields that needed to be updated
     * @return optional DTO of the updated certificate
     */
    Optional<CertificateDTO> update(CertificateDTO certificate);

    /**
     * Patch method for certificate and its tags.
     * Fields could be provided partially.
     * @param certificate
     * @return optional DTO of the updated certificate
     */
    Optional<CertificateDTO> patch(CertificatePatchDTO certificate);

    /**
     * Gets all existing certificates by searching by name or description.
     * @param search search pattern
     * @return list of resulted certificates
     */
    List<CertificateDTO> searchByNameOrDescription(String search);

}
