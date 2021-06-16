using System.Net.Http;
using System.Threading.Tasks;
using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{
    public class MovementPostHandlerTest
    {
        private readonly MovementPostHandler _sut;

        private readonly HttpClient _httpClient = new();
        private readonly string BaseUrl = Constants.BaseUrl;
        private readonly string ApiKey = Constants.ApiKey;
        private readonly string ApiSecret = Constants.ApiSecret;
        private const int TotalNumberOfMovements = 9;
        private const string StartDate = "2021-06-07T13:45";
        private const string EndDate = "2021-06-08T13:45";

        public MovementPostHandlerTest()
        {
            var accessTokenHandler = new AccessTokenHandler(_httpClient, BaseUrl, ApiKey, ApiSecret);
            var movementDbHandler = new MovementDbHandler(Constants.MovementFilePath, Constants.MovementAddressesFilePath);

            _sut = new MovementPostHandler(_httpClient, accessTokenHandler, BaseUrl, movementDbHandler);
        }

        [Fact]
        public async Task CreatesAllMovements()
        {
            var actual = await _sut.CreateMovements();

            actual.Should().HaveCount(TotalNumberOfMovements);
        }

        [Fact]
        public async Task CreatesMovementsForDateRange()
        {
            var actual = await _sut.CreateMovementsForDateRange(StartDate, EndDate);

            actual.Should().HaveCountGreaterThan(0);
            actual.Should().HaveCountLessThan(TotalNumberOfMovements);
        }
    }
}
