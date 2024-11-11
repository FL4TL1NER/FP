file:///D:/funct/lab2/src/main/scala/Main.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/collection/immutable/Seq.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 310
uri: file:///D:/funct/lab2/src/main/scala/Main.scala
text:
```scala
import scala.io.StdIn.readLine

@main def main() =
  println("div = ")
  //val div = Int(readLine())
  println(divide(div))



def integrate(f: Double=>Double,L: Double,R: Double,I: Int): Double = {
  val step: Double = (R-L)/I
  val rangeIncl = Range.inclusive(1,I).map(a: Int => step/2 + step*a).m@@
  rangeIncl = 
}
/*
def newInt(a: Int): Int =
  if(a%2 == 0) return a*2 else return a/2

def divide(a:Int):Try[Int] = Try{
  1/a
}
def newCollection(a: Seq[Int]): Seq[Int] = 
  return a.map(newInt)*/
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