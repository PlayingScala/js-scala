import scala.virtualization.lms.common._

import java.io.PrintWriter
import java.io.FileOutputStream

trait ConditionalProg { this: Arith with Equal with Print with IfThenElse =>
  
  def test(x: Rep[Double]): Rep[Double] = {
    
    print("yoyo")
    
    val z = if (x == x) {
      print("yoyo")
      print("xxx")
      print("yoyo")
      (x+4)
    } else {
      (x+6)
    }
    
    print("yyy")
    print("yoyo")
    
    z + (x + 4)
  }
  
}

class TestConditional extends FileDiffSuite {
  
  val prefix = "test-out/"
  
  def testConditional = {
    withOutFile(prefix+"conditional") {
    
      println("-- begin")

      new ConditionalProg with ArithExpOpt with EqualExp with PrintExp
      with IfThenElseExp with CompileScala { self =>
        val codegen = new ScalaGenIfThenElse with ScalaGenArith 
        with ScalaGenEqual with ScalaGenPrint { val IR: self.type = self }
        
        val f = (x: Rep[Double]) => test(x)
        codegen.emitSource(f, "Test", new PrintWriter(System.out))
        val g = compile(f)
        println(g(7))
      }
    
      new ConditionalProg with IfThenElseExp with ArithExpOpt with EqualExp
      with PrintExp { self =>
        val codegen = new JSGenIfThenElse with JSGenArith 
        with JSGenEqual with JSGenPrint { val IR: self.type = self }
        
        val f = (x: Rep[Double]) => test(x)
        codegen.emitSource(f, "main", new PrintWriter(System.out))
        codegen.emitHTMLPage(() => f(7), new PrintWriter(new FileOutputStream(prefix+"conditional.html")))
      }

      println("-- end")
    }
    assertFileEqualsCheck(prefix+"conditional")
    assertFileEqualsCheck(prefix+"conditional.html")
  }
}
