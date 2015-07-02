using Microsoft.AspNet.Identity.EntityFramework;
using MySql.Data.Entity;
using RecipeViewer.Data.Migrations;
using RecipeViewer.Models;
using System.Data.Entity;

namespace RecipeViewer.Data
{
    [DbConfigurationType(typeof(MySqlEFConfiguration))]
    public class RecipeViewerDbContext : IdentityDbContext<User>
    {
        public RecipeViewerDbContext()
            : base("RecipeViewerDB", throwIfV1Schema: false)
        {
            Database.SetInitializer(new MigrateDatabaseToLatestVersion<RecipeViewerDbContext, Configuration>());
        }

        public static RecipeViewerDbContext Create()
        {
            return new RecipeViewerDbContext();
        }

        public IDbSet<Recipe> Recipes { get; set; }

        public IDbSet<Ingredient> Ingredients { get; set; }

        public IDbSet<UserImage> UserImages { get; set; }

        public new IDbSet<TEntity> Set<TEntity>() where TEntity : class
        {
            return base.Set<TEntity>();
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Microsoft.AspNet.Identity.EntityFramework.IdentityRole>()
                .Property(c => c.Name).HasMaxLength(128).IsRequired();

            modelBuilder.Entity<User>().ToTable("AspNetUsers")//I have to declare the table name, otherwise IdentityUser will be created
                .Property(c => c.UserName).HasMaxLength(128).IsRequired();
            modelBuilder.Entity<User>().ToTable("AspNetUsers")//I have to declare the table name, otherwise IdentityUser will be created
                .Property(c => c.PasswordHash).HasMaxLength(128).IsRequired();
        }
    }
}
