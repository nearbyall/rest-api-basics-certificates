package com.epam.esm.certificates.service;

import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import com.epam.esm.certificates.service.dto.TagDTO;
import com.epam.esm.certificates.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    private TagService tagService;
    @Mock
    private TagRepository tagRepository;

    private final static List<TagEntity> testTags = ServiceTestUtil.getTags();
    private final static List<TagDTO> testTagsDTOs = ServiceTestUtil.getTagsDTOs();

    @BeforeEach
    public void before() {
        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    public void testGetAll_shouldDelegateGettingToRepositoryIfTagsExist() {
        when(tagRepository.getAll()).thenReturn(testTags);

        List<TagDTO> actualTags = tagService.getAll();

        verify(tagRepository, times(1)).getAll();
        verifyNoMoreInteractions(tagRepository);

        assertThat(actualTags.size(), is(12));
        assertThat(actualTags, is(equalTo(testTagsDTOs)));
    }


    @Test
    public void testGetById_shouldDelegateGettingToRepositoryIfTagFoundById() {
        long id = 1;
        when(tagRepository.getById(id)).thenReturn(Optional.of(testTags.get(0)));

        Optional<TagDTO> actualTag = tagService.getById(id);

        verify(tagRepository, times(1)).getById(id);
        verifyNoMoreInteractions(tagRepository);

        assertTrue(actualTag.isPresent());
        assertThat(actualTag.get().getName(), is("game"));
    }

    @Test
    public void testCreate_shouldDelegateCreationToRepositoryIfCreationIsSuccessful() {
        String tagName = "test";
        long tagId = 13;
        TagEntity tag = new TagEntity().setName(tagName);

        TagDTO createdTag = new TagDTO().setName(tagName);

        when(tagRepository.getByName(tagName)).thenReturn(Optional.empty());
        when(tagRepository.create(tag)).thenReturn(Optional.of(tagId));
        when(tagRepository.getById(tagId)).thenReturn(Optional.of(new TagEntity().setId(tagId).setName(tagName)));

        Optional<TagDTO> actualTag = tagService.create(createdTag);

        verify(tagRepository, times(1)).getByName(tagName);
        verify(tagRepository, times(1)).create(tag);
        verify(tagRepository, times(1)).getById(tagId);
        verifyNoMoreInteractions(tagRepository);

        assertTrue(actualTag.isPresent());
        assertThat(actualTag.get().getName(), is("test"));
    }

    @Test
    public void testDelete_shouldDelegateDeletionToRepositoryIfTagFoundById() {
        long id = 1;

        when(tagRepository.getById(id)).thenReturn(Optional.of(testTags.get(0)));
        when(tagRepository.delete(id)).thenReturn((int) id);

        Optional<TagDTO> actualTag = tagService.delete(id);

        verify(tagRepository, times(1)).getById(id);
        verify(tagRepository, times(1)).delete(id);
        verifyNoMoreInteractions(tagRepository);

        assertTrue(actualTag.isPresent());
        assertThat(actualTag.get().getName(), is("game"));
    }

}
