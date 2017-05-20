package controllers

import models.{Notaria, NotariaForm}
import services.NotariaService
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationControllerNotaria extends Controller {

  def index = Action.async { implicit request =>
    NotariaService.listAllNotarias map { notarias =>
      Ok(views.html.notariaIndex(NotariaForm.form, notarias))
    }
  }

  def addNotaria() = Action.async { implicit request =>
    NotariaForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.notariaIndex(errorForm, Seq.empty[Notaria]))),
      data => {
        val newNotaria = Notaria( 0,
                                  data.nombre, 
                                  data.direccion, 
                                  data.telefono, 
                                  data.telefono2, 
                                  data.horario, 
                                  data.correo, 
                                  data.usuario, 
                                  data.fechacrea)
        NotariaService.addNotaria(newNotaria).map(res =>
          Redirect(routes.ApplicationControllerServicio.index())
        )
      })
  }

  def deleteNotaria(id: Long) = Action.async { implicit request =>
    NotariaService.deleteNotaria(id) map { res =>
      Redirect(routes.ApplicationControllerNotaria.index())
    }
  }

}
