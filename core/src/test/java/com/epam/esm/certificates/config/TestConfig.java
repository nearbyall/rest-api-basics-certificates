package com.epam.esm.certificates.config;

import com.epam.esm.certificates.persistence.CertificateRepository;
import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.impl.CertificateRepositoryImpl;
import com.epam.esm.certificates.persistence.impl.TagRepositoryImpl;

import com.epam.esm.certificates.service.mapper.CertificateMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
@ComponentScan("com.epam.esm")
public class TestConfig {

    @Bean
    public CertificateRepository certificateRepositoryPersistenceTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("test_schema.sql")
                .addScript("test_data.sql")
                .build();
        CertificateRepository certificateRepository = new CertificateRepositoryImpl(dataSource);
        return certificateRepository;
    }

    @Bean
    public TagRepository tagRepositoryPersistenceTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .build();
        TagRepository tagRepository = new TagRepositoryImpl(dataSource);
        return tagRepository;
    }

    @Bean
    public CertificateMapper testMapper() {
        return Mockito.spy(CertificateMapper.class);
    }

    @Bean
    public TagRepository mockTagRepository() {
        return Mockito.mock(TagRepository.class);
    }

    @Bean
    public Clock testClock() {
        return Clock.fixed(Instant.parse("2021-01-26T10:00:00.00Z"), ZoneId.of("UTC"));
    }

}
