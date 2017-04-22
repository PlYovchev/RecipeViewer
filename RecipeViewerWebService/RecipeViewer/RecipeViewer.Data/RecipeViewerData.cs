using RecipeViewer.Data.Repository;
using RecipeViewer.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RecipeViewer.Data
{
    public class RecipeViewerData : IRecipeViewerData
    {
        private DbContext context;
        private IDictionary<Type, object> repositories;

        public RecipeViewerData()
            :this(new RecipeViewerDbContext())
        {
        }

        public RecipeViewerData(DbContext context)
        {
            this.context = context;
            this.repositories = new Dictionary<Type, object>();
        }

        public IRepository<Recipe> Recipes
        {
            get
            {
                return this.GetRepository<Recipe>();
            }
        }

        public IRepository<UserImage> UserImages
        {
            get
            {
                return this.GetRepository<UserImage>();
            }
        }

        public IRepository<UserFeedback> UserFeedbacks
        {
            get
            {
                return this.GetRepository<UserFeedback>();
            }
        }

        public IRepository<Ingredient> Ingredients
        {
            get
            {
                return this.GetRepository<Ingredient>();
            }
        }

        public int SaveChanges()
        {
            return this.context.SaveChanges();
        }

        private IRepository<T> GetRepository<T>() where T : class
        {
            var typeOfRepository = typeof(T);
            if (!this.repositories.ContainsKey(typeOfRepository))
            {
                var newRepository = Activator.CreateInstance(typeof(Repository<T>), context);
                this.repositories.Add(typeOfRepository, newRepository);
            }

            return (IRepository<T>)this.repositories[typeOfRepository];
        }
    }
}
