using System;
using System.Net.Http;
using System.Net.Http.Json;

namespace Npb.Agview.Api.Example
{
    public class PremisePostHandler
    {

        private readonly HttpClient httpClient = new HttpClient();

        public PremisePostHandler(HttpClient httpClient)
        {

        }

        public PremisePostHandler()
        {
            //var postUser = new User { Name = "Steve Gordon" };

            //httpClient.PostAsJsonAsync();
        }
    }
}
