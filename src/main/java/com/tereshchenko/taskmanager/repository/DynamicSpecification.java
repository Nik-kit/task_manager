package com.tereshchenko.taskmanager.repository;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

public class DynamicSpecification {

    private static final Logger log = LoggerFactory.getLogger(DynamicSpecification.class);

    public static <T> Specification<T> filterByEntity(T filterObject, String... ignoredField) {

        Set<String> ignoredFields = new HashSet<>();

        for (String field : ignoredField) {

            ignoredFields.add(field.trim());
        }

        return(root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            for(Field field : filterObject.getClass().getDeclaredFields()){

                field.setAccessible(true);

                try{
                    Object value = field.get(filterObject);

                    if(ignoredFields.contains(field.getName())) continue;

                    if(value != null) {

                        String fieldName = field.getName();

                        Class<?> fieldType = field.getType();

                        if (fieldType.equals(String.class)) {

                            predicates.add(cb.like(cb.lower(root.get(fieldName)), "%" + value.toString().toLowerCase() + "%"));

                        } else if (fieldType.equals(LocalDate.class)) {

                            LocalDate dateValue = (LocalDate) value;

                            predicates.add(cb.equal(cb.function("DATE", LocalDate.class, root.get(fieldName)), dateValue));

                        } else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {

                            try {
                                Field idField = Arrays.stream(value.getClass().getDeclaredFields())
                                        .filter(f -> f.getName().equalsIgnoreCase("id"))
                                        .findFirst()
                                        .orElseThrow(() -> new NoSuchFieldException("ID field not found in " + value.getClass()));

                                idField.setAccessible(true);

                                Object idValue = idField.get(value);

                                if (idValue != null) {

                                    Join<Object, Object> join = root.join(fieldName);

                                    predicates.add(cb.equal(join.get("id"), idValue));
                                }

                            } catch (NoSuchFieldException | IllegalAccessException e) {

                                log.warn("Error processing field '{}' when filtering: {}", fieldName, e.getMessage());
                            }
                        } else {

                            predicates.add(cb.equal(root.get(fieldName), value));
                        }
                    }
                } catch (IllegalAccessException e){

                    log.warn("Error processing field '{}' when filtering: {}", field, e.getMessage());
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}