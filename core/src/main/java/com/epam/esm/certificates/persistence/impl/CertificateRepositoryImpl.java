package com.epam.esm.certificates.persistence.impl;

import com.epam.esm.certificates.persistence.CertificateRepository;
import com.epam.esm.certificates.persistence.entity.CertificateEntity;
import com.epam.esm.certificates.persistence.sorting.SortColumn;
import com.epam.esm.certificates.persistence.sorting.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String CREATE = "INSERT INTO certificates " +
            "(name, description, price, duration, create_date, last_update_date) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING certificates.id";

    private static final String GET_ALL = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date " +
            "FROM certificates";

    private static final String GET_BY_ID = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date " +
            "FROM certificates WHERE id = ?";

    private static final String GET_BY_NAME = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date " +
            "FROM certificates WHERE name = ?";

    private static final String UPDATE = "UPDATE " +
            "certificates SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ?" +
            "WHERE id = ?";

    private static final String DELETE = "DELETE FROM certificates WHERE id = ?";

    private static final String GET_BY_TAG = "SELECT " +
            "gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date " +
            "FROM certificates_tags gct JOIN certificates gc on gc.id = gct.certificate_id " +
            "JOIN tags t on t.id = gct.tag_id " +
            "WHERE t.name = ?";

    private static final String GET_ALL_SORTED = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date " +
            "FROM certificates " +
            "ORDER BY %s %s";

    private static final String GET_SEARCH_BY_NAME_OR_DESCRIPTION = "SELECT " +
            "id, name, description, price, duration, create_date, last_update_date " +
            "FROM certificates WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ?";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<CertificateEntity> getByName(String name) {
        return jdbcTemplate.query(GET_BY_NAME, (rs, rowNum) -> getCertificate(rs), name);
    }

    @Override
    public List<CertificateEntity> getByTagName(String tag) {
        return jdbcTemplate.query(GET_BY_TAG, (rs, rowNum) -> getCertificate(rs), tag);
    }

    @Override
    public List<CertificateEntity> getAllSorted(SortColumn sort, SortOrder order) {
        String sql = String.format(GET_ALL_SORTED, sort.getValue(), order.toString());
        return jdbcTemplate.query(sql, (rs, rowNum) -> getCertificate(rs));
    }

    @Override
    public List<CertificateEntity> searchByNameOrDescription(String search) {
        return jdbcTemplate.query(GET_SEARCH_BY_NAME_OR_DESCRIPTION, (rs, rowNum) -> getCertificate(rs),
                "%" + search.toLowerCase(Locale.ROOT) + "%",
                "%" + search.toLowerCase(Locale.ROOT) + "%");
    }

    @Override
    public Optional<Long> create(CertificateEntity certificate) {
        List<Long> returningId = jdbcTemplate.query(CREATE, (rs, rowNum) -> rs.getLong(1),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Timestamp.valueOf(certificate.getCreateDate()),
                Timestamp.valueOf(certificate.getLastUpdateDate())
        );

        return returningId.isEmpty()
                ? Optional.empty()
                : Optional.of(returningId.get(0));
    }

    @Override
    public List<CertificateEntity> getAll() {
        return jdbcTemplate.query(GET_ALL, (rs, rowNum) -> getCertificate(rs));
    }

    @Override
    public Optional<CertificateEntity> getById(Long id) {
        List<CertificateEntity> giftCertificates = jdbcTemplate.query(GET_BY_ID, (rs, rowNum) -> getCertificate(rs), id);

        return giftCertificates.isEmpty()
                ? Optional.empty()
                : Optional.of(giftCertificates.get(0));
    }

    @Override
    public int update(CertificateEntity certificate) {
        return jdbcTemplate.update(UPDATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Timestamp.valueOf(certificate.getCreateDate()),
                Timestamp.valueOf(certificate.getLastUpdateDate()),
                certificate.getId());
    }

    @Override
    public int delete(Long id) {
        return jdbcTemplate.update(DELETE, id);
    }

    private CertificateEntity getCertificate(ResultSet rs) throws SQLException {
        return new CertificateEntity()
                .setId(rs.getLong(1))
                .setName(rs.getString(2))
                .setDescription(rs.getString(3))
                .setPrice(rs.getDouble(4))
                .setDuration(rs.getInt(5))
                .setCreateDate(rs.getTimestamp(6).toLocalDateTime())
                .setLastUpdateDate(rs.getTimestamp(7).toLocalDateTime());
    }


}
