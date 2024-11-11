file:///D:/funct/lab1/src/main/scala/Main.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12-sources.jar!/scala/Int.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 354
uri: file:///D:/funct/lab1/src/main/scala/Main.scala
text:
```scala
@main def main() = {
  //println("hello world")

}

def someFunc(n: Int): Unit = {
  for (i <- Range.inclusive(0,n)) {
    print("hello ")
    if (i%2 == 0)
      printf((i).toString())
    else
      printf((n-i).toString())
  }
}

def someFunc_v2(n: Int): Unit = {
  for (i <- Range.inclusive(0,n)) {
    print("hello ")
    i%2 match@@
      case 0 => printf((i).toString())
      case 1 => printf((n-i).toString())
  }
}

def someFunc2(array: Seq[Int]): (Seq[Int],Seq[Int]) = 
(Range(0,array.length).filter(_%2==0).map(array.apply(_)),
 Range(0,array.length).filter(_%2==1).map(array.apply(_)))

def someFunc3(array: Seq[Int]): Int = array.reduce((a: Int, b:Int) => if a>=b then a else b)

def someFunc4(array: Seq[Int]): Unit = {
  var a = printf
  printf(a.toString())
}

def compose(func1: Double=>Double,func2: Double=>Double): Double=>Double = (a: Double) => func2(func1(a))
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
	dotty.tools.pc.completions.CaseKeywordCompletion$.sortSubclasses(MatchCaseCompletions.scala:326)
	dotty.tools.pc.completions.CaseKeywordCompletion$.matchContribute(MatchCaseCompletions.scala:276)
	dotty.tools.pc.completions.Completions.advancedCompletions(Completions.scala:307)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:109)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:90)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:146)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/Hyperion/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12-sources.jar!/scala/Int.scala