/*
 * Copyright 2019 Immutables Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.immutables.criteria.typemodel;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.criteria.Criteria;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Defines various types (long/int/boolean) with containers (like {@code int[]} or {@code List<Boolean>})
 */
public interface TypeHolder {

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface BooleanHolder {
    boolean value();
    Boolean boxed();
    @Nullable Boolean nullable();
    Optional<Boolean> optional();
    boolean[] array();
    List<Boolean> list();

  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface ByteHolder {
    byte value();
    Byte boxed();
    @Nullable Byte nullable();
    Optional<Byte> optional();
    byte[] array();
    List<Byte> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface ShortHolder {
    short value();
    Short boxed();
    @Nullable Short nullable();
    Optional<Short> optional();
    short[] array();
    List<Short> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface FloatHolder {
    float value();
    Float boxed();
    @Nullable Float nullable();
    Optional<Float> optional();
    float[] array();
    List<Float> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface DoubleHolder {
    double value();
    Double boxed();
    @Nullable Double nullable();
    OptionalDouble optional();
    Optional<Double> optional2();
    double[] array();
    List<Double> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface IntegerHolder {
    int value();
    Integer boxed();
    @Nullable Integer nullable();
    Optional<Integer> optional2();
    int[] array();
    List<Integer> list();
    OptionalInt optional();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface LongHolder {
    long value();
    Long boxed();
    @Nullable Long nullable();
    OptionalLong optional();
    Optional<Long> optional2();
    long[] array();
    List<Long> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface CharacterHolder {
    char value();
    Character boxed();
    @Nullable Character nullable();
    Optional<Character> optional();
    char[] array();
    List<Character> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface BigIntegerHolder {
    BigInteger value();
    @Nullable BigInteger nullable();
    Optional<BigInteger> optional();
    BigInteger[] array();
    List<BigInteger> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface BigDecimalHolder {
    BigDecimal value();
    @Nullable BigDecimal nullable();
    Optional<BigDecimal> optional();
    BigDecimal[] array();
    List<BigDecimal> list();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  @JsonDeserialize(as = ImmutableStringHolder.class)
  @JsonSerialize(as = ImmutableStringHolder.class)
  @JsonIgnoreProperties(ignoreUnknown = true)
  interface StringHolder {

    @Criteria.Id
    String id();

    String value();
    @Nullable String nullable();
    Optional<String> optional();
    String[] array();
    List<String> list();

    static Supplier<ImmutableStringHolder> generator() {
      AtomicLong counter = new AtomicLong();
      return () -> ImmutableStringHolder.builder().id("id" + counter.incrementAndGet()).value("foo").nullable(null).array("a1", "a2").addList("l1", "l2").build();
    }
  }

  enum Foo {
    ONE, TWO
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface EnumHolder {
    Foo value();
    @Nullable Foo nullable();
    Optional<Foo> optional();
    List<Foo> list();
    Foo[] array();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface DateHolder {
    Date value();
    @Nullable Date nullable();
    Optional<Date> optional();
    List<Date> list();
    Date[] array();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface LocalDateHolder {
    LocalDate value();
    @Nullable LocalDate nullable();
    Optional<LocalDate> optional();
    List<LocalDate> list();
    LocalDate[] array();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface TimeZoneHolder {
    TimeZone value();
    @Nullable TimeZone nullable();
    Optional<TimeZone> optional();
    List<TimeZone> list();
    TimeZone[] array();
  }

  /**
   * Definitions which usually don't make sense
   */
  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface WeirdHolder {
    // weird stuff
    Optional<Optional<String>> weird1();
    Optional<List<String>> weird2();
    List<Optional<String>> weird3();
    Optional<OptionalInt> weird4();
    Optional<Map<String, String>> weird5();
  }

  @Value.Immutable
  @Criteria
  @Criteria.Repository
  interface MapHolder {
    Map<String, String> map();
    Optional<Map<String, String>> optionalMap();
    List<Map<String, String>> maps();
  }
}
