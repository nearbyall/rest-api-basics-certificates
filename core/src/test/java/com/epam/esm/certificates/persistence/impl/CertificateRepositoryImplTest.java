package com.epam.esm.certificates.persistence.impl;

import com.epam.esm.certificates.persistence.CertificateRepository;
import com.epam.esm.certificates.persistence.config.TestConfig;
import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.sorting.SortColumn;
import com.epam.esm.certificates.persistence.sorting.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("dev")
public class CertificateRepositoryImplTest {

    private static final List<CertificateEntity> testCertificates = getCertificatesForTest();

    @Qualifier("certificateRepositoryTest")
    @Autowired
    private CertificateRepository certificateRepository;

    @Test
    public void testGetAll() {
        List<CertificateEntity> actualCertificates = certificateRepository.getAll();

        assertEquals(testCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetById_whenFound() {
        List<Optional<CertificateEntity>> Certificates = new ArrayList<>();

        LongStream.range(1, 4).forEach(i -> Certificates.add(certificateRepository.getById(i)));

        Certificates.forEach(Certificate -> assertTrue(Certificate.isPresent()));

        List<CertificateEntity> actualCertificates = Certificates.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        assertEquals(testCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetById_whenNotFound() {
        List<Optional<CertificateEntity>> actualCertificates = new ArrayList<>();

        LongStream.range(4, 7).forEach(i -> actualCertificates.add(certificateRepository.getById(i)));

        actualCertificates.forEach(certificate -> assertFalse(certificate.isPresent()));
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetByName_whenFound() {
        List<List<CertificateEntity>> certificates = new ArrayList<>();
        List<CertificateEntity> expectedCertificates = testCertificates.subList(0, 3);

        List<CertificateEntity> actualCertificates = new ArrayList<>();
        Arrays.asList("Game Store", "Kids Clothes", "Beauty Store")
                .forEach(name -> certificates.add(certificateRepository.getByName(name)));

        certificates.forEach(Certificate -> assertEquals(1, Certificate.size()));
        certificates.forEach(Certificate -> actualCertificates.add(Certificate.get(0)));

        assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetByName_whenNotFound() {
        List<CertificateEntity> actualCertificates =
                certificateRepository.getByName("Certificate that doesn't exist");
        assertTrue(actualCertificates.isEmpty());
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetByTagName_whenFound() {
        List<CertificateEntity> actualCertificates = certificateRepository.getByTagName("kid");
        CertificateEntity expectedCertificate = testCertificates.get(1);

        assertEquals(1, actualCertificates.size());
        assertEquals(expectedCertificate, actualCertificates.get(0));
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetByTagName_whenNotFound() {
        List<CertificateEntity> actualCertificates = certificateRepository.getByTagName("tag123");

        assertTrue(actualCertificates.isEmpty());
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetAllSorted_whenSortByNameAndOrderASC() {
        List<CertificateEntity> actualCertificates = certificateRepository.getAllSorted(SortColumn.NAME, SortOrder.ASC);

        List<CertificateEntity> expectedCertificates = new ArrayList<>(testCertificates);
        expectedCertificates.sort(Comparator.comparing(CertificateEntity::getName));

        assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetAllSorted_whenSortByNameAndOrderDESC() {
        List<CertificateEntity> actualCertificates = certificateRepository.getAllSorted(SortColumn.NAME, SortOrder.DESC);

        List<CertificateEntity> expectedCertificates = new ArrayList<>(testCertificates);
        expectedCertificates.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));

        assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetAllSorted_whenSortByDateAndOrderASC() {
        List<CertificateEntity> actualCertificates = certificateRepository.getAllSorted(SortColumn.DATE, SortOrder.ASC);

        List<CertificateEntity> expectedCertificates = new ArrayList<>(testCertificates);
        expectedCertificates.sort(Comparator.comparing(CertificateEntity::getCreateDate));

        assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testGetAllSorted_whenSortByDateAndOrderDESC() {
        List<CertificateEntity> actualCertificates = certificateRepository.getAllSorted(SortColumn.DATE, SortOrder.DESC);

        List<CertificateEntity> expectedCertificates = new ArrayList<>(testCertificates);
        expectedCertificates.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));

        assertEquals(expectedCertificates, actualCertificates);
    }

    @Test
    public void whenInjectInMemoryDataSource_testSearchByNameOrDescription() {
        List<CertificateEntity> actualCertificatesByName = certificateRepository
                .searchByNameOrDescription("sTOre");

        List<CertificateEntity> actualCertificatesByDescription = certificateRepository
                .searchByNameOrDescription("purchAse");

        List<CertificateEntity> expectedCertificates = new ArrayList<>();
        expectedCertificates.add(testCertificates.get(0));
        expectedCertificates.add(testCertificates.get(2));

        assertEquals(expectedCertificates, actualCertificatesByName);
        assertEquals(expectedCertificates, actualCertificatesByDescription);
    }

    private static List<CertificateEntity> getCertificatesForTest() {
        return Arrays.asList(
                new CertificateEntity()
                        .setId(1L)
                        .setName("Game Store")
                        .setDescription("Gift certificate for purchase in the game store")
                        .setPrice(50.0)
                        .setDuration(30)
                        .setCreateDate(LocalDateTime.parse("2021-01-26T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .setLastUpdateDate(LocalDateTime.parse("2021-01-26T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                new CertificateEntity()
                        .setId(2L)
                        .setName("Kids Clothes")
                        .setDescription("Gift certificate for kids clothing purchasing")
                        .setPrice(40.0)
                        .setDuration(45)
                        .setCreateDate(LocalDateTime.parse("2021-01-26T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .setLastUpdateDate(LocalDateTime.parse("2021-01-26T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                new CertificateEntity()
                        .setId(3L)
                        .setName("Beauty Store")
                        .setDescription("Gift certificate for purchase in the beauty store")
                        .setPrice(60.0)
                        .setDuration(60)
                        .setCreateDate(LocalDateTime.parse("2021-01-26T10:00:00"))
                        .setLastUpdateDate(LocalDateTime.parse("2021-01-26T10:00:00"))
        );
    }

}
