package com.epam.esm.certificates.service.mapper;

import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import com.epam.esm.certificates.service.dto.CertificateDTO;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class CertificateMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final NumberFormat NUMBER_FORMATTER =
            new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

    public CertificateDTO toDto(CertificateEntity certificate, List<TagEntity> tags) {
        return new CertificateDTO()
                .setId(certificate.getId())
                .setName(certificate.getName())
                .setDescription(certificate.getDescription())
                .setPrice(NUMBER_FORMATTER.format(certificate.getPrice()))
                .setDuration(certificate.getDuration())
                .setCreateDate(DATE_FORMAT.format(certificate.getCreateDate()))
                .setLastUpdateDate(DATE_FORMAT.format(certificate.getLastUpdateDate()))
                .setTags(tags);
    }

    public CertificateEntity toEntity(CertificateDTO certificateDTO) {
        return new CertificateEntity()
                .setId(certificateDTO.getId())
                .setName(certificateDTO.getName())
                .setDescription(certificateDTO.getDescription())
                .setPrice(Double.parseDouble(certificateDTO.getPrice()))
                .setDuration(certificateDTO.getDuration())
                .setCreateDate(LocalDateTime.parse(certificateDTO.getCreateDate()))
                .setLastUpdateDate(LocalDateTime.parse(certificateDTO.getLastUpdateDate()));
    }

}
