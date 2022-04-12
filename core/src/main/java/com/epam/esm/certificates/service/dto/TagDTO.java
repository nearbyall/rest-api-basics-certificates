package com.epam.esm.certificates.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO class of the tag
 * @see com.epam.esm.certificates.persistence.entity.TagEntity
 */
public class TagDTO {

    @NotNull
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public TagDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDTO tagDTO = (TagDTO) o;

        return name != null ? name.equals(tagDTO.name) : tagDTO.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " {" +
                "name='" + name + '\'' +
                '}';
    }
}

