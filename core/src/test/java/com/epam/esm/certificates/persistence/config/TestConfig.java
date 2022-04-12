package com.epam.esm.certificates.persistence.config;

import com.epam.esm.certificates.persistence.CertificateRepository;
import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.impl.CertificateRepositoryImpl;
import com.epam.esm.certificates.persistence.impl.TagRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
public class TestConfig {

    @Bean
    public CertificateRepository certificateRepositoryTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("test_schema.sql")
                .addScript("test_data.sql")
                .build();
        CertificateRepository certificateRepository = new CertificateRepositoryImpl(dataSource);
        return certificateRepository;
    }

    @Bean
    public TagRepository tagRepositoryTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .build();
        TagRepository tagRepository = new TagRepositoryImpl(dataSource);
        return tagRepository;
    }

}
