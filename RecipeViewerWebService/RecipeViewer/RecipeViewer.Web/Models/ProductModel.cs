using RecipeViewer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace RecipeViewer.Web.Models
{
    public class ProductModel
    {
        public static Expression<Func<Product, ProductModel>> fromProduct
        {
            get
            {
                return i => new ProductModel
                {
                    Id = i.Id,
                    Name = i.Name,
                    Category = i.Category
                };
            }
        }

        public static ProductModel fromProductItem(Product product)
        {
            return new ProductModel
            {
                Id = product.Id,
                Name = product.Name,
                Category = product.Category
            };
        }

        public int Id { get; set; }

        public string Name { get; set; }

        public int Category { get; set; }
    }
}