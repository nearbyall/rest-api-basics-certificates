package com.epam.esm.certificates.service.impl;

import com.epam.esm.certificates.persistence.CertificateRepository;
import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import com.epam.esm.certificates.persistence.sorting.SortColumn;
import com.epam.esm.certificates.persistence.sorting.SortOrder;
import com.epam.esm.certificates.service.CertificateService;
import com.epam.esm.certificates.service.aspect.Loggable;
import com.epam.esm.certificates.service.dto.CertificateDTO;
import com.epam.esm.certificates.service.dto.CertificatePatchDTO;
import com.epam.esm.certificates.service.mapper.CertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    private final TagRepository tagRepository;
    private CertificateMapper mapper;
    private Clock clock;

    @Autowired
    public CertificateServiceImpl( @Qualifier("certificateRepositoryImpl") CertificateRepository certificateRepository,
                                   @Qualifier("tagRepositoryImpl") TagRepository tagRepository) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setMapper(CertificateMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Override
    public List<CertificateDTO> getAll() {
        List<CertificateEntity> certificates = certificateRepository.getAll();

        return getCertificateDTOsList(certificates);
    }

    @Override
    public Optional<CertificateDTO> getById(Long id) {
        Optional<CertificateEntity> certificate = certificateRepository.getById(id);

        return certificate.map(certificateToDto ->
                mapper.toDto(certificateToDto, tagRepository.getTagsOfCertificate(certificateToDto.getId())));
    }

    @Override
    public List<CertificateDTO> getByName(String name) {
        List<CertificateEntity> certificates = certificateRepository.getByName(name);

        return getCertificateDTOsList(certificates);
    }

    @Override
    public List<CertificateDTO> getAllSorted(SortColumn sort, SortOrder order) {
        List<CertificateEntity> certificates = certificateRepository.getAllSorted(sort, order);

        return getCertificateDTOsList(certificates);
    }

    @Override
    public List<CertificateDTO> getByTagName(String tag) {
        List<CertificateEntity> certificates = certificateRepository.getByTagName(tag);

        return getCertificateDTOsList(certificates);
    }

    @Override
    @Loggable
    @Transactional(rollbackFor = Exception.class)
    public Optional<CertificateDTO> create(CertificateDTO certificate) {
        certificate.setCreateDate(LocalDateTime.now(clock).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        certificate.setLastUpdateDate(LocalDateTime.now(clock).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        //add to certificates
        Optional<Long> certificateId = certificateRepository.create(mapper.toEntity(certificate));

        if (!certificateId.isPresent()) {
            return Optional.empty();
        }
        //add to tag non-existing tags
        certificate.getTags().stream()
                .filter(tag -> !tagRepository.getByName(tag.getName()).isPresent())
                .forEach(tagRepository::create);
        //add to certificates_tags (m2m)
        certificate.getTags().stream()
                .map(tag -> tagRepository.getByName(tag.getName()))
                .filter(Optional::isPresent)
                .forEach(tag -> tagRepository.addCertificateTag(certificateId.get(), tag.get().getId()));

        return getById(certificateId.get());
    }

    @Override
    @Loggable
    @Transactional(rollbackFor = Exception.class)
    public Optional<CertificateDTO> update(CertificateDTO certificate) {
        Optional<CertificateEntity> certificateOptional = certificateRepository.getById(certificate.getId());
        Long id = certificate.getId();
        if (!certificateOptional.isPresent()) {
            return Optional.empty();
        }
        //update certificates
        int result = certificateRepository
                .update(mapper.toEntity(certificate).setLastUpdateDate(LocalDateTime.now()));

        if (result == 0) {
            return Optional.empty();
        }
        //update tags if new were found
        List<TagEntity> tags = certificate.getTags();
        List<TagEntity> newTags = tags.stream()
                .filter(tag -> !tagRepository.getCertificateTagByTagName(id, tag.getName()).isPresent())
                .collect(Collectors.toList());
        newTags.stream()
                .filter(tag -> !tagRepository.getByName(tag.getName()).isPresent())
                .forEach(tagRepository::create);

        //add new tags (tag) and connect tags to corresponding certificate (certificates_tags)
        newTags.stream()
                .map(tag -> tagRepository.getByName(tag.getName()))
                .filter(Optional::isPresent)
                .forEach(tag -> tagRepository.addCertificateTag(id, tag.get().getId()));

        //if tag from existing tags doesn't exist in new tags -> delete
        List<String> existingTags = tagRepository.getTagsOfCertificate(id).stream()
                .map(TagEntity::getName).collect(Collectors.toList());
        List<String> updateTags = tags.stream()
                .map(TagEntity::getName).collect(Collectors.toList());

        existingTags.stream()
                .filter(tag -> !updateTags.contains(tag))
                .map(tagRepository::getByName)
                .filter(Optional::isPresent)
                .forEach(tag -> tagRepository.deleteCertificateTag(id, tag.get().getId()));

        return getById(certificate.getId());
    }

    @Override
    @Loggable
    @Transactional(rollbackFor = Exception.class)
    public Optional<CertificateDTO> patch(CertificatePatchDTO certificate) {
        Long id = certificate.getId();

        Optional<CertificateEntity> certificateOptional = certificateRepository.getById(id);

        if (!certificateOptional.isPresent()) {
            return Optional.empty();
        }

        List<TagEntity> existingTags = tagRepository.getTagsOfCertificate(id);
        CertificateDTO certificateUpdateDTO = mapper.toDto(certificateOptional.get(), existingTags);

        String name = certificate.getName();
        String description = certificate.getDescription();
        String price = certificate.getPrice();
        int duration = certificate.getDuration();
        String createDate = certificate.getCreateDate();
        List<TagEntity> tags = certificate.getTags();

        if (name != null) {
            certificateUpdateDTO.setName(certificate.getName());
        }
        if (description != null) {
            certificateUpdateDTO.setDescription(certificate.getDescription());
        }
        if (price != null) {
            certificateUpdateDTO.setPrice(certificate.getPrice());
        }
        if (duration != 0) {
            certificateUpdateDTO.setDuration(certificate.getDuration());
        }
        if (createDate != null) {
            certificateUpdateDTO.setCreateDate(certificate.getCreateDate());
        }
        if (!tags.isEmpty()) {
            List<TagEntity> tagsToPatch = certificate.getTags().stream()
                    .filter(tag -> !tagRepository.getCertificateTagByTagName(id, tag.getName()).isPresent())
                    .collect(Collectors.toList());
            tagsToPatch.addAll(certificateUpdateDTO.getTags());
            certificateUpdateDTO.setTags(tagsToPatch);
        }

        return update(certificateUpdateDTO);
    }

    @Override
    @Loggable
    @Transactional(rollbackFor = Exception.class)
    public Optional<CertificateDTO> delete(Long id) {
        Optional<CertificateEntity> certificate = certificateRepository.getById(id);

        if (!certificate.isPresent()) {
            return Optional.empty();
        }

        Optional<CertificateDTO> certificateDTO = getById(id);

        int result = certificateRepository.delete(id);

        if (result == 0) {
            return Optional.empty();
        }

        List<TagEntity> tags = tagRepository.getTagsOfCertificate(id);
        tags.forEach(tag -> tagRepository.deleteCertificateTag(id, tag.getId()));

        return certificateDTO;
    }

    @Override
    public List<CertificateDTO> searchByNameOrDescription(String search) {
        List<CertificateEntity> certificates = certificateRepository.searchByNameOrDescription(search);

        return getCertificateDTOsList(certificates);
    }

    private List<CertificateDTO> getCertificateDTOsList(List<CertificateEntity> certificates) {
        return certificates.stream()
                .map(giftCertificate ->
                        mapper.toDto(giftCertificate, tagRepository.getTagsOfCertificate(giftCertificate.getId())))
                .collect(Collectors.toList());
    }

}
