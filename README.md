# Cagette

Cagette is a quick and dirty data store for prototyping. It allows to focus on designing your domain model, business rules, screens, user interactions, batch, reports, ... without bothering about how your data will eventually be persisted.

> "Cagette saved my hackday" — [Mr. Speaker](http://www.mrspeaker.net/)

## Setting up with sbt

Configure a new resolver:

```scala
resolvers += "Ekito" at "https://raw.github.com/Ekito/maven-repo/master/releases"
```

Add the library dependency:

```scala
libraryDependencies += "guillaume.bort" %% "cagette" % "0.3"
```

## Using – as easy as 1,2,3

### 1. Create your domain class with an `id` field:

Let's try with a `User` class:

```scala
case class User(id: Long, email: String, groups: Seq[String])
```

### 2. Create a `Cageot[DomainType,IdType]`:

We usually name the `Cageot` with the same name as the domain class:

```scala
object User extends cagette.Cageot[User,Long]
```

### 3. Query the cageot using Scala functions

The `Cageot` type provides convenient higher order query functions you can use directly:

#### findAll()

```scala
val allusers: Seq[User] = User.findAll()
```

#### findById(id: IdType)

```scala
val maybeUser: Option[User] = User.findById(88)
```

#### findOneBy(predicate: DomainType => Boolean)

```scala
val maybeUser: Option[User] = User.findOneBy(_.email == "coco@gmail.com")
```

#### findBy(predicate: DomainType => Boolean)

```scala
val adminUsers: Seq[User] = User.findBy(_.groups.contains("admin"))
```

## Setting up initial data set

While defining your `Cageot` you can define the initial data set it will contain:

```scala
object User extends cagette.Cageot[User,Long] {
	
	override def initialData = Seq(
		User(1, "bob@gmail.com", groups = Seq("admin", "user")),
		User(2, "kiki@gmail.com", groups = Seq("user")),
		User(3, "toto@gmail.com", groups = Nil)
	)

}
```

## Updating data

### save will insert a new instance

If you want to store a new instance into your `Cageot`, just use the `save` operation as:

```scala
User.save( 
  User(88, "coco@gmail.com", groups = Seq("user")) 
)
```

### but save will also update an existing instance

If the store already contains an instance with the same __id__, this instance will just be udpated:

```scala
User.findOneBy(_.email == "kiki@gmail.com").map { user =>
	User.save(user.copy(groups = (user.groups :+ "admin").distinct ))
}
```

### then you can delete them later

You can either delete by using the __id__ of the instances you want to remove as:

```scala
User.delete(88)
```

or batch delete using a predicate as:

```scala
User.delete(_.groups.contains("archived"))
```

## How to avoid the evil `id` field

If you don't want to artificially specify an `id` field in your case class, you can still provide your own `Identifier` instance when creating your `Cageot`:

```scala
case class User(email: String, groups: Seq[String])
object User extends Cageot[User,String]()(Identifier(_.email)) // Specify the identifier as `email`
```

## Using with Play framework

You can directly use this for prototyping with http://www.playframework.org. Just add the resolver and library dependency to your `Build.scala` file, and start defining your model. The `initialData` set will be applied automatically at startup and each time your code change.

## Bonus

Check the source code and find secret features like __pagination__ and __autoincrement__ for domain class identifiers.

__Enjoy!__