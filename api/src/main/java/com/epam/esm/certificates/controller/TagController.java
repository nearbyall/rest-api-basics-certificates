package com.epam.esm.certificates.controller;

import com.epam.esm.certificates.controller.exception.BadRequestException;
import com.epam.esm.certificates.controller.exception.ResourceNotFoundException;
import com.epam.esm.certificates.controller.exception.message.ApiStatusCode;
import com.epam.esm.certificates.service.TagService;
import com.epam.esm.certificates.service.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for tags manipulation that produces JSON objects.
 */
@RestController
@RequestMapping("/gifts/certificates")
public class TagController {

    private TagService tagService;

    private MessageSource messageSource;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * GET Controller for retrieving all existing tags.
     *
     * @return ResponseEntity of tags list or ResponseEntity of api error message if tags are not found
     */
    @GetMapping("/tags")
    public ResponseEntity<List<TagDTO>> getAll() {
        List<TagDTO> tags = tagService.getAll();

        if (tags.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("controllerMessage.notFoundPlural", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.TAG_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving tag DTOs by id.
     *
     * @param id tag id
     * @return ResponseEntity of tag or ResponseEntity of api error message if id is not found
     */
    @GetMapping( "/tags/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable long id) {
        Optional<TagDTO> tag = tagService.getById(id);

        if (!tag.isPresent()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notFoundById",
                            null, LocaleContextHolder.getLocale()), id),
                    ApiStatusCode.TAG_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving all tags of the specific certificate.
     *
     * @param id certificate id
     * @return ResponseEntity of tags list or ResponseEntity of api error message if tags are not found
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagDTO>> getCertificateTags(@PathVariable long id) {
        List<TagDTO> tags = tagService.getTagsOfCertificate(id);

        if (tags.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notFoundPluralById",
                            null, LocaleContextHolder.getLocale()), id),
                    ApiStatusCode.TAG_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    /**
     * POST Controller for new tag creation.
     *
     * @param tag tag DTO to create
     * @param bindingResult contains errors if the requested tag is invalid.
     * @return ResponseEntity of tag or ResponseEntity of api error message if tag was not created or request tag contains invalid fields.
     */
    @PostMapping("/tags")
    public ResponseEntity<TagDTO> addNewTag(@Valid @RequestBody TagDTO tag, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    messageSource.getMessage("controllerMessage.invalidData", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.TAG_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<TagDTO> createdTag = tagService.create(tag);
        if (!createdTag.isPresent()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("controllerMessage.notAdded", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.TAG_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(createdTag.get(), HttpStatus.OK);
    }

    /**
     * DELETE Controller for tag deletion.
     *
     * @param id tag id to delete
     * @return ResponseEntity of deleted tag or ResponseEntity of api error message if tag was not deleted
     */
    @DeleteMapping(value = "/tags/{id}")
    public ResponseEntity<TagDTO> deleteTag(@PathVariable long id) {
        Optional<TagDTO> deletedTag = tagService.delete(id);
        if (!deletedTag.isPresent()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notDeletedById",
                            null, LocaleContextHolder.getLocale()), id),
                    ApiStatusCode.TAG_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(deletedTag.get(), HttpStatus.OK);
    }

}
