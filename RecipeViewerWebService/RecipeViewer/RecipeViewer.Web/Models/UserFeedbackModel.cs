using RecipeViewer.Models;
using System;
using System.Linq.Expressions;

namespace RecipeViewer.Web.Models
{
    public class UserFeedbackModel
    {
        public static Expression<Func<UserFeedback, UserFeedbackModel>> fromUserFeedback
        {
            get
            {
                return i => new UserFeedbackModel
                {
                    RecipeId = i.RecipeId,
                    UserId = i.UserId,
                    Rating = i.Rating,
                    Comment = i.Comment
                };
            }
        }

        public int RecipeId { get; set; }

        public string UserId { get; set; }

        public int Rating { get; set; }

        public string Comment { get; set; }
    }
}
