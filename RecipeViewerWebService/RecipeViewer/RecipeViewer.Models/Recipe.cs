using System;
using System.Collections.Generic;

namespace RecipeViewer.Models
{
    public class Recipe
    {
        private ICollection<Ingredient> ingredients;

        public Recipe()
        {
            this.ingredients = new HashSet<Ingredient>();
        }

        public int Id { get; set; }

        public string Title { get; set; }

        public int Difficulty { get; set; }

        public float Duration { get; set; }

        public int Category { get; set; }

        public string AuthorId { get; set; }

        public virtual User Author { get; set; }

        public DateTime? DateRecipeAdded { get; set; }

        public int Rating { get; set; }

        public string ImageUrl { get; set; }

        public string Description { get; set; }

        public virtual ICollection<Ingredient> Ingredients
        {
            get { return this.ingredients; }
            set { this.ingredients = value; }
        }
    }
}
