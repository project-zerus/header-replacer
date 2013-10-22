package com.xiaomi.xiaoqiang.devtools


import org.specs2.mutable._

class HeaderExtractorTest extends SpecificationWithJUnit {
  "HeaderExtractor" should {

    "test 1" in {
      HeaderExtractor.isHeader("hello.h") shouldEqual true
    }

    "test 2" in {
      HeaderExtractor.isHeader("hello.hh") shouldEqual true
    }

    "test 3" in {
      HeaderExtractor.isHeader("hello.hpp") shouldEqual true
    }

    "test 4" in {
      HeaderExtractor.isHeader("hello.h.c") shouldEqual false
    }

    "test 5" in {
      HeaderExtractor.isHeader("hello.hh.cpp") shouldEqual false
    }

    "test 6" in {
      HeaderExtractor.isHeader("hello.hhh") shouldEqual false
    }

    "test 7" in {
      HeaderExtractor.isHeader("hello.hppp") shouldEqual false
    }

    "test 8" in {
      HeaderExtractor.isHeader("hhhh") shouldEqual false
    }

  }
}
