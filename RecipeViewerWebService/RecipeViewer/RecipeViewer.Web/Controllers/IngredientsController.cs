using RecipeViewer.Data;
using RecipeViewer.Web.Models;
using System.Linq;
using System.Web.Http;

namespace RecipeViewer.Web.Controllers
{
    public class IngredientsController : ApiController
    {
        private IRecipeViewerData data;

        public IngredientsController()
            : this(new RecipeViewerData())
        {
        }

        public IngredientsController(IRecipeViewerData data)
        {
            this.data = data;
        }

        [HttpGet]
        public IHttpActionResult IngredientsForRecipe(int id)
        {
            var ingredients = this.data.Ingredients.All()
                    .Where(i => i.RecipeId == id)
                    .Select(IngredientModel.fromIngredient);

            return Ok(ingredients);
        }
    }
}
