package controllers

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import models.{Notaria, NotariaForm}
import play.api.mvc._
import play.Logger
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.NotariaService

class ApplicationControllerNotaria extends Controller {

  def index = Action.async { implicit request =>
    Logger.info("entro index")
    NotariaService.listAllNotarias map { notarias =>
      Ok(views.html.notariaIndex(NotariaForm.form, notarias))
    }
  }

  def addNotaria() = Action.async { implicit request =>
    var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    var date: String = sdf.format(new java.util.Date())

    Logger.info("entro add")
    NotariaForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.notariaIndex(errorForm, Seq.empty[Notaria]))),
      data => {
        val newNotaria = Notaria(0, data.nombre, data.direccion, data.telefono, data.telefono2, 
                                  data.horario, data.correo, "15", date)
        NotariaService.addNotaria(newNotaria).map(res =>
          Redirect(routes.ApplicationControllerNotaria.index())
        )
      })
  }

  def deleteNotaria(id: Long) = Action.async { implicit request =>
    Logger.info("entro delete")
    NotariaService.deleteNotaria(id) map { res =>
      Redirect(routes.ApplicationControllerNotaria.index())
    }
  }

  def editNotaria(id: Long) = Action.async { implicit request =>
    Logger.info("entro edit")
    NotariaService.getNotaria(id) map { notarias =>
      Ok(views.html.notariaEdit(id, NotariaForm.form, notarias))
    }
  }

  def updateNotaria(id: Long) = Action.async { implicit request =>
    var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    var date: String = sdf.format(new java.util.Date())

    Logger.info("entro update")
    NotariaForm.form.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.notariaEdit(id, errorForm, Seq.empty[Notaria]))),
      data => {
        val newNotaria = Notaria(id, data.nombre, data.direccion, data.telefono, data.telefono2, 
                                  data.horario, data.correo, "15", date)
        NotariaService.updateNotaria(id, newNotaria).map(res =>
          Redirect(routes.ApplicationControllerNotaria.index())
        )
      })
  }

}
