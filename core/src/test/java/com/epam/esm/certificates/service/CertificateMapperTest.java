package com.epam.esm.certificates.service;

import com.epam.esm.certificates.config.TestConfig;
import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import com.epam.esm.certificates.service.dto.CertificateDTO;
import com.epam.esm.certificates.service.mapper.CertificateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("dev")
public class CertificateMapperTest {

    @Qualifier("testMapper")
    @Autowired
    private CertificateMapper mapper;

    private static final CertificateEntity testCertificate = ServiceTestUtil.getCertificates().get(0)
            .setLastUpdateDate(LocalDateTime.parse("2019-02-05T10:00:00"));
    private static final List<TagEntity> testTags = ServiceTestUtil.getTags().subList(0, 4);
    private static final CertificateDTO testCertificateDTO = ServiceTestUtil.getCertificatesDTOs().get(0)
            .setLastUpdateDate("2019-02-05T10:00:00");

    @Test
    public void testToDto_whenSuccessfullyMapped() {
        CertificateDTO actualCertificateDTO = mapper.toDto(testCertificate, testTags);

        assertThat(actualCertificateDTO, is(equalTo(testCertificateDTO)));
    }

    @Test
    public void testToGiftCertificate_whenSuccessfullyMapped() {
        CertificateEntity actualCertificate = mapper.toEntity(testCertificateDTO);

        assertThat(actualCertificate, is(equalTo(testCertificate)));
    }

}
