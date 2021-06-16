using System.Net.Http;
using System.Threading.Tasks;
using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{
    public class PremisePostHandlerTest
    {
        private readonly PremisePostHandler _sut;

        private readonly HttpClient _httpClient = new();
        private readonly string BaseUrl = Constants.BaseUrl;
        private readonly string ApiKey = Constants.ApiKey;
        private readonly string ApiSecret = Constants.ApiSecret;

        public PremisePostHandlerTest()
        {
            var accessTokenHandler = new AccessTokenHandler(_httpClient, BaseUrl, ApiKey, ApiSecret);
            var premiseDbHandler = new PremiseDbHandler(TestConstants.PremisesFilePath, TestConstants.PremiseAddressesFilePath);

            _sut = new PremisePostHandler(_httpClient, accessTokenHandler, BaseUrl, premiseDbHandler);
        }

        [Fact]
        public async Task CreatesPremises()
        {
            var actual = await _sut.CreatePremises();

            actual.Should().HaveCountGreaterThan(0);
        }
    }
}
