package controllers
import javax.inject._

import play.api.mvc._

import play.api.libs.json

import model._

@Singleton
class ProductController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder) extends AbstractController(cc) {

  def all = Action {
    var p1 = new Product("produit1", 11);
    //var p2 = new Product{name="produit2"; age=22};
    var produits = List(p1);

    Ok(views.html.produits(p1))
  }
}
