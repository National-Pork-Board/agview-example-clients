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
    public class PremisePostHandler
    {
        private readonly HttpClient _httpClient;
        private readonly AccessTokenHandler _accessTokenHandler;
        private readonly string _baseUrl;
        private readonly PremiseDbHandler _premiseDbHandler;

        public PremisePostHandler(HttpClient httpClient,
            AccessTokenHandler accessTokenHandler,
            string baseUrl,
            PremiseDbHandler premiseDbHandler)
        {
            _httpClient = httpClient;
            _accessTokenHandler = accessTokenHandler;
            _baseUrl = baseUrl;
            _premiseDbHandler = premiseDbHandler;
        }

        public async Task<List<CreatedPremise>> CreatePremises()
        {
            var premises = _premiseDbHandler.GetPremisesToLoad();
            var premiseAddresses = _premiseDbHandler.GetPremiseAddressesToLoad();
            var premiseAddressByUsdaPin = premiseAddresses.ToDictionary(k => k.UsdaPin, v => v);

            var requestBody = premises.Select(premise => new
            {
                usdaPin = premise.UsdaPin,
                premName = premise.PremName,
                species = premise.Species,
                iceContactPhone = premise.IceContactPhone,
                iceContactEmail = premise.IceContactEmail,
                locationType = premise.LocationType,
                siteCapacityNumberBarns = premise.SiteCapacityNumberBarns,
                siteCapacityNumberAnimals = premise.SiteCapacityNumberAnimals,
                numberOfAnimalsOnSite = premise.NumberOfAnimalsOnSite,
                streetAddress = premiseAddressByUsdaPin[premise.UsdaPin].StreetAddress,
                city = premiseAddressByUsdaPin[premise.UsdaPin].City,
                state = premiseAddressByUsdaPin[premise.UsdaPin].State,
                zip = premiseAddressByUsdaPin[premise.UsdaPin].Zip
            });

            var requestBodyStr = JArray.FromObject(requestBody).ToString();

            var accessToken = _accessTokenHandler.GetNewAccessToken().Result;
            _httpClient.DefaultRequestHeaders.Authorization =
                    new AuthenticationHeaderValue("Bearer", accessToken.Access);
            var response = await _httpClient.PostAsync($"{_baseUrl}/api/v1/prems/", new StringContent(requestBodyStr, Encoding.UTF8, "application/json"));

            var responseBodyStr = response.Content.ReadAsStringAsync().Result;

            return JsonConvert.DeserializeObject<List<CreatedPremise>>(responseBodyStr);
        }
    }
}
