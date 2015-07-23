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
package com.domingosuarez.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Predicate;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by domix on 23/07/15.
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Logic {
  String message() default "{validation.constraints.Logic.message}";

  String property();

  //Class<? extends Predicate<? extends Object>> condition();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Defines several {@link Logic} annotations on the same element.
   *
   * @see com.domingosuarez.validation.constraints.Logic
   */
  @Target({METHOD, TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Logic[] value();
  }
}
