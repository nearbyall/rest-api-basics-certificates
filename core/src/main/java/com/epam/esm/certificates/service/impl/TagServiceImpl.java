package com.epam.esm.certificates.service.impl;

import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import com.epam.esm.certificates.service.TagService;
import com.epam.esm.certificates.service.aspect.Loggable;
import com.epam.esm.certificates.service.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {


    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(@Qualifier("tagRepositoryImpl") TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDTO> getAll() {
        List<TagEntity> tags = tagRepository.getAll();

        return tags.stream()
                .map(tag -> new TagDTO().setName(tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDTO> getById(Long id) {
        Optional<TagEntity> tag = tagRepository.getById(id);

        return tag.map(value -> new TagDTO().setName(value.getName()));
    }

    @Override
    @Loggable
    public Optional<TagDTO> create(TagDTO tagDTO) {
        Optional<TagEntity> existingTag = tagRepository.getByName(tagDTO.getName());

        if (existingTag.isPresent()) {
            return Optional.empty();
        }

        Optional<Long> tagId = tagRepository.create(new TagEntity().setName(tagDTO.getName()));

        if (!tagId.isPresent()) {
            return Optional.empty();
        }

        return getById(tagId.get());
    }

    @Override
    @Loggable
    public Optional<TagDTO> delete(Long id) {
        Optional<TagEntity> deletedTag = tagRepository.getById(id);

        if (!deletedTag.isPresent()) {
            return Optional.empty();
        }

        int success = tagRepository.delete(deletedTag.get().getId());

        return success == 1
                ? Optional.of(new TagDTO().setName(deletedTag.get().getName()))
                : Optional.empty();
    }

    @Override
    public List<TagDTO> getTagsOfCertificate(Long id) {
        List<TagEntity> tags = tagRepository.getTagsOfCertificate(id);

        return tags.stream()
                .map(tag -> new TagDTO().setName(tag.getName()))
                .collect(Collectors.toList());
    }

}
