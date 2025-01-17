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

package org.immutables.criteria.repository.rxjava;

import org.immutables.criteria.backend.Backend;
import org.immutables.criteria.expression.Query;
import org.immutables.criteria.repository.MapperFunction5;
import org.immutables.criteria.repository.Tuple;
import org.immutables.criteria.repository.reactive.ReactiveMapper5;

import java.util.function.Function;

public class RxJavaMapper5<T1, T2, T3, T4, T5> {

  private final ReactiveMapper5<T1, T2, T3, T4, T5> delegate;

  RxJavaMapper5(Query query, Backend.Session session) {
    this.delegate = new ReactiveMapper5<>(query, session);
  }

  public <R> RxJavaFetcher<R> map(MapperFunction5<T1, T2, T3, T4, T5, R> mapFn) {
    return RxJavaFetcherDelegate.fromReactive(delegate.map(mapFn));
  }

  public <R> RxJavaFetcher<R> map(Function<? super Tuple, ? extends R> mapFn) {
    return RxJavaFetcherDelegate.fromReactive(delegate.map(mapFn));
  }

}
