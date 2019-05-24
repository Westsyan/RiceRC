package filters

import javax.inject.Inject

import akka.stream.Materializer
import controllers.routes
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class LoginFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {

    if ( false) {
        Future.successful(Results.Redirect(routes.HomeController.home()))
    } else {
      f(rh)
    }
  }
}
