package com.epam.esm.certificates.persistence.impl;

import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.config.TestConfig;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("dev")
public class TagRepositoryImplTest {

    private static final List<TagEntity> testTags = getTagsForTest();

    @Qualifier("tagRepositoryTest")
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void whenInjectInMemoryDataSource_getAllTags() {
        List<TagEntity> actualTags = tagRepository.getAll();

        assertEquals(testTags, actualTags);
    }

    @Test
    public void whenInjectInMemoryDataSource_thenReturnCorrectTagById() {

        List<Optional<TagEntity>> tags = new ArrayList<>();

        LongStream.range(1, 13).forEach(i -> tags.add(tagRepository.getById(i)));

        tags.forEach(tag -> assertTrue(tag.isPresent()));
        List<TagEntity> actualTags = tags.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        assertEquals(testTags, actualTags);

    }

    @Test
    public void whenInjectInMemoryDataSource_GetByName_whenNotFound() {
        List<Optional<TagEntity>> actualTags = new ArrayList<>();

        Arrays.asList("tags", "that", "don't", "exist")
                .forEach(name -> actualTags.add(tagRepository.getByName(name)));

        actualTags.forEach(tag -> assertFalse(tag.isPresent()));
    }

    @Test
    public void whenInjectInMemoryDataSource_DeleteTag_whenIdExists() {
        long id = 13;
        long actualResult = tagRepository.delete(id);

        assertEquals(1, actualResult);

        List<TagEntity> expectedTags = new ArrayList<>(testTags);
        expectedTags.remove(id - 1);

        List<TagEntity> actualTags = tagRepository.getAll();
        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void whenInjectInMemoryDataSource_DeleteTag_whenIdNotExist() {
        long id = 55;
        long actualResult = tagRepository.delete(id);

        assertEquals(0, actualResult);
    }

    @Test
    public void whenInjectInMemoryDataSource_GetTagsOfCertificate_whenNoFound() {
        List<TagEntity> actualTags = tagRepository.getTagsOfCertificate(55L);

        assertTrue(actualTags.isEmpty());
    }

    @Test
    public void whenInjectInMemoryDataSource_AddCertificateTag() {
        int result = tagRepository.addCertificateTag(1L, 6L);

        assertEquals(1, result);

        List<TagEntity> actualTags = tagRepository.getTagsOfCertificate(1L);
        List<TagEntity> expectedTags = new ArrayList<>(testTags.subList(0, 4));
        expectedTags.add(new TagEntity().setId(6L).setName("children"));

        assertEquals(expectedTags, actualTags);
    }

    private static List<TagEntity> getTagsForTest() {
        return Arrays.asList(
                new TagEntity().setId(1L).setName("game"),
                new TagEntity().setId(2L).setName("videogame"),
                new TagEntity().setId(3L).setName("games"),
                new TagEntity().setId(4L).setName("playstation"),
                new TagEntity().setId(5L).setName("kids"),
                new TagEntity().setId(6L).setName("children"),
                new TagEntity().setId(7L).setName("kid"),
                new TagEntity().setId(8L).setName("clothes"),
                new TagEntity().setId(9L).setName("beauty"),
                new TagEntity().setId(10L).setName("makeup"),
                new TagEntity().setId(11L).setName("cosmetics"),
                new TagEntity().setId(12L).setName("skincare")
        );
    }

}