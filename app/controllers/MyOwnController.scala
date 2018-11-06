package controllers

import javax.inject._

import play.api.mvc._

import play.api.libs.json

@Singleton
class MyOwnController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def myownPage = Action {
    Ok(views.html.myown("Mon message"))
  }

  def myownData = Action {
    Ok("5")
  }

  def myownScript = Action {
    Ok(views.js.myown())
  }


}
