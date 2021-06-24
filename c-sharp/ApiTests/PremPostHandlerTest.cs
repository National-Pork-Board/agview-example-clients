using System.Net.Http;
using System.Threading.Tasks;
using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{
    public class PremPostHandlerTest
    {
        private readonly PremPostHandler _sut;

        private readonly HttpClient _httpClient = new();
        private readonly string BaseUrl = Constants.BaseUrl;
        private readonly string ApiKey = Constants.ApiKey;
        private readonly string ApiSecret = Constants.ApiSecret;

        public PremPostHandlerTest()
        {
            var accessTokenHandler = new AccessTokenHandler(_httpClient, BaseUrl, ApiKey, ApiSecret);
            var premDbHandler = new PremDbHandler(Constants.PremsFilePath, Constants.PremAddressesFilePath);

            _sut = new PremPostHandler(_httpClient, accessTokenHandler, BaseUrl, premDbHandler);
        }

        [Fact]
        public async Task CreatesPrems()
        {
            var actual = await _sut.CreatePrems();

            actual.Should().HaveCountGreaterThan(0);
        }
    }
}
