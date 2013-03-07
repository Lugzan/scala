import scala.tools.partest.DirectTest

object Test extends DirectTest {
  // The program being tested in some fashion
  def code = ""

  override def extraSettings = "-Yrangepos -Ymacrodebug"

  // produce the output to be compared against a checkfile
  def show() {
    compileString(newCompiler(args: _*))(macroDef)
    compileString(newCompiler(args: _*))(macroCall)
  }


  def macroDef =
    """
      |import reflect.macros.Context
      |import language.experimental.macros
      |
      |object Debug {
      |  def debug(a: Any): Any = macro debugImpl
      |
      |  def debugImpl(c: Context)(a: c.Expr[Any]) = {
      |    c.universe.reify {
      |      (() => a.splice)()
      |    }
      |  }
      |}
    """.stripMargin

  def macroCall =
    """
      |import Debug._
      |import Debug$._
      |
      |object Test7 {
      |  def main(args: Array[String]): Unit = {
      |    Debug.debug {
      |      println("foo")
      |    }
      |  }
      |}
    """.stripMargin
}