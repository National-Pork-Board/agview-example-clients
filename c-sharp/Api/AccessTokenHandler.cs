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
        private readonly HttpClient _httpClient;
        private readonly string _baseUrl;
        private readonly string _apiKey;
        private readonly string _apiSecret;

        public AccessTokenHandler(HttpClient httpClient, string baseUrl, string apiKey, string apiSecret)
        {
            _httpClient = httpClient;
            _baseUrl = baseUrl;
            _apiKey = apiKey;
            _apiSecret = apiSecret;
        }

        public async Task<AccessToken> GetNewAccessToken()
        {
            var requestBody = JObject.FromObject(new { key = _apiKey, secret = _apiSecret }).ToString();

            var response = await _httpClient.PostAsync($"{_baseUrl}/auth/org-token/", new StringContent(requestBody, Encoding.UTF8, "application/json"));

            var accessTokenJson = response.Content.ReadAsStringAsync().Result;

            return JsonConvert.DeserializeObject<AccessToken>(accessTokenJson);
        }

        public async Task<AccessToken> GetNonExpiredOrNewAccessToken(AccessToken accessToken, long minimumValidityLeftInSeconds)
        {
            var expirationTimeInSeconds = accessToken.Exp;
            var timeOrigin = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            var expirationTime = timeOrigin.AddSeconds(expirationTimeInSeconds).ToLocalTime();

            if (expirationTime.CompareTo(DateTime.Now.AddSeconds(minimumValidityLeftInSeconds)) > 0)
            {
                return accessToken;
            }

            return await GetNewAccessToken();
        }
    }
}
