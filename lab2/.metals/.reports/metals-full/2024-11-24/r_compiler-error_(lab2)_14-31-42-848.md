file:///D:/funct/lab2/src/main/scala/Main.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/util/Try.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 2785
uri: file:///D:/funct/lab2/src/main/scala/Main.scala
text:
```scala
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global // без .Implicits ломается
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.Duration
import scala.concurrent.Await

import scala.util.Try
import scala.util.Success
import scala.util.Failure

import scala.io.StdIn.readLine

@main def main() = {
  var a = readPassword()
  Await.ready(a,Duration.Inf)
}

// 1. 
def integrate(f: Double=>Double, l: Double, r: Double, i: Int): Double = {
  val step: Double = (r-l)/i
  val values = Range(0,i).map(l + step/2 + step*_).map(f(_)*step)
  return values.reduce(_ + _)
}

//дописал после лабы
def integrateConcurent(f: Double=>Double, l: Double, r: Double, i: Int, t: Int): Double = {
  val step: Double = (r-l)/t
  val futures: Seq[Future[Double]] = Range(0,t).map((i: Int) => Future {integrate(f,l+step*i,l+step*(i+1),i)})
  val future: Future[Seq[Double]] = Future.sequence(futures)
  val res = Await.result(future,1.second)
  return res.fold(0.0)(_ + _)
}

// 2.

// def goodEnoughPassword(password:String): Boolean
// Option

val LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"
val UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
val NUMBERS_LETTERS = "1234567890"
val SPECIAL_LETTERS = "!@#$%^&*()[]{};:,./<>?|"

def hasNSymbols(passwordCandidate: String, someLetters: String, n:Int): Boolean = passwordCandidate.count(someLetters.contains(_)) >= n

// Не понимаю зачем тут Option, и без него элегантное решение
def goodEnoughPassword(password:String): Boolean = {
 var reqs: Seq[Boolean] = Seq(password.length() >= 8,
                              hasNSymbols(password,LOWERCASE_LETTERS,1),
                              hasNSymbols(password,UPPERCASE_LETTERS,1),
                              hasNSymbols(password,NUMBERS_LETTERS,1),
                              hasNSymbols(password,SPECIAL_LETTERS,1))
 return reqs.reduce(_ && _)
}

// def goodEnoughPassword(password:String):Either[Boolean, String]
// Try

def goodEnoughPassword_v2(password:String): Either[Boolean, String] = {
  var reqs: Seq[(String, String => Boolean)] = Seq("Password len is < 8"                 -> (_.length() >= 8),
                                                   "Password must have lowercase letter" -> (hasNSymbols(_,LOWERCASE_LETTERS,1)),
                                                   "Password must have uppercase letter" -> (hasNSymbols(_,UPPERCASE_LETTERS,1)),
                                                   "Password must have number"           -> (hasNSymbols(_,NUMBERS_LETTERS,1)),
                                                   "Password must have special symbol"   -> (hasNSymbols(_,SPECIAL_LETTERS,1)))
  

  Try(reqs.map((a,b)=>b(password))) m@@
  /*
  var reqs: Seq[(String, Try[Boolean])] = Seq("Password len is < 8"                 -> Try(password.length() >= 8),
                                              "Password must have lowercase letter" -> Try(hasNSymbols(password,LOWERCASE_LETTERS,1)),
                                              "Password must have uppercase letter" -> Try(hasNSymbols(password,UPPERCASE_LETTERS,1)),
                                              "Password must have number"           -> Try(hasNSymbols(password,NUMBERS_LETTERS,1)),
                                              "Password must have special symbol"   -> Try(hasNSymbols(password,SPECIAL_LETTERS,1)))

  if reqs.exists((a,b) => b match {case Failure(exception) => true //check for exeptions
                                   case Success(value) => false})
    then return Left(false)
  
  val errors = reqs.filter((a,b) => b match {case Failure(exception) => false
                                             case Success(value) => !value})
                   .map((a,b)=>a)
                   .fold("")(_ + " " + _)
  
  errors match
        case "" => Left(true)
        case a => Right(a)
  */
}

// def readPassword(): Future[String]
// Future

def readPassword(): Future[String] = {
  var res = Future({
    printf("\nPlease enter password: ")
    var input: String = readLine()
    var isValid = goodEnoughPassword_v2(input)
    while isValid.fold((fa: Boolean) => !fa,(fb: String) => true) do {
      isValid match
        case Left(value)  => printf("unknown error")
        case Right(value) => printf(value)
      
      printf("\nPlease enter better password: ")
      input = readLine()
      isValid = goodEnoughPassword_v2(input)
    }
    input
  })
  res.onComplete((a: Try[String]) => a match {case Success(value) => {printf(value)}
                                              case Failure(exception) => {printf("Failed lol")}})
  res
}

// 3.* свои Monad и Functor .reduceLeft и .reduceRight, .map
// 

trait Functor[F[A]]{
  def map[A,B](f: A=>B):F[B]
}

trait Monad[F[A]]{
  def reduceLeft [A](f: (A,A)=>A):A
  def reduceRight[A](f: (A,A)=>A):A
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