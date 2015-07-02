using RecipeViewer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace RecipeViewer.Web.Models
{
    public class RecipeModel
    {
        private ICollection<IngredientModel> ingredients;

        public static Expression<Func<Recipe, RecipeModel>> fromRecipe
        {
            get
            {
                return r => new RecipeModel
                {
                    Id = r.Id,
                    Title = r.Title,
                    AuthorUserName = r.Author.UserName,
                    Rating = r.Rating,
                    ImageUrl = "http://" + HttpContext.Current.Request.Url.Host + ":" + HttpContext.Current.Request.Url.Port + r.ImageUrl,
                    Description = r.Description,
                    DateRecipeAdded = r.DateRecipeAdded,
                    Ingredients = r.Ingredients.AsQueryable().Select(IngredientModel.fromIngredient).ToList()
                };
            }
        }

        public int Id { get; set; }

        public string Title { get; set; }

        public string AuthorUserName { get; set; }

        public int Rating {get; set;}

        public string ImageUrl { get; set; }

        public DateTime? DateRecipeAdded { get; set; }

        public string Description { get; set; }

        public virtual ICollection<IngredientModel> Ingredients
        {
            get { return this.ingredients; }
            set { this.ingredients = value; }
        }
    }
}
