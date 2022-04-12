package com.epam.esm.certificates.service;

import com.epam.esm.certificates.service.dto.TagDTO;

import java.util.List;

/**
 * Implements Service interface for business logic operations of the Tags.
 * @see com.epam.esm.certificates.service.dto.TagDTO
 * @see com.epam.esm.certificates.persistence.entity.TagEntity
 */
public interface TagService extends Service<TagDTO, Long> {

    /**
     * Gets tags of a specific certificate.
     * @param id certificate id
     * @return list of TagDTOs
     */
    List<TagDTO> getTagsOfCertificate(Long id);

}
