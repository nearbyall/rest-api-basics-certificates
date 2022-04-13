package com.epam.esm.certificates.persistence.impl;

import com.epam.esm.certificates.persistence.TagRepository;
import com.epam.esm.certificates.persistence.entity.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String CREATE = "INSERT INTO tags (name) VALUES (?) RETURNING tags.id";
    private static final String GET_ALL = "SELECT id, name FROM tags";
    private static final String GET_BY_ID = "SELECT id, name FROM tags WHERE id = ?";
    private static final String GET_BY_NAME = "SELECT id, name FROM tags WHERE name = ?";
    private static final String DELETE = "DELETE FROM tags WHERE id = ?";

    private static final String GET_GIFT_CERTIFICATE_TAGS = "SELECT tags.id, tags.name " +
            "FROM certificates_tags JOIN tags ON certificates_tags.tag_id = tags.id " +
            "WHERE certificate_id = ?";

    private static final String ADD_GIFT_CERTIFICATE_TAG = "INSERT INTO certificates_tags " +
            "(certificate_id, tag_id) VALUES (?, ?)";

    private static final String DELETE_GIFT_CERTIFICATE_TAG = "DELETE FROM certificates_tags " +
            "WHERE certificate_id = ? AND tag_id = ?";

    private static final String GET_GIFT_CERTIFICATE_TAG_BY_TAG_NAME = "SELECT t.id, t.name " +
            "FROM certificates_tags " +
            "JOIN tags t on t.id = certificates_tags.tag_id " +
            "JOIN certificates c on certificates_tags.certificate_id = c.id " +
            "WHERE c.id = ? AND t.name = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Long> create(TagEntity tag) {
        List<Long> returningId = jdbcTemplate.query(CREATE, (rs, rowNum) -> rs.getLong(1), tag.getName());

        return returningId.isEmpty()
                ? Optional.empty()
                : Optional.of(returningId.get(0).longValue());
    }

    @Override
    public List<TagEntity> getAll() {
        return jdbcTemplate.query(GET_ALL, (rs, rowNum) -> getTag(rs));
    }

    @Override
    public Optional<TagEntity> getById(Long id) {
        List<TagEntity> tag = jdbcTemplate.query(GET_BY_ID, (rs, rowNum) -> getTag(rs), id);

        return tag.isEmpty()
                ? Optional.empty()
                : Optional.of(tag.get(0));
    }

    @Override
    public int update(TagEntity tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Long id) {
        return jdbcTemplate.update(DELETE, id);
    }

    @Override
    public Optional<TagEntity> getByName(String name) {
        List<TagEntity> tags = jdbcTemplate.query(GET_BY_NAME, (rs, rowNum) -> getTag(rs), name);

        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));
    }

    @Override
    public List<TagEntity> getTagsOfCertificate(Long id) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_TAGS, (rs, rowNum) -> getTag(rs), id);
    }

    @Override
    public int addCertificateTag(Long certificateId, Long tagId) {
        return jdbcTemplate.update(ADD_GIFT_CERTIFICATE_TAG, certificateId, tagId);
    }

    @Override
    public int deleteCertificateTag(Long certificateId, Long tagId) {
        return jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_TAG, certificateId, tagId);
    }

    @Override
    public Optional<TagEntity> getCertificateTagByTagName(Long certificateId, String tagName) {
        List<TagEntity> tags = jdbcTemplate
                .query(GET_GIFT_CERTIFICATE_TAG_BY_TAG_NAME, (rs, rowNum) -> getTag(rs), certificateId, tagName);

        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));

    }

    private TagEntity getTag(ResultSet rs) throws SQLException {
        return new TagEntity()
                .setId(rs.getLong(1))
                .setName(rs.getString(2));
    }

}
