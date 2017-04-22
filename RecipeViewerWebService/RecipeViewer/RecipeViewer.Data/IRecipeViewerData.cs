using RecipeViewer.Data.Repository;
using RecipeViewer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RecipeViewer.Data
{
    public interface IRecipeViewerData
    {
        IRepository<Recipe> Recipes { get; }

        IRepository<UserImage> UserImages { get; }

        IRepository<Ingredient> Ingredients { get; }

        IRepository<UserFeedback> UserFeedbacks { get; }

        int SaveChanges();
    }
}
