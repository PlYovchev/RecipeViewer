using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace RecipeViewer.Models
{
    public class UserFeedback
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        public int RecipeId { get; set; }

        public virtual Recipe Recipe { get; set; }

        public string UserId { get; set; }

        public virtual User User { get; set; }

        public int Rating { get; set; }

        public string Comment { get; set; }
    }
}
