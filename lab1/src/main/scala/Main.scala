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
    i%2 match
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

def compose[A, B, C](func1: A=>B,func2: B=>C): A=>C = (a: A) => func2(func1(a))