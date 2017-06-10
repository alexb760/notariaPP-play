import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  //"Routes" should {

  //  "send 404 on a bad request" in  {
  //    route(app, FakeRequest(GET, "/notarias")).map(status(_)) mustBe Some(NOT_FOUND)
  //  }

 // }

  "ApplicationControllerNotaria" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/notarias")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      //contentAsString(home) must include ("Your new application is ready.")
    }

  }


  "ApplicationControllerServicio" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/servicios")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      //contentAsString(home) must include ("Your new application is ready.")
    }

  }
  


  "ApplicationController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Bienvenido")
    }

  }


 // "CountController" should {

 //   "return an increasing count" in {
 //     contentAsString(route(app, FakeRequest(GET, "/notarias")).get) mustBe "0"
 //     contentAsString(route(app, FakeRequest(GET, "/notarias")).get) mustBe "1"
 //     contentAsString(route(app, FakeRequest(GET, "/notarias")).get) mustBe "2"
 //   }

 // }

}
