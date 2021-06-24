using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Npb.Agview.Api.Example
{
    public class PremPostHandler
    {
        private readonly HttpClient _httpClient;
        private readonly AccessTokenHandler _accessTokenHandler;
        private readonly string _baseUrl;
        private readonly PremDbHandler _premDbHandler;

        public PremPostHandler(HttpClient httpClient,
            AccessTokenHandler accessTokenHandler,
            string baseUrl,
            PremDbHandler premDbHandler)
        {
            _httpClient = httpClient;
            _accessTokenHandler = accessTokenHandler;
            _baseUrl = baseUrl;
            _premDbHandler = premDbHandler;
        }

        public async Task<List<CreatedPrem>> CreatePrems()
        {
            var prems = _premDbHandler.GetPremsToLoad();
            var premAddresses = _premDbHandler.GetPremAddressesToLoad();
            var premAddressByUsdaPin = premAddresses.ToDictionary(k => k.UsdaPin, v => v);

            var requestBody = prems.Select(prem => new
            {
                usdaPin = prem.UsdaPin,
                premName = prem.PremName,
                species = prem.Species,
                iceContactPhone = prem.IceContactPhone,
                iceContactEmail = prem.IceContactEmail,
                locationType = prem.LocationType,
                siteCapacityNumberBarns = prem.SiteCapacityNumberBarns,
                siteCapacityNumberAnimals = prem.SiteCapacityNumberAnimals,
                numberOfAnimalsOnSite = prem.NumberOfAnimalsOnSite,
                streetAddress = premAddressByUsdaPin[prem.UsdaPin].StreetAddress,
                city = premAddressByUsdaPin[prem.UsdaPin].City,
                state = premAddressByUsdaPin[prem.UsdaPin].State,
                zip = premAddressByUsdaPin[prem.UsdaPin].Zip
            });

            var requestBodyStr = JArray.FromObject(requestBody).ToString();

            var accessToken = _accessTokenHandler.GetNewAccessToken().Result;
            _httpClient.DefaultRequestHeaders.Authorization =
                    new AuthenticationHeaderValue("Bearer", accessToken.Access);
            var response = await _httpClient.PostAsync($"{_baseUrl}/api/v1/prems/", new StringContent(requestBodyStr, Encoding.UTF8, "application/json"));

            var responseBodyStr = response.Content.ReadAsStringAsync().Result;

            return JsonConvert.DeserializeObject<List<CreatedPrem>>(responseBodyStr);
        }
    }
}
