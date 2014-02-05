package im.chic.devtools

import org.specs2.mutable._

class HeaderReplacerTest extends SpecificationWithJUnit {
  "HeaderReplacer" should {

    "test 1" in {
      HeaderReplacer.isCppSource("hello.h") shouldEqual true
    }

    "test 2" in {
      HeaderReplacer.isCppSource("hello.hpp") shouldEqual true
    }

    "test 3" in {
      HeaderReplacer.isCppSource("hello.h.c") shouldEqual true
    }

    "test 4" in {
      HeaderReplacer.isCppSource("hello.hh.cpp") shouldEqual true
    }

    "test 5" in {
      HeaderReplacer.isCppSource("hello.hhh") shouldEqual false
    }

    "test 6" in {
      HeaderReplacer.isCppSource("hello.hppp") shouldEqual false
    }

    "test 7" in {
      HeaderReplacer.isCppSource("hhhh") shouldEqual false
    }

    "test 8" in {
      HeaderReplacer.processLine("fdasfdsa", "a", "b") shouldEqual "fdasfdsa"
    }

    "test 9" in {
      HeaderReplacer.processLine("#include <a>", "a", "b") shouldEqual """#include "b" """.trim
    }

    "test 10" in {
      HeaderReplacer.processLine("""#include "a" """.trim, "a", "b") shouldEqual """#include "b" """.trim
    }

    "test 11" in {
      HeaderReplacer.processLine("#include  <a>", "a", "b") shouldEqual """#include "b" """.trim
    }

    "test 12" in {
      HeaderReplacer.processLine("#include \t <a>", "a", "b") shouldEqual """#include "b" """.trim
    }

    "test 13" in {
      HeaderReplacer.processLine("#include\t<a>", "a", "b") shouldEqual """#include "b" """.trim
    }

    "test 14" in {
      HeaderReplacer.processLine("  \t#include<a/b.h>\t ", "a/b.h", "b") shouldEqual "  \t#include \"b\"\t "
    }
  }
}
