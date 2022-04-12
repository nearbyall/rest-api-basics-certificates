package com.epam.esm.certificates.service;

import java.util.List;
import java.util.Optional;

/**
 * Service interface that delegates work to repository classes and performs business logic.
 * @param <T> type of the DTO object
 * @param <Id> object id
 */
public interface Service<T, Id> {

    /**
     * Gets all existing objects with provided type.
     * @return list of objects
     */
    List<T> getAll();

    /**
     *  Gets object with the provided id.
     * @param id object id
     * @return optional object
     */
    Optional<T> getById(Id id);

    /**
     * Creates a new instance of an entity.
     * @param obj Object to create
     * @return optional created object
     */
    Optional<T> create(T obj);

    /**
     * Deletes object with the provided id.
     * @param id object id
     * @return optional deleted object
     */
    Optional<T> delete(Id id);

}
