package com.epam.esm.certificates.converter;

import com.epam.esm.certificates.controller.exception.BadRequestException;
import com.epam.esm.certificates.controller.exception.message.ApiStatusCode;
import com.epam.esm.certificates.persistence.sorting.SortColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;

/**
 * Class is needed to convert the string value of the parameter for sorting
 * into an enumeration object
 * @see com.epam.esm.certificates.persistence.sorting.SortColumn
 */
public class StringToSortColumnConverter implements Converter<String, SortColumn> {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public SortColumn convert(String source) {
        try {
            return SortColumn.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    messageSource.getMessage("controllerMessage.invalidData", null, LocaleContextHolder.getLocale()),
                    ApiStatusCode.CERTIFICATE_BAD_REQUEST,
                    HttpStatus.BAD_REQUEST);
        }
    }

}
