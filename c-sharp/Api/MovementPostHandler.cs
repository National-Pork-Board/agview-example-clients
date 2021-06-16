using System;
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
    public class MovementPostHandler
    {
        private readonly HttpClient _httpClient;
        private readonly AccessTokenHandler _accessTokenHandler;
        private readonly string _baseUrl;
        private readonly MovementDbHandler _movementDbHandler;

        public MovementPostHandler(HttpClient httpClient,
            AccessTokenHandler accessTokenHandler,
            string baseUrl,
            MovementDbHandler movementDbHandler)
        {
            _httpClient = httpClient;
            _accessTokenHandler = accessTokenHandler;
            _baseUrl = baseUrl;
            _movementDbHandler = movementDbHandler;
        }

        public async Task<List<CreatedMovement>> CreateMovements()
        {
            var movements = _movementDbHandler.GetMovementsToLoad();
            var movementAddresses = _movementDbHandler.GetMovementAddressesToLoad();
            var movementAddressesByMovementId = movementAddresses.ToDictionary(k => k.MovementId, v => v);

            var requestBody = movements.Select(CreateRequestBody(movementAddressesByMovementId));

            return await PostMovements(requestBody);
        }

        private async Task<List<CreatedMovement>> PostMovements(IEnumerable<Dictionary<string, object>> requestBody)
        {
            var requestBodyStr = JArray.FromObject(requestBody).ToString();

            var accessToken = _accessTokenHandler.GetNewAccessToken().Result;
            _httpClient.DefaultRequestHeaders.Authorization =
                    new AuthenticationHeaderValue("Bearer", accessToken.Access);
            var response = await _httpClient.PostAsync($"{_baseUrl}/api/v1/movements/", new StringContent(requestBodyStr, Encoding.UTF8, "application/json"));

            var responseBodyStr = response.Content.ReadAsStringAsync().Result;

            return JsonConvert.DeserializeObject<List<CreatedMovement>>(responseBodyStr);
        }

        private static Func<Movement, Dictionary<string, object>> CreateRequestBody(Dictionary<int, MovementAddresses> movementAddressesByMovementId)
        {
            return movement =>
            {
                var source = movementAddressesByMovementId[movement.MovementId].Source;
                var sourceLatitude = movementAddressesByMovementId[movement.MovementId].SourceLatitude;
                var target = movementAddressesByMovementId[movement.MovementId].Target;
                var targetLatitude = movementAddressesByMovementId[movement.MovementId].TargetLatitude;

                var requestBody = new Dictionary<string, object>();
                requestBody.Add("species", movement.Species);
                requestBody.Add("numberInShipment", movement.NumberInShipment);
                requestBody.Add("movementType", movement.MovementType);
                requestBody.Add("movementDatetime", movement.MovementDatetime);

                requestBody.Add("source", source);
                requestBody.Add("sourceLatitude", sourceLatitude);
                requestBody.Add("sourceLongitude", movementAddressesByMovementId[movement.MovementId].SourceLongitude);
                if (source == null && sourceLatitude == null)
                {
                    requestBody.Add("sourceAddress", new Dictionary<string, string> {
                            { "streetAddress", movementAddressesByMovementId[movement.MovementId].SourceStreetAddress },
                            { "city",  movementAddressesByMovementId[movement.MovementId].SourceCity },
                            { "state", movementAddressesByMovementId[movement.MovementId].SourceState },
                            { "zip", movementAddressesByMovementId[movement.MovementId].SourceZip }
                        });
                }

                requestBody.Add("destination", target);
                requestBody.Add("destinationLatitude", targetLatitude);
                requestBody.Add("destinationLongitude", movementAddressesByMovementId[movement.MovementId].TargetLongitude);
                if (target == null && targetLatitude == null)
                {
                    requestBody.Add("destinationAddress", new Dictionary<string, string> {
                            { "streetAddress", movementAddressesByMovementId[movement.MovementId].TargetStreetAddress },
                            { "city",  movementAddressesByMovementId[movement.MovementId].TargetCity },
                            { "state", movementAddressesByMovementId[movement.MovementId].TargetState },
                            { "zip", movementAddressesByMovementId[movement.MovementId].TargetZip }
                        });
                }

                return requestBody;

            };
        }

        public async Task<List<CreatedMovement>> CreateMovementsForDateRange(string startDate, string endDate)
        {
            var movements = _movementDbHandler.GetMovementsToLoad();
            var movementAddresses = _movementDbHandler.GetMovementAddressesToLoad();
            var movementAddressesByMovementId = movementAddresses.ToDictionary(k => k.MovementId, v => v);

            var requestBody= movements.Where(MovementsWithinTimerange(startDate, endDate)).Select(CreateRequestBody(movementAddressesByMovementId));

            return await PostMovements(requestBody);
        }

        private static System.Func<Movement, bool> MovementsWithinTimerange(string startTimeStr, string endTimeStr)
        {
            return movement => {
                var startTime = DateTime.Parse(startTimeStr);
                var endTime = DateTime.Parse(endTimeStr);
                var movementTime = DateTime.Parse(movement.MovementDatetime);

                return startTime.CompareTo(movementTime) <= 0 && endTime.CompareTo(movementTime) >= 0;
            };
        }
    }
}
