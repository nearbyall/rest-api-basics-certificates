package com.epam.esm.certificates.controller;

import com.epam.esm.certificates.controller.exception.BadRequestException;
import com.epam.esm.certificates.controller.exception.ResourceNotFoundException;
import com.epam.esm.certificates.controller.exception.message.ApiStatusCode;
import com.epam.esm.certificates.persistence.sorting.SortColumn;
import com.epam.esm.certificates.persistence.sorting.SortOrder;
import com.epam.esm.certificates.service.CertificateService;
import com.epam.esm.certificates.service.dto.CertificateDTO;
import com.epam.esm.certificates.service.dto.CertificatePatchDTO;
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
 * REST Controller for certificates manipulation that produces JSON objects.
 */
@RestController
@RequestMapping(value = "/gifts", produces = "application/json")
public class CertificateController {

    private CertificateService сertificateService;

    private MessageSource messageSource;

    @Autowired
    public void setСertificateService(CertificateService сertificateService) {
        this.сertificateService = сertificateService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * GET Controller for retrieving certificate DTOs by id.
     *
     * @param id certificate id
     * @return ResponseEntity of certificate or ResponseEntity of api error message if id is not found
     */
    @GetMapping("/certificates/{id}")
    public ResponseEntity<CertificateDTO> getById(@PathVariable long id) {
        Optional<CertificateDTO> certificateDTO = сertificateService.getById(id);

        if (!certificateDTO.isPresent()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notFoundById",
                            null, LocaleContextHolder.getLocale()), id),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(certificateDTO.get(), HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving all existing certificates.
     *
     * @return ResponseEntity of certificates DTOs list or ResponseEntity of api error message if entities were not found.
     */
    @GetMapping("/certificates")
    public ResponseEntity<List<CertificateDTO>> getAll() {
        List<CertificateDTO> сertificateDTOs = сertificateService.getAll();

        if (сertificateDTOs.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("controllerMessage.notFoundPlural", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(сertificateDTOs, HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving all existing certificates in a specific sorted order.
     *
     * @param sort  sort param (name or date)
     * @param order order param (asc or desc)
     * @return ResponseEntity of sorted certificates DTOs list or ResponseEntity of api error message if entities were not found or request params are not valid.
     */
    @GetMapping(value = "/certificates", params = {"sort", "order"})
    public ResponseEntity<List<CertificateDTO>> getAllSorted(@RequestParam SortColumn sort, @RequestParam SortOrder order) {
        List<CertificateDTO> certificateDTOs = сertificateService.getAllSorted(sort, order);

        if (certificateDTOs.isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("controllerMessage.notFoundPlural", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving all existing certificates with specific name.
     *
     * @param name certificate name
     * @return ResponseEntity of certificates DTOs list or ResponseEntity of api error message if entities were not found.
     */
    @GetMapping(value = "/certificates", params = "name")
    public ResponseEntity<List<CertificateDTO>> getByName(@RequestParam String name) {
        List<CertificateDTO> certificateDTOs = сertificateService.getByName(name);

        if (certificateDTOs.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notFoundByName",
                            null, LocaleContextHolder.getLocale()), name),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving all existing certificates with specific tag name.
     *
     * @param tag tag name
     * @return ResponseEntity of gift certificates DTOs list or ResponseEntity of api error message if entities were not found.
     */
    @GetMapping(value = "/certificates", params = "tag")
    public ResponseEntity<List<CertificateDTO>> getByTagName(@RequestParam String tag) {
        List<CertificateDTO> certificateDTOs = сertificateService.getByTagName(tag);

        if (certificateDTOs.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notFoundByTag",
                            null, LocaleContextHolder.getLocale()), tag),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    /**
     * GET Controller for retrieving all existing certificates by searching by name or description.
     *
     * @param search search pattern
     * @return ResponseEntity of certificates DTOs list or ResponseEntity of api error message if entities were not found.
     */
    @GetMapping(value = "/certificates", params = "search")
    public ResponseEntity<List<CertificateDTO>> searchByNameAndDescription(@RequestParam String search) {
        List<CertificateDTO> certificateDTOs = сertificateService.searchByNameOrDescription(search);

        if (certificateDTOs.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(messageSource.getMessage("controllerMessage.notFoundBySearch",
                            null, LocaleContextHolder.getLocale()), search),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(certificateDTOs, HttpStatus.OK);
    }

    /**
     * POST Controller for new certificate creation.
     *
     * @param certificate certificate DTO to create
     * @param bindingResult   contains errors if the requested certificate is invalid.
     * @return ResponseEntity of created certificate or ResponseEntity of
     * api error message if certificate was not created or request certificate contains invalid fields.
     */
    @PostMapping("/certificates")
    public ResponseEntity<CertificateDTO> addNewCertificate
    (@Valid @RequestBody CertificateDTO certificate, BindingResult bindingResult) {
        checkErrors(bindingResult);

        Optional<CertificateDTO> certificateDTO = сertificateService.create(certificate);

        if (!certificateDTO.isPresent()) {
            throw new BadRequestException(
                    messageSource.getMessage("controllerMessage.notAdded", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(certificateDTO.get(), HttpStatus.OK);
    }

    /**
     * PUT Controller for updating existing certificate.
     *
     * @param certificate certificate DTO with fields that need to be updated
     * @param bindingResult   contains errors if the requested certificate is invalid.
     * @return ResponseEntity of updated certificate or ResponseEntity of api error message if certificate was not updated or request certificate contains invalid fields.
     */
    @PutMapping("/certificates")
    public ResponseEntity<CertificateDTO> updateCertificate
    (@RequestBody CertificateDTO certificate, BindingResult bindingResult) {
        checkErrors(bindingResult);

        Optional<CertificateDTO> certificateDTO = сertificateService.update(certificate);

        if (!certificateDTO.isPresent()) {
            throw new BadRequestException(
                    messageSource.getMessage("controllerMessage.notUpdated", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(certificateDTO.get(), HttpStatus.OK);
    }

    /**
     * PATCH Controller for partial updating.
     *
     * @param certificate certificate DTO with fields that need to be updated
     * @param bindingResult contains errors if the requested giftCertificate is invalid.
     * @return ResponseEntity of updated certificate or ResponseEntity of api error message if certificate
     * was not updated or request gift certificate contains invalid fields.
     */
    @PatchMapping("/certificates")
    public ResponseEntity<CertificateDTO> patchCertificate
    (@Valid @RequestBody CertificatePatchDTO certificate, BindingResult bindingResult) {
        checkErrors(bindingResult);

        Optional<CertificateDTO> certificateDTO = сertificateService.patch(certificate);

        if (!certificateDTO.isPresent()) {
            throw new BadRequestException(
                    messageSource.getMessage("controllerMessage.notUpdated", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(certificateDTO.get(), HttpStatus.OK);
    }

    /**
     * DELETE Controller for certificate deletion.
     *
     * @param id certificate id to delete
     * @return ResponseEntity of deleted certificate or ResponseEntity of api error message if certificate was not deleted
     */
    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<CertificateDTO> deleteCertificate(@PathVariable long id) {
        Optional<CertificateDTO> certificateDTO = сertificateService.delete(id);

        if (!certificateDTO.isPresent()) {
            throw new BadRequestException(
                    String.format(messageSource.getMessage("controllerMessage.notDeletedById",
                            null, LocaleContextHolder.getLocale()), id),
                    ApiStatusCode.CERTIFICATE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(certificateDTO.get(), HttpStatus.OK);
    }

    private void checkErrors(BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException(
                    messageSource.getMessage("controllerMessage.invalidData", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
