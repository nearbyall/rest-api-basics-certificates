package com.epam.esm.certificates.service;

import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import com.epam.esm.certificates.service.dto.CertificateDTO;
import com.epam.esm.certificates.service.dto.TagDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ServiceTestUtil {

    public static List<CertificateDTO> getCertificatesDTOs() {
        return Arrays.asList(
                new CertificateDTO()
                        .setId(1L)
                        .setName("Game Store")
                        .setDescription("Gift certificate for purchase in the game store")
                        .setPrice("50.00")
                        .setDuration(30)
                        .setCreateDate("2019-01-26T10:00:00")
                        .setLastUpdateDate("2019-01-26T10:00:00")
                        .setTags(getTags().subList(0, 4)),
                new CertificateDTO()
                        .setId(2L)
                        .setName("Kids Clothes")
                        .setDescription("Gift certificate for kids clothing purchasing")
                        .setPrice("40.00")
                        .setDuration(45)
                        .setCreateDate("2020-01-26T10:00:00")
                        .setLastUpdateDate("2020-01-26T10:00:00")
                        .setTags(getTags().subList(4, 8)),
                new CertificateDTO()
                        .setId(3L)
                        .setName("Beauty Store")
                        .setDescription("Gift certificate for purchase in the beauty store")
                        .setPrice("60.00")
                        .setDuration(60)
                        .setCreateDate("2022-01-26T10:00:00")
                        .setLastUpdateDate("2022-01-26T10:00:00")
                        .setTags(getTags().subList(8, 12))
        );
    }

    public static List<TagEntity> getTags() {
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

    public static List<TagDTO> getTagsDTOs() {
        return Arrays.asList(
                new TagDTO().setName("game"),
                new TagDTO().setName("videogame"),
                new TagDTO().setName("games"),
                new TagDTO().setName("playstation"),
                new TagDTO().setName("kids"),
                new TagDTO().setName("children"),
                new TagDTO().setName("kid"),
                new TagDTO().setName("clothes"),
                new TagDTO().setName("beauty"),
                new TagDTO().setName("makeup"),
                new TagDTO().setName("cosmetics"),
                new TagDTO().setName("skincare")
        );
    }

    public static List<CertificateEntity> getCertificates() {
        return Arrays.asList(
                new CertificateEntity()
                        .setId(1L)
                        .setName("Game Store")
                        .setDescription("Gift certificate for purchase in the game store")
                        .setPrice(50.0)
                        .setDuration(30)
                        .setCreateDate(LocalDateTime.parse("2019-01-26T10:00:00"))
                        .setLastUpdateDate(LocalDateTime.parse("2019-01-26T10:00:00")),
                new CertificateEntity()
                        .setId(2L)
                        .setName("Kids Clothes")
                        .setDescription("Gift certificate for kids clothing purchasing")
                        .setPrice(40.0)
                        .setDuration(45)
                        .setCreateDate(LocalDateTime.parse("2020-01-26T10:00:00"))
                        .setLastUpdateDate(LocalDateTime.parse("2020-01-26T10:00:00")),
                new CertificateEntity()
                        .setId(3L)
                        .setName("Beauty Store")
                        .setDescription("Gift certificate for purchase in the beauty store")
                        .setPrice(60.0)
                        .setDuration(60)
                        .setCreateDate(LocalDateTime.parse("2022-01-26T10:00:00"))
                        .setLastUpdateDate(LocalDateTime.parse("2022-01-26T10:00:00"))
        );
    }

    public static CertificateDTO getCertificateCreationTestDTO() {
        return new CertificateDTO()
                .setId(4L)
                .setName("Test Gift")
                .setDescription("Test Gift certificate for purchasing")
                .setPrice("52.30")
                .setDuration(30)
                .setCreateDate("2021-01-26T10:00:00")
                .setLastUpdateDate("2021-01-26T10:00:00")
                .setTags(new ArrayList<>(Arrays.asList(
                        new TagEntity().setId(1L).setName("game"),
                        new TagEntity().setId(13L).setName("test")))
                );
    }

    public static CertificateEntity getCertificateCreationTest() {
        return new CertificateEntity()
                .setId(4L)
                .setName("Test Gift")
                .setDescription("Test Gift certificate for purchasing")
                .setPrice(52.3)
                .setDuration(30)
                .setCreateDate(LocalDateTime.parse("2021-01-26T10:00:00"))
                .setLastUpdateDate(LocalDateTime.parse("2021-01-26T10:00:00"));
    }

}
