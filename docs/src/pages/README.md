# Scala Template

This is a template for creating projects in the Creative Scala style. The majority of this is a `build.sbt` that sets up:

- a `build` task to do everything (compile, run tests, format, etc.) for day-to-day development.
- documentation created via [mdoc][mdoc] and [Laika][laika], and styled using the [Creative Scala theme][creative-scala-theme].
- setup the [Typelevel sbt plugin][sbt-typelevel] to run tests and so on via Github actions, and publish to Maven when tags like `v0.2` are pushed.


## Getting Started

Clone or fork this repository to create your own project, and then customize it as you need. There are two main customizations you should consider:

- You should definitely change the settings in `project/Settings.scala`
- The build has a single main project called `core`. This is setup as a cross platform project. You can change it to a single platform, if that is appropriate for your project, and remove some complexity.


## Development

Write code and then run `build` in sbt. Watch it do everything (compile code, run tests, format code, check dependencies are up to date, build your documentation, and more!)


## Documentation

Write your documentation in `docs/src/pages`. You'll find this documentation there!

You can write Scala code and use mdoc to evaluate it, as shown below.

```scala mdoc
List(1,2,3).map(_ * 2)
```

You can include images in the usual way. Here is a picture of a chessboard !

![A picture of a red and black chessboard](chessboard.png)

If your documentation needs extended code examples, you can write that code in the `examples` project and pull it into the documentation.

*Examples here*

When working on the documentation, if is often useful to just build the documentation without doing all the other time consuming stuff that `build` does. The sbt command to do this is `docs / tlSite`.

Publishing documentation.

[mdoc]: https://scalameta.org/mdoc/
[laika]: https://typelevel.org/Laika/
[creative-scala-theme]: https://github.com/creativescala/creative-scala-theme/
[sbt-typelevel]: https://typelevel.org/sbt-typelevel/
