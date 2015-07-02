using RecipeViewer.Data;
using RecipeViewer.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace RecipeViewer.Web.Controllers
{
    public class UserImagesController : ApiController
    {
         private IRecipeViewerData data;

        public UserImagesController()
            : this(new RecipeViewerData())
        {
        }

        public UserImagesController(IRecipeViewerData data)
        {
            this.data = data;
        }

        public IHttpActionResult getImage(int id)
        {
            return NotFound();
        }

        [HttpPost]
        public IHttpActionResult addImageForUser()
        {
            var task = this.Request.Content.ReadAsStreamAsync();
            task.Wait();
            Stream requestStream = task.Result;

            try
            {
                Stream fileStream = File.Create(HttpContext.Current.Server.MapPath("~/" + "test.png"));
                requestStream.CopyTo(fileStream);
                fileStream.Close();
                requestStream.Close();
            }
            catch (IOException)
            {
               
            }

            //HttpResponseMessage response = new HttpResponseMessage();
            //response.StatusCode = HttpStatusCode.Created;
            //return response;
          //  UserImage userImage = new UserImage();
   //         var currentUserId = this.userIdProvider.GetUserId();
            var userId = User.Identity.IsAuthenticated;
            //userImage.ImageUrl = "dadasd.dadasd";

           // this.data.UserImages.Add(userImage);
          //  this.data.SaveChanges();

            return Ok();
        }

    }

    public class UserImagee
    {
        public string Image { get; set; }
    }
}
