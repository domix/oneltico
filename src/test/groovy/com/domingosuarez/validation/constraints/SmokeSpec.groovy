/*
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
package com.domingosuarez.validation.constraints

import spock.lang.Specification

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

/**
 * Created by domix on 26/07/15.
 */
class SmokeSpec extends Specification {
  def foo() {
    setup:
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
      Validator validator = factory.getValidator()
    when:
      Person domix = new Person(name: '')
      def constraintViolations = validator.validate(domix)
    then:
      constraintViolations.size() == 2
    when:
      Dog firulais = new Dog(name: 'toby')
      constraintViolations = validator.validate(firulais)
    then:
      constraintViolations.size() == 0

    when:
      firulais = new Dog(name: 'firulais')
      constraintViolations = validator.validate(firulais)
    then:
      constraintViolations.size() == 1
  }
}
