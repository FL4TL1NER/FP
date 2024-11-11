file:///D:/funct/lab2/src/main/scala/Main.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/util/Try.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 2178
uri: file:///D:/funct/lab2/src/main/scala/Main.scala
text:
```scala
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.Await
import scala.util.Try

@main def main() = {
  printf(integrateConcurent((x: Double) => x*2,0,1,10,10).toString())
  printf("\n")
}
  
//дописал после лабы
def integrateConcurent(f: Double=>Double,L: Double,R: Double,I: Int,T: Int): Double = {
  val step: Double = (R-L)/T
  val futures: Seq[Future[Double]] = Range(0,T).map((i: Int) => Future {integrate(f,L+step*i,L+step*(i+1),I)})
  val future: Future[Seq[Double]] = Future.sequence(futures)
  val res = Await.result(future,1.second)
  return res.fold(0.0)(_ + _)
}

def integrate(f: Double=>Double,L: Double,R: Double,I: Int): Double = {
  val step: Double = (R-L)/I
  val values = Range(0,I).map(L + step/2 + step*_).map(f(_)*step)
  return values.reduce(_ + _)
}

val LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"
val UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
val NUMBERS_LETTERS = "1234567890"

def hasNSymbols(a:String,set:String, n:Int): Boolean = a.filter(set.contains(_)).length() >= n

def goodEnoughPassword(password:String): Boolean = {
 var reqs: Seq[Boolean] = Seq(password.length() >= 8,
                              hasNSymbols(password,LOWERCASE_LETTERS,1),
                              hasNSymbols(password,UPPERCASE_LETTERS,1),
                              hasNSymbols(password,NUMBERS_LETTERS,1))
 return reqs.reduce(_ && _)
}

def goodEnoughPassword_v2(password:String): Either[Boolean, String] = {
  var reqs: Map[String, (String)=>Boolean] = Map("Password len is < 8"                 -> (_.length() >= 8),
                                                 "Password must have lowercase letter" -> (hasNSymbols(_,LOWERCASE_LETTERS,1)),
                                                 "Password must have uppercase letter" -> (hasNSymbols(_,UPPERCASE_LETTERS,1)),
                                                 "Password must have number"           -> (hasNSymbols(_,NUMBERS_LETTERS,1)))
  for ((err,func) <- reqs) {
    val temp: Try[Boolean] = Try(func(password))
    temp match@@
  }
  return Left(true)
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

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/util/Try.scala