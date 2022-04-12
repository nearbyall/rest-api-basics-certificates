package com.epam.esm.certificates.persistence.entity;

/**
 * Entity describing the Tag in the database
 */
public class TagEntity {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public TagEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagEntity tagEntity = (TagEntity) o;

        if (id != null ? !id.equals(tagEntity.id) : tagEntity.id != null) return false;
        return name != null ? name.equals(tagEntity.name) : tagEntity.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
