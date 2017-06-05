package controllers

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import models.{Requisito, RequisitoForm}
import play.api.i18n.I18nSupport
import play.api.i18n.Messages
import play.api.mvc._
import play.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import service.RequisitoService


class ApplicationControllerRequisito() extends Controller {
	
	def index(idSer: Long) = Action.async { implicit request =>
	    RequisitoService.getRequisitoXIdServ(idSer) map { requisitos =>
	      Ok(views.html.requisitoIndex(idSer, RequisitoForm.form, requisitos))
	    }
	}


	def upload(idSer: Long) = Action(parse.multipartFormData) { request =>
	 	request.body.file("uplRequisito").map { requisito =>
		    import java.io.File

		    val filename = requisito.filename
		    val contentType = requisito.contentType
		    val strMensaje: String = ""

		    if("application/pdf".equalsIgnoreCase(contentType.toString())){
	            requisito.ref.moveTo(new File(s"public/tmp/requisitos/$filename"))
	            //strMensaje = "Archivo cargado!"
	        }else{
	        	//strMensaje = "La extensión del archivo debe ser pdf."
	        }

	        Redirect(routes.ApplicationControllerRequisito.index(idSer))
	  	}.getOrElse {
	    	Redirect(routes.ApplicationControllerRequisito.index(idSer))
	    	//.flashing("error" -> "Missing file")
	  }
	}

	def alert(nombre:String) = Action{
		Ok("Hola" + nombre)
	}
}
