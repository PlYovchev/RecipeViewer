using RecipeViewer.Data;
using RecipeViewer.Models;
using RecipeViewer.Web.Models;
using System.Linq;
using System.Web.Http;

namespace RecipeViewer.Web.Controllers
{
    public class RecipeController : ApiController
    {
        private IRecipeViewerData data;

        public RecipeController()
            : this(new RecipeViewerData())
        {
        }

        public RecipeController(IRecipeViewerData data)
        {
            this.data = data;
        }

        [HttpGet]
        public IHttpActionResult All()
        {
            var recipes = this.data.Recipes.All().Select(RecipeModel.fromRecipe).ToList();
            return Ok(recipes);
        }

        [HttpGet]
        public IHttpActionResult Recipes([FromUri]string filterType, [FromUri]string filterValue)
        {
            string value = "";
            var allRecipesQuery = this.data.Recipes.All();
            IQueryable<RecipeModel> recipes = null;

            if (filterValue != null)
            {
                value = filterValue.ToLower();
            }
            else
            {
                filterType = null;
            }

            switch (filterType)
            {
                case "Title":
                    recipes = allRecipesQuery.Where(r => r.Title.ToLower().Contains(value)).Select(RecipeModel.fromRecipe);
                    break;
                case "Author":
                    recipes = allRecipesQuery.Where(r => r.Author.UserName.ToLower().Contains(value))
                        .Select(RecipeModel.fromRecipe);
                    break;
                case "Ingredient":
                    recipes = allRecipesQuery.Where(r => r.Ingredients.Any(i => i.Product.Name.ToLower().Contains(value)))
                        .Select(RecipeModel.fromRecipe);
                    break;
                case "Rating":
                    int filterRating = 0;
                    if (int.TryParse(value, out filterRating))
                    {
                        recipes = allRecipesQuery.Where(r => r.Rating == filterRating)
                            .Select(RecipeModel.fromRecipe);
                    }
                    else
                    {
                        recipes = allRecipesQuery.Select(RecipeModel.fromRecipe);
                    }

                    break;
                default:
                    recipes = allRecipesQuery.Select(RecipeModel.fromRecipe);
                    break;
            }

            return Ok(recipes);
        }

        [HttpPost]
        public IHttpActionResult AddRecipeComment(UserFeedback feedback)
        {
            if (!this.ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            this.data.UserFeedbacks.Add(feedback);
            this.data.UserFeedbacks.SaveChanges();

            return Ok();
        }


        [HttpPost]
        public IHttpActionResult Create(Recipe recipe)
        {
            if (!this.ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            this.data.Recipes.Add(recipe);
            this.data.Recipes.SaveChanges();

            return Ok(recipe);
        }
    }
}
