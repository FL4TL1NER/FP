error id: file:///D:/funct/lab2/src/main/scala/Main.scala:[4123..4123) in Input.VirtualFile("file:///D:/funct/lab2/src/main/scala/Main.scala", "import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global // без .Implicits ломается
import scala.concurrent.duration.DurationInt
import scala.concurrent.Await

import scala.util.Try
import scala.util.Success
import scala.util.Failure

import scala.io.StdIn.readLine

@main def main() = {
  printf(integrateConcurent((x: Double) => x*2,0,1,10,10).toString())
  printf("\n")
}

// 1. 
def integrate(f: Double=>Double,L: Double,R: Double,I: Int): Double = {
  val step: Double = (R-L)/I
  val values = Range(0,I).map(L + step/2 + step*_).map(f(_)*step)
  return values.reduce(_ + _)
}

//дописал после лабы
def integrateConcurent(f: Double=>Double,L: Double,R: Double,I: Int,T: Int): Double = {
  val step: Double = (R-L)/T
  val futures: Seq[Future[Double]] = Range(0,T).map((i: Int) => Future {integrate(f,L+step*i,L+step*(i+1),I)})
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
val SPECIAL_LETTERS = "!@#$%^&*()"

def hasNSymbols(a:String,set:String, n:Int): Boolean = a.filter(set.contains(_)).length() >= n

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
  var reqs: Map[String, Try[Boolean]] = Map("Password len is < 8"                 -> Try(password.length() >= 8),
                                            "Password must have lowercase letter" -> Try(hasNSymbols(password,LOWERCASE_LETTERS,1)),
                                            "Password must have uppercase letter" -> Try(hasNSymbols(password,UPPERCASE_LETTERS,1)),
                                            "Password must have number"           -> Try(hasNSymbols(password,NUMBERS_LETTERS,1)),
                                            "Password must have special symbol"   -> Try(hasNSymbols(password,SPECIAL_LETTERS,1)))
  
  if !reqs.filter((a,b) => b match {case Failure(exception) => true // если хоть один Failure то возвращаем Left(false)
                                    case Success(value) => false})
          .isEmpty then return Left(false)
  
  var errors = reqs.filter((a,b) => b match {case Failure(exception) => false // выписываем все ошибки в errors
                                             case Success(value) => !value})
                   .map((a,b) => a)
                   .foldRight("")(_ + " " + _)
  if errors.length() == 0 then Left(true) else Right(errors) // если есть хоть одна ошибка возвращаем строку с ошибками, иначе у нас все хорошо
}

// 
// 

def readPassword(): Future[String] = {
  var res = Future({
    var input: String = readLine()
    var isValid = goodEnoughPassword_v2(input)
    while isValid.fold((fa: Boolean) => !fa,(fb: String) => true) do {
      isValid match
        case Left(value)  => printf("unknown error")
        case Right(value) => printf(value)
      
      input = readLine()
      isValid = goodEnoughPassword_v2(input)
    }
    input
  })
  res.onComplete((a: Try[String]) => a match {case Success(value) => printf(value)
                                              case Failure(exception) => printf("Failed lol")})
  res
}

// 3.* свои Monad и Functor .reduceLeft и .reduceRight, .map?

trait Functor[F[A]]{
  def map[A,B](f: A=>B):F[B]
}

trait")
file:///D:/funct/lab2/src/main/scala/Main.scala
file:///D:/funct/lab2/src/main/scala/Main.scala:104: error: expected identifier; obtained eof
trait
     ^
#### Short summary: 

expected identifier; obtained eof