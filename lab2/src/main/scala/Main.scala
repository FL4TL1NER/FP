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
  var reqs: Map[String, String => Boolean] = Map("Password len is < 8"                 -> (_.length() >= 8),
                                                 "Password must have lowercase letter" -> (hasNSymbols(_,LOWERCASE_LETTERS,1)),
                                                 "Password must have uppercase letter" -> (hasNSymbols(_,UPPERCASE_LETTERS,1)),
                                                 "Password must have number"           -> (hasNSymbols(_,NUMBERS_LETTERS,1)),
                                                 "Password must have special symbol"   -> (hasNSymbols(_,SPECIAL_LETTERS,1)))
  

  Try(reqs.map((a,b)=>(a,b(password)))) match
    case Failure(exception) => Left(false)
    case Success(value) => value.filter((a,b) => !b)
                                .map((a,b)=>a)
                                .fold("")(_ + " " + _) match
      case "" => Left(true)
      case value => Right(value)
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