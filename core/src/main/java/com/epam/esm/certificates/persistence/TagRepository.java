package com.epam.esm.certificates.persistence;

import com.epam.esm.certificates.persistence.entity.TagEntity;

import java.util.List;
import java.util.Optional;

/**
 * Implements CrudRepository interface for CRUD operations for TagEntity.
 * @see com.epam.esm.certificates.persistence.CrudRepository
 * @see com.epam.esm.certificates.persistence.entity.TagEntity
 */
public interface TagRepository extends CrudRepository<TagEntity, Long>{

    /**
     * Gets tag entity with the provided name.
     * @param name tag name
     * @return optional tag
     */
    Optional<TagEntity> getByName(String name);

    /**
     * Gets tags of a specific certificate.
     * @param id gift certificate id
     * @return list of tags
     */
    List<TagEntity> getTagsOfCertificate(Long id);

    /**
     * Adds tag to the certificate
     * @param certificateId certificate id
     * @param tagId tag id
     * @return the number of rows affected
     */
    int addCertificateTag(Long certificateId, Long tagId);

    /**
     * Deletes relation of the certificate with tag
     * @param certificateId certificate id
     * @param tagId tag id
     * @return the number of rows affected
     */
    int deleteCertificateTag(Long certificateId, Long tagId);

    /**
     * Gets tag entity that belongs to the specific certificate and has a provided name
     * @param certificateId certificate id
     * @param tagName tag name
     * @return optional tag
     */
    Optional<TagEntity> getCertificateTagByTagName(Long certificateId, String tagName);

}
