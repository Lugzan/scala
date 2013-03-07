import scala.tools.partest.{CompilerTest, DirectTest}

object Test extends CompilerTest {
  // The program being tested in some fashion
  def code = ""


  // produce the output to be compared against a checkfile
  def show() {
    compileString(newCompiler(args: _*))(macroDef)
    compileString(newCompiler(args: _*))(macroCall)
  }

  // override to add additional settings with strings
  override def extraSettings = "-Yrangepos -Ymacrodebug"

  def macroDef =
    """
      |object Test {
      |  import scala.reflect.macros._
      |  import language.experimental.macros
      |
      |  def testMacro(c: Context {type PrefixType = CC})(e1: c.Expr[Int], e2: c.Expr[String]) = { c.literal("test") }
      |
      |  class CC {
      |    def macroCall(e1: Int, e2: String) = macro testMacro
      |  }
      |}
    """.stripMargin

  def macroCall =
    """
      | object B {
      |   import Test._
      |   import Test$._
      |   val t = new Test.CC().macroCall(1, "test")
      | }
    """.stripMargin
}