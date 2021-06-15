using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Npb.Agview.Api.Example
{
    public class AccessTokenHandler
    {
        private readonly HttpClient httpClient;
        private readonly string baseUrl;
        private readonly string apiKey;
        private readonly string apiSecret;

        public AccessTokenHandler(HttpClient httpClient, string baseUrl, string apiKey, string apiSecret)
        {
            this.httpClient = httpClient;
            this.baseUrl = baseUrl;
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
        }

        public async Task<OAuthAccessToken> GetNewAccessToken()
        {
            var requestBody = JObject.FromObject(new { key = apiKey, secret = apiSecret }).ToString();

            var response = await httpClient.PostAsync($"{baseUrl}/auth/org-token/", new StringContent(requestBody, Encoding.UTF8, "application/json"));

            var accessTokenJson =  response.Content.ReadAsStringAsync().Result;

            return JsonConvert.DeserializeObject<OAuthAccessToken>(accessTokenJson);
        }

        public async Task<OAuthAccessToken> GetNonExpiredAccessToken(OAuthAccessToken accessToken,
            long minimumValidityLeftInSeconds)
        {
            var expirationTimeInSeconds = accessToken.Exp;
            var expirationTime =
                new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc)
                        .AddSeconds(expirationTimeInSeconds).ToLocalTime();

            if (expirationTime.CompareTo(DateTime.Now.AddSeconds(minimumValidityLeftInSeconds)) > 0)
            {
                return accessToken;
            }

            return await GetNewAccessToken();
        }
    }
}
