/**
 *
 * Copyright (C) 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.domingosuarez.validation.constraints.validators;

import com.domingosuarez.validation.constraints.Constrained;
import com.domingosuarez.validation.constraints.Constraint;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;
import static org.springframework.util.ReflectionUtils.*;

/**
 * Created by domix on 25/07/15.
 */
public class ConstrainedValidator implements ConstraintValidator<Constrained, Object> {

  @Override
  public void initialize(Constrained constraint) {

  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    List<Field> fields = new ArrayList<>();

    doWithFields(value.getClass(), f -> {
      makeAccessible(f);
      fields.add(f);
    }, f -> isPredicate.and(hasConstraintAnnotation).test(f));

    List<ContraintViolation> violations = fields.stream()
      .map((f) -> new Foo(getAnnotation(f, Constraint.class), extractPredicate.apply(f, value), value))
      .map((f) -> findViolation.apply(f))
      .filter((f) -> f != null)
      .collect(toList());

    boolean hasViolations = !violations.isEmpty();

    if (hasViolations) {
      context.disableDefaultConstraintViolation();
      violations.forEach((v) -> context.buildConstraintViolationWithTemplate(v.message)
        .addPropertyNode(v.property).addConstraintViolation());
    }

    return !hasViolations;
  }

  Function<Foo, ContraintViolation> findViolation = (f) -> of(f.predicate.test(f.entity))
    .map((r) -> new ContraintViolation(f.constraint.message(), f.constraint.property()))
    .orElse(null);

  BiPredicate<Foo, Object> ff = (f, o) -> f.predicate.test(o);

  BiFunction<Field, Object, Predicate> extractPredicate = (f, v) -> (Predicate) getField(f, v);

  Predicate<Field> isPredicate = (f) -> f.getType().isAssignableFrom(Predicate.class);

  Predicate<Field> hasConstraintAnnotation = (f) -> getAnnotation(f, Constraint.class) != null;

  @AllArgsConstructor
  static class Foo {
    Constraint constraint;
    Predicate predicate;
    Object entity;
  }

  @AllArgsConstructor
  static class ContraintViolation {
    String message;
    String property;
  }

}
