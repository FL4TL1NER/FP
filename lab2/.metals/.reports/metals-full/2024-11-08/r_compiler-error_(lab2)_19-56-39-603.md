file:///D:/funct/lab2/src/main/scala/Main.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/collection/immutable/Seq.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 583
uri: file:///D:/funct/lab2/src/main/scala/Main.scala
text:
```scala
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.DurationInt
import scala.util.Success
import scala.concurrent.Await

@main def main() =
  print(integrateConcurent((x: Double) => x*2,0,1,1,20))

def integrateConcurent(f: Double=>Double,L: Double,R: Double,I: Int,T: Int): Double = {
  var futures: Seq[Future[Double]] = Seq.empty[Future[Double]]
  val step: Double = (R-L)/T 
  for (i <- Range.inclusive(0,T-1)) {
    futures.appended(Future {integrate(f,L+step*i,L+step*(i+1),I)}(global))
  }
  futures.m@@(Await.result(_,1.seconds))
  return futures.reduce(_ + _)
}

def integrate(f: Double=>Double,L: Double,R: Double,I: Int): Double = {
  val step: Double = (R-L)/I
  val values = Range.inclusive(0,I-1).map(step/2 + step*_).map(f(_)*step)
  return values.reduce(_ + _)
}
```



#### Error stacktrace:

```
java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:204)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:175)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
	java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
	java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:231)
	java.base/java.nio.file.Path.of(Path.java:148)
	java.base/java.nio.file.Paths.get(Paths.java:69)
	scala.meta.io.AbsolutePath$.apply(AbsolutePath.scala:58)
	scala.meta.internal.metals.MetalsSymbolSearch.$anonfun$definitionSourceToplevels$2(MetalsSymbolSearch.scala:70)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.metals.MetalsSymbolSearch.definitionSourceToplevels(MetalsSymbolSearch.scala:69)
	dotty.tools.pc.completions.CaseKeywordCompletion$.dotty$tools$pc$completions$CaseKeywordCompletion$$$sortSubclasses(MatchCaseCompletions.scala:342)
	dotty.tools.pc.completions.CaseKeywordCompletion$.matchContribute(MatchCaseCompletions.scala:292)
	dotty.tools.pc.completions.Completions.advancedCompletions(Completions.scala:350)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:120)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:90)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:146)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/collection/immutable/Seq.scala