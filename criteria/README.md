The goal of Criteria module is to provide fluent java API (DSL) to query heterogeneous data-sources.

### Features

1. **Expressive and type-safe API** Compile-type validation of the query.
2. **Dynamic** Combine predicates at runtime based on some logic
3. **Data-source agnostic** Define criteria once and apply to different data-sources (Map, JDBC, Mongo, Elastic etc.)
4. **Blocking / asynchronous operations** Generated repositories allow you to query data in blocking, non-blocking and [reactive](https://www.reactive-streams.org/) fashion

### Example

#### Define Model
Define your model using immutables interfaces
```java
@Value.Immutable
@Criteria // generate criteria
@Criteria.Repository // means generate repository (different from @Criteria)
interface Person {
    @Criteria.Id
    String id();
    String fullName();
    Optional<String> nickName();  
    int age();
    List<Pet> pets();
}
```

#### Query
Because of `@Criteria` annotation, `PersonCriteria` class is automatically generated by immutables. You can now leverage it to write queries similar to:
```java
// basic query by id
PersonCriteria.person.id.in("id1", "id2", "id3");
PersonCriteria.person.id.notIn("bad_id");

// query on Strings, Comparables and Optionals
person
    .fullName.is("John") // basic equal
    .fullName.isNot("Mary") // not equal
    .fullName.endsWith("Smith") // string condition
    .fullName.is(3.1415D) // ERROR! will not compile since fullName is String (not double)
    .nickName.isPresent() // for Optional attribute
    .nickName.value().startsWith("Adam") // For Optional<String> attribute
    .pets.notEmpty() // condition on an Iterable
    .active.isTrue() // boolean
    .or() // disjunction (equivalent to logical OR)
    .age.atLeast(21) // comparable attribute
    .or()
    .not(p -> p.nickName.value().hasLength(4)); // negation on a Optional<String> attribute

// apply specific predicate to elements of a collection
person
    .pets.none().type.is(Pet.PetType.iguana)  // no Iguanas
    .or()
    .pets.any().name.contains("fluffy"); // person has a pet which sounds like fluffy

```

You will notice that there are no `and` statements (conjunctions) that is because criteria uses 
[Disjunctive Normal Form](https://en.wikipedia.org/wiki/Disjunctive_normal_form) (in short DNF) by default. 

For more complex expressions, one can still combine criterias arbitrarily using `and`s / `or`s / `not`s. 
Statement like `A and (B or C)` can be written as follows:
```java
person.fullName.isEqualTo("John").and(person.age.isGreaterThan(22).or().nickName.isPresent())
```

Not all entities require repository (`@Criteria.Repository`) but you need to add `@Criteria` to all classes you want to query by. For example, to filter on `Person.pets.name` `Pet` class needs to have `@Criteria` (otherwise `PersonCriteria.pets` will have a very generic Object matcher).

#### Use generated repository to query or update a datasource
`@Criteria.Repository` instructs immutables to generate repository class with `find` / `insert` / `watch` operations. You are required to provide a valid [backend](https://github.com/immutables/immutables/blob/master/criteria/common/src/org/immutables/criteria/backend/Backend.java) 
instance (mongo, elastic, inmemory etc).

```java
MongoCollection<Person> collection = ... // prepare collection with DocumentClass / CodecRegistry
Backend backend = new MongoBackend(collection);

// PersonRepository is automatically generated. You need to provide only backend instance 
PersonRepository repository = new PersonRepository(backend); 

repository.insert(ImmutablePerson.builder().id("aaa").fullName("John Smith").age(22).build());

// query repository
Publisher<Person> result = repository.find(person.fullName.contains("Smith")).fetch();
``` 

### Building blocks (nomenclature)
- **Matcher** Typed predicate on a particular attribute. There are several variations of the matcher and, usually, they're
associated with a type (eg. 
[StringMatcher](https://github.com/immutables/immutables/blob/master/criteria/common/src/org/immutables/criteria/matcher/StringMatcher.java), 
[IterableMatcher](https://github.com/immutables/immutables/blob/master/criteria/common/src/org/immutables/criteria/matcher/IterableMatcher.java), 
[ComparableMatcher](https://github.com/immutables/immutables/blob/master/criteria/common/src/org/immutables/criteria/matcher/ComparableMatcher.java)). 
Matcher internally builds an _Expression_.
- **Expression** Abstraction of a generic expression modeled as 
[Abstract Syntax Tree](https://en.wikipedia.org/wiki/Abstract_syntax_tree). Used internally as Intermediate Representation (IR) 
to transform original expression into a native query of a database. Users rarely have to deal with this API unless
they write adapters for a particular backend.
- **Backend** adapter to a data-source (database). Responsible for interpreting expressions and operations into native
queries and API calls using vendor drivers.  
- **Repository**  User facing API to perform queries, updates, pub/sub or other CRUD operations. Uses _Backend_. 
- **Facet** Property of repository to fine-tune its behaviour. Eg. `Readable` / `Writable` / `Watchable` 
Also one can define return types based on [rxjava](https://github.com/ReactiveX/RxJava) / 
async ([CompletionStage](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html))
or synchronous types. 

### Development 
`common` module contains runtime support. Remaining folders are backend and facet implementation.

This folder contains classes specific to Criteria API and its runtime evaluation:

1. `common` shared classes by all modules
2. `elasticsearch` adapter for [Elastic Search](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
3. `mongo` adapter for [MongoDB](https://www.mongodb.com/) 
based on [reactive streams](https://mongodb.github.io/mongo-java-driver-reactivestreams/) driver.
4. `geode` adapter for [Apache Geode](https://geode.apache.org)
5. `inmemory` lightweight implementation of a backend based on existing Map.
6. `rxjava` [rxjava](https://github.com/ReactiveX/RxJava) repository facets.

Criteria API requires Java 8 (or later)
