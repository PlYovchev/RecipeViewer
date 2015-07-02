﻿using RecipeViewer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace RecipeViewer.Web.Models
{
    public class IngredientModel
    {
        public static Expression<Func<Ingredient, IngredientModel>> fromIngredient
        {
            get
            {
                return i => new IngredientModel
                {
                    Id = i.Id,
                    Name = i.Name,
                    Quantity = i.Quantity
                };
            }
        }

        public int Id { get; set; }

        public string Name { get; set; }

        public string Quantity { get; set; }
    }
}